package com.fiserv.payment.connector.transaction.ingenico.websocket.listener;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;
import com.fiserv.payment.connector.database.model.TransactionRequest;
import com.fiserv.payment.connector.database.service.TransactionRequestDataService;
import com.fiserv.payment.connector.log.PortLoggerFactory;
import com.fiserv.payment.connector.model.arcsi.request.IngenicoRequest;
import com.fiserv.payment.connector.model.arcsi.request.Request;
import com.fiserv.payment.connector.model.arcsi.request.Resource;
import com.fiserv.payment.connector.model.arcsi.response.EventAck;
import com.fiserv.payment.connector.model.arcsi.response.IngenicoEventAck;
import com.fiserv.payment.connector.model.arcsi.response.IngenicoResponse;
import com.fiserv.payment.connector.transaction.ingenico.values.RESOURCE_STATUS;
import com.fiserv.payment.connector.transaction.ingenico.values.RESPONSE_RESOURCET_TYPE;
import com.fiserv.payment.connector.transaction.ingenico.websocket.pending.ArcPendingRequests;

@Component
public class IngenicoTransactionRequestResponseProcessor {

    private final ObjectMapper objectMapper;
    private final ArcPendingRequests pendingSales;

    private final TransactionRequestDataService transactionRequestDataService;

    /**
     * Shared client (Java 11–17: HttpClient is not AutoCloseable; reuse avoids
     * per-sale leak warnings).
     */
    private final HttpClient httpClient = HttpClient.newHttpClient();

    IngenicoTransactionRequestResponseProcessor(ObjectMapper objectMapper, ArcPendingRequests pendingSales,
            TransactionRequestDataService transactionRequestDataService) {
        this.objectMapper = objectMapper;
        this.pendingSales = pendingSales;
        this.transactionRequestDataService = transactionRequestDataService;
    }

    public ArcPendingRequests.ArcPendingResult executeRequest(Request request, DeviceEntry deviceEntry) {
        String flowId = request.getFlow_id();
        long maxLifecycleMs = deviceEntry.getMaxSaleLifecycleMs();
        ArcPendingRequests.PendingTransactions sale = pendingSales.track(flowId, maxLifecycleMs);
        if (sale == null) {
            return ArcPendingRequests.ArcPendingResult.duplicateInFlight();
        }

        String portStr = String.valueOf(deviceEntry.getPort());
        PortLoggerFactory.LoggersContainer loggers = PortLoggerFactory.getLoggersForPort(portStr);

        WebSocket webSocket = null;
        try {
            IngenicoRequest req = new IngenicoRequest();
            req.setRequest(request);
            String arcSIRequestStr = objectMapper.writeValueAsString(req);

            loggers.reqResLogger.debug("ARC-SI request sending flow_id={}", flowId);
            loggers.reqResLogger.debug("arcSIRequestStr : {}", arcSIRequestStr);

            persistTransactionRequest(flowId, arcSIRequestStr, loggers.appLogger);

            long saleTimeoutMs = deviceEntry.getTimeoutMs();

            loggers.appLogger.info("Initiating WebSocket connection to target endpoint: {}",
                    deviceEntry.getWebsocketUrl());
            IngenicoWebSocketListener listener = new IngenicoWebSocketListener(req, saleTimeoutMs, portStr);
            webSocket = httpClient.newWebSocketBuilder().buildAsync(URI.create(deviceEntry.getWebsocketUrl()), listener)
                    .join();
            sale.setSocket(webSocket);
            pendingSales.refreshTimeout(sale, saleTimeoutMs);

            webSocket.sendText(arcSIRequestStr, true);
            ArcPendingRequests.ArcPendingResult result = sale.getWaiter().join();
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Done");

            if (result.isTimedOut()) {
                logTimedOutTransactionRetained(flowId, loggers.appLogger);
            } else if (result.getResponse() != null && !isTerminalErrorResult(result)) {
                removeTransactionRequest(flowId);
            }
            return result;
        } catch (Exception e) {
            loggers.appLogger.error("Sale failed for flow_id {}", flowId, e);
            sale.onError();
            return new ArcPendingRequests.ArcPendingResult();
        } finally {
            sale.cancelTimeoutTask();
            pendingSales.drop(flowId, sale);
        }
    }

    private boolean isTerminalErrorResult(ArcPendingRequests.ArcPendingResult result) {
        IngenicoResponse r = result.getResponse();
        return r != null && r.getEvent() != null && r.getEvent().getResource() != null
                && r.getEvent().getResource().getError_info() != null;
    }

    private void logTimedOutTransactionRetained(String flowId, Logger portAppLogger) {
        TransactionRequest transactionRequest = transactionRequestDataService.readTransactionRequest(flowId);
        if (transactionRequest != null) {
            portAppLogger.warn("Sale timed out; persisted transaction request retained for recovery flow_id={}",
                    flowId);
        } else {
            portAppLogger.warn("Sale timed out flow_id={}", flowId);
        }
    }

    private void removeTransactionRequest(String flowId) {
        TransactionRequest transactionRequest = transactionRequestDataService.readTransactionRequest(flowId);
        if (transactionRequest != null) {
            transactionRequestDataService.deleteTransactionRequest(transactionRequest);
        }
    }

    private void persistTransactionRequest(String flowId, String arcSIRequestStr, Logger portAppLogger) {
        TransactionRequest transactionRequest = transactionRequestDataService.readTransactionRequest(flowId);
        if (transactionRequest == null) {
            transactionRequest = new TransactionRequest();
            transactionRequest.setFlowid(flowId);
            transactionRequest.setExistingFlowId(flowId);
            transactionRequest.setRequestJSON(arcSIRequestStr);
            portAppLogger.info(transactionRequestDataService.createTransactionRequest(transactionRequest));
        } else {
            transactionRequest.setFlowid(flowId);
            transactionRequest.setRequestJSON(arcSIRequestStr);
            portAppLogger.info(transactionRequestDataService.updateTransactionRequest(transactionRequest));
        }
    }

    class IngenicoWebSocketListener implements WebSocket.Listener {

        IngenicoRequest request;
        private final long saleTimeoutMs;
        private final PortLoggerFactory.LoggersContainer loggers;
        /**
         * Device 7000+ may split large JSON (e.g. transaction_completed) across
         * multiple WS frames.
         */
        private final StringBuilder wsMessageBuffer = new StringBuilder();

        public IngenicoWebSocketListener(IngenicoRequest req, long saleTimeoutMs, String assignedPort) {
            this.request = req;
            this.saleTimeoutMs = saleTimeoutMs;
            this.loggers = PortLoggerFactory.getLoggersForPort(assignedPort);
        }

        /**
         * Same framing as legacy Ingenico listener (device 7000 firmware): accumulate
         * chunks until {@code last}, then parse once. Non-last frames only append — no
         * JSON parse.
         */
        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            loggers.reqResLogger.debug("ARC-SI WS frame flow_id={} chunkLen={} last={}",
                    request.getRequest().getFlow_id(), data.length(), last);

            if (last) {
                wsMessageBuffer.append(data);
                String payload = wsMessageBuffer.toString();
                wsMessageBuffer.setLength(0);

                loggers.reqResLogger.debug("ARC-SI WS full message flow_id={} length={}",
                        request.getRequest().getFlow_id(), payload.length());
                loggers.reqResLogger.debug("Received: {}", payload);

                return handleArcsiMessage(webSocket, data, last, payload);
            }

            wsMessageBuffer.append(data);
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        private CompletionStage<?> handleArcsiMessage(WebSocket webSocket, CharSequence data, boolean last,
                String payload) {
            IngenicoResponse responseObj = processData(payload);
            if (responseObj == null) {
                return handleTextMessage(webSocket, data, last);
            }

            String flowId = resolveFlowId(responseObj);
            if (flowId == null) {
                return handleTextMessage(webSocket, data, last);
            }

            ArcPendingRequests.PendingTransactions sale = pendingSales.find(flowId);
            if (sale == null) {
                loggers.appLogger.warn("No open sale session detected for flow_id {}", flowId);
                return handleTextMessage(webSocket, data, last);
            }

            if (responseObj.getEvent() != null && responseObj.getEvent().getResource() != null) {
                return handleArcsiEvent(webSocket, data, last, responseObj, sale, flowId);
            }
            if (isValidResponse(responseObj, request)) {
                handleSyncResponse(responseObj, sale);
            }
            return handleTextMessage(webSocket, data, last);
        }

        private CompletionStage<?> handleTextMessage(WebSocket webSocket, CharSequence data, boolean last) {
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        private String resolveFlowId(IngenicoResponse responseObj) {
            if (responseObj.getEvent() != null && responseObj.getEvent().getFlowId() != null) {
                return responseObj.getEvent().getFlowId();
            }
            if (responseObj.getResponse() != null) {
                return responseObj.getResponse().getFlow_id();
            }
            return null;
        }

        private RESOURCE_STATUS parseResourceStatus(Resource resource) {
            if (resource.getStatus() == null) {
                return RESOURCE_STATUS.unknown;
            }
            try {
                return RESOURCE_STATUS.valueOf(resource.getStatus());
            } catch (IllegalArgumentException ex) {
                return RESOURCE_STATUS.unknown;
            }
        }

        private CompletionStage<?> handleArcsiEvent(WebSocket webSocket, CharSequence data, boolean last,
                IngenicoResponse responseObj, ArcPendingRequests.PendingTransactions sale, String flowId) {
            Resource resource = responseObj.getEvent().getResource();
            String type = resource.getType();
            RESOURCE_STATUS stat = parseResourceStatus(resource);

            if (RESPONSE_RESOURCET_TYPE.transaction_completed.name().equalsIgnoreCase(type)) {
                handleTransactionCompleted(webSocket, responseObj, sale, stat);
                return handleTextMessage(webSocket, data, last);
            }
            if (RESPONSE_RESOURCET_TYPE.transaction_error.name().equalsIgnoreCase(type)) {
                handleTransactionError(webSocket, responseObj, sale, flowId, resource);
                return handleTextMessage(webSocket, data, last);
            }
            if (RESPONSE_RESOURCET_TYPE.post_txn_reset.name().equalsIgnoreCase(type)) {
                return finishPostTransactionReset(webSocket, data, last, responseObj, sale, flowId, stat);
            }
            acknowledgeEventIfCompletedOrProceed(webSocket, responseObj, stat);
            return handleTextMessage(webSocket, data, last);
        }

        private void handleTransactionCompleted(WebSocket webSocket, IngenicoResponse responseObj,
                ArcPendingRequests.PendingTransactions sale, RESOURCE_STATUS stat) {
            sale.onTxnCompleted(responseObj);
            pendingSales.refreshTimeout(sale, saleTimeoutMs);
            acknowledgeEventIfCompletedOrProceed(webSocket, responseObj, stat);
        }

        private void handleTransactionError(WebSocket webSocket, IngenicoResponse responseObj,
                ArcPendingRequests.PendingTransactions sale, String flowId, Resource resource) {
            if (sale.isClosed() || resource.getError_info() == null) {
                return;
            }
            sendAcknowledgement(webSocket, getEventAcknowledgementOK(request));
            sale.onArcError(responseObj);
            loggers.appLogger.info("transaction_error handled flow_id={} code={}", flowId,
                    resource.getError_info().getCode());
        }

        private void acknowledgeEventIfCompletedOrProceed(WebSocket webSocket, IngenicoResponse responseObj,
                RESOURCE_STATUS stat) {
            if (stat == RESOURCE_STATUS.completed || stat == RESOURCE_STATUS.proceed) {
                sendAcknowledgement(webSocket, getEventAcknowledgementOK(request));
            }
        }

        private CompletionStage<?> finishPostTransactionReset(WebSocket webSocket, CharSequence data, boolean last,
                IngenicoResponse responseObj, ArcPendingRequests.PendingTransactions sale, String flowId,
                RESOURCE_STATUS stat) {
            CompletableFuture<Void> ackDone = CompletableFuture.completedFuture(null);
            if (stat == RESOURCE_STATUS.completed || stat == RESOURCE_STATUS.proceed) {
                ackDone = sendAcknowledgement(webSocket, getEventAcknowledgementOK(request));
            }
            return ackDone.thenRun(() -> executePostTransactionReset(sale, flowId)).exceptionally(ex -> {
                loggers.appLogger.error("post_txn_reset lifecycle aborted after ACK failure flow_id={}", flowId, ex);
                sale.onError();
                return null;
            }).thenCompose(v -> handleTextMessage(webSocket, data, last));
        }

        private void executePostTransactionReset(ArcPendingRequests.PendingTransactions sale, String flowId) {
            if (sale.onPostTxnReset()) {
                return;
            }
            if (sale.isClosed()) {
                loggers.reqResLogger.debug("post_txn_reset after flow already closed flow_id={}", flowId);
                return;
            }
            loggers.appLogger.warn("post_txn_reset without approval; failing open flow flow_id={}", flowId);
            sale.onError();
        }

        private void handleSyncResponse(IngenicoResponse responseObj, ArcPendingRequests.PendingTransactions sale) {
            Resource resource = responseObj.getResponse().getResource();
            RESOURCE_STATUS stat = RESOURCE_STATUS.valueOf(resource.getStatus());
            if (stat == RESOURCE_STATUS.started) {
                sale.onStarted();
                pendingSales.refreshTimeout(sale, saleTimeoutMs);
            } else if (stat == RESOURCE_STATUS.error) {
                sale.onArcError(responseObj);
            }
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            wsMessageBuffer.setLength(0);
            loggers.appLogger.info("WebSocket channel closed flow_id={} code={} reason={}",
                    request.getRequest().getFlow_id(), statusCode, reason);
            loggers.reqResLogger.info("Closed: {}", reason);
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            wsMessageBuffer.setLength(0);
            loggers.appLogger.error("WebSocket transport error flow_id={}", request.getRequest().getFlow_id(), error);
            ArcPendingRequests.PendingTransactions sale = pendingSales.find(request.getRequest().getFlow_id());
            if (sale != null && !sale.isClosed()) {
                sale.onError();
            }
            WebSocket.Listener.super.onError(webSocket, error);
        }

        private IngenicoResponse processData(CharSequence data) {
            try {
                return objectMapper.readValue(data.toString(), IngenicoResponse.class);
            } catch (JacksonException e) {
                loggers.appLogger.error("Failed to parse ARC-SI WebSocket message flow_id={}",
                        request.getRequest().getFlow_id(), e);
            }
            return null;
        }

        private boolean isValidResponse(IngenicoResponse response, IngenicoRequest req) {
            if (response != null && response.getResponse() != null && response.getResponse().getResource() != null) {
                return response.getResponse().getFlow_id().equalsIgnoreCase(req.getRequest().getFlow_id());
            }
            return false;
        }

        private IngenicoEventAck getEventAcknowledgementOK(IngenicoRequest engenicoRequest) {
            IngenicoEventAck ack = new IngenicoEventAck();
            EventAck eventAck = new EventAck();
            eventAck.setFlowId(engenicoRequest.getRequest().getFlow_id());
            eventAck.setEndpoint(engenicoRequest.getRequest().getEndpoint());

            Resource res = new Resource();
            res.setStatus("ok");
            eventAck.setResource(res);

            ack.setEventAck(eventAck);

            return ack;
        }

        private CompletableFuture<Void> sendAcknowledgement(WebSocket webSocket, IngenicoEventAck ack) {
            try {
                String jsonStr = objectMapper.writeValueAsString(ack);
                String ackFlowId = ack.getEventAck() != null ? ack.getEventAck().getFlowId() : "?";
                loggers.reqResLogger.debug("Sending event_ack flow_id={}", ackFlowId);
                loggers.reqResLogger.debug("Event ACK payload: {}", jsonStr);
                return webSocket.sendText(jsonStr, true)
                        .thenAccept(ws -> loggers.reqResLogger.debug("event_ack sent flow_id={}", ackFlowId))
                        .exceptionally(ex -> {
                            loggers.appLogger.error("event_ack send failed flow_id={}", ackFlowId, ex);
                            throw new java.util.concurrent.CompletionException(ex);
                        });
            } catch (JacksonException e) {
                loggers.appLogger.error("event_ack serialization failed", e);
                return CompletableFuture.failedFuture(e);
            }
        }
    }
}
