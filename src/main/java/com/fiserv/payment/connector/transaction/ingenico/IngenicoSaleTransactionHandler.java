package com.fiserv.payment.connector.transaction.ingenico;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;
import com.fiserv.payment.connector.constant.OPIConstants;
import com.fiserv.payment.connector.device.type.DeviceType;
import com.fiserv.payment.connector.model.arcsi.request.Amount;
import com.fiserv.payment.connector.model.arcsi.request.Host;
import com.fiserv.payment.connector.model.arcsi.request.Request;
import com.fiserv.payment.connector.model.arcsi.request.Resource;
import com.fiserv.payment.connector.model.arcsi.response.Card;
import com.fiserv.payment.connector.model.arcsi.response.IngenicoResponse;
import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;
import com.fiserv.payment.connector.transaction.TransactionHandler;
import com.fiserv.payment.connector.transaction.ingenico.values.IngenicoResponseEnum;
import com.fiserv.payment.connector.transaction.ingenico.values.RESPONSE_OBJECT_TYPE;
import com.fiserv.payment.connector.transaction.ingenico.websocket.listener.IngenicoTransactionRequestResponseProcessor;
import com.fiserv.payment.connector.transaction.ingenico.websocket.pending.ArcPendingRequests.ArcPendingResult;
import com.fiserv.payment.connector.transaction.type.TransactionType;

/**
 * 
 */
@Component
public class IngenicoSaleTransactionHandler implements TransactionHandler {

    private static Logger logger = LoggerFactory.getLogger(IngenicoSaleTransactionHandler.class);

    private final IngenicoTransactionRequestResponseProcessor transactionProcessor;

    /**
     * @param transactionProcessor
     */
    public IngenicoSaleTransactionHandler(IngenicoTransactionRequestResponseProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.INGENICO;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.SALE;
    }

    @Override
    public OPITransactionResponse process(OPITransactionRequest opiTransactionRequest, DeviceEntry deviceEntry) {

        // 1. Convert EFTLink request -> ARC-SI Sale request
        Request generateRequest = generateRequest(opiTransactionRequest, deviceEntry);
        // 2. Send request to ARC-SI
        // 3. Receive ARC-SI response
        ArcPendingResult saleResult = transactionProcessor.executeRequest(generateRequest, deviceEntry);
        // 4. Convert ARC-SI response -> EFTLink response
        OPITransactionResponse opiTransactionResponse;

        if (saleResult.isDuplicateInFlight()) {
            opiTransactionResponse = new OPITransactionResponse();
            opiTransactionResponse.setSequenceNo(opiTransactionRequest.getSequenceNo());
            opiTransactionResponse.setTransType(opiTransactionRequest.getTransType());
            opiTransactionResponse.setRespCode(IngenicoResponseEnum.DEFAULT_ERROR_DECLINE.getOpi_retial_response_code());
            opiTransactionResponse.setRespText("Transaction already in progress");
        } else if (saleResult.isTimedOut()) {
            opiTransactionResponse = new OPITransactionResponse();
            opiTransactionResponse.setSequenceNo(opiTransactionRequest.getSequenceNo());
            opiTransactionResponse.setTransType(opiTransactionRequest.getTransType());
            opiTransactionResponse.setRespCode(IngenicoResponseEnum.CARD_READ_TIMEOUT.getOpi_retial_response_code());
            opiTransactionResponse.setRespText(IngenicoResponseEnum.CARD_READ_TIMEOUT.getResponseMessage());
        } else if (saleResult.getResponse() == null) {
            opiTransactionResponse = new OPITransactionResponse();
            opiTransactionResponse.setSequenceNo(opiTransactionRequest.getSequenceNo());
            opiTransactionResponse.setTransType(opiTransactionRequest.getTransType());
            opiTransactionResponse
                    .setRespCode(IngenicoResponseEnum.DEFAULT_ERROR_DECLINE.getOpi_retial_response_code());
            opiTransactionResponse.setRespText(IngenicoResponseEnum.DEFAULT_ERROR_DECLINE.getResponseMessage());
        } else {
            opiTransactionResponse = convertResponse(saleResult.getResponse(), opiTransactionRequest);
        }

        logger.info("Ingenico SALE processed:{} ",opiTransactionRequest.getTransType());

        return opiTransactionResponse;
    }

    public Request generateRequest(OPITransactionRequest opiTransactionRequest, DeviceEntry deviceEntry) {

        Request ingenicoRequest = new Request();
        ingenicoRequest.setFlow_id(UUID.randomUUID().toString().replace("-", ""));
        ingenicoRequest.setEndpoint(deviceEntry.getEndPointUrl());

        Resource resource = new Resource();

        Amount amount = new Amount();
        amount.setTotal(Integer.parseInt(opiTransactionRequest.getTransAmount()));
        resource.setAmount(amount);

        resource.setConfirm_amount(true); // pull from config file
        resource.setDisplay_txn_result(true);// pull from config file
        resource.setManual_entry(false);// pull from config file
        resource.setPartial_approval(true);// pull from config file

        List<String> paymentType = new ArrayList<String>();
        paymentType.add(OPIConstants.PAYMENT_TYPE_CREDIT);
        paymentType.add(OPIConstants.PAYMENT_TYPE_DEBIT);
        resource.setPaymentType(paymentType);

        resource.setQsr_limit(0);

        List<String> readers = new ArrayList<String>();
        resource.setReaders(readers);
        readers.add(OPIConstants.READERS_CONTACTLESS);
        readers.add(OPIConstants.READERS_MSR);
        readers.add(OPIConstants.READERS_SMARTCARD);

        resource.setReturn_to_idle(true);
        resource.setToken_request(true);
        resource.setTxn_status_events(false);
        resource.setType(TransactionType.SALE.getTransactionTypeARCSI());

        ingenicoRequest.setResource(resource);

        return ingenicoRequest;
    }

    public OPITransactionResponse convertResponse(IngenicoResponse response,
            OPITransactionRequest opiTransactionRequest) {

        OPITransactionResponse opiResponse = new OPITransactionResponse();

        opiResponse.setSequenceNo(opiTransactionRequest.getSequenceNo());
        opiResponse.setTransType(opiTransactionRequest.getTransType());

        // Path A: transaction_error, card timeout (74), user cancel (10), etc.
        if (hasResourceErrorInfo(response)) {
            mapResourceErrorToOpi(opiResponse, response);
            return opiResponse;
        }

        // Path B: transaction_completed — host approval OR host decline (54, 05, …); no
        // error_info
        Host host = getHost(response);
        if (host != null) {
            mapHostResultToOpi(opiResponse, host);
            if (isHostApproved(host)) {
                mapHostApprovalDetails(opiResponse, response, host);
            }
            return opiResponse;
        }

        mapDefaultDecline(opiResponse);
        return opiResponse;
    }

    /** Terminal / manager errors (cancel, timeout, …) — not host auth decline. */
    private void mapResourceErrorToOpi(OPITransactionResponse opiResponse, IngenicoResponse response) {
        IngenicoResponseEnum mapped = IngenicoResponseEnum.DEFAULT_ERROR_DECLINE;
        String processorCode = response.getEvent().getResource().getError_info().getCode();
        if (processorCode != null) {
            mapped = IngenicoResponseEnum.getResponseCode(processorCode);
        }
        opiResponse.setRespCode(mapped.getOpi_retial_response_code());
        String deviceText = response.getEvent().getResource().getError_info().getText();
        if (deviceText != null && !deviceText.isBlank()) {
            opiResponse.setRespText(deviceText);
        } else {
            opiResponse.setRespText(mapped.getResponseMessage());
        }
    }

    /**
     * Any host response code + text (decline, expired card, or approval header
     * before extra fields).
     */
    private void mapHostResultToOpi(OPITransactionResponse opiResponse, Host host) {
        if (host.getResponse_code() != null) {
            opiResponse.setRespCode(host.getResponse_code());
        }
        String text = host.getResponse_text();
        if (text == null || text.isBlank()) {
            text = host.getCustomer_text();
        }
        if (text != null && !text.isBlank()) {
            opiResponse.setRespText(text);
        } else if (opiResponse.getRespCode() != null && !isHostApproved(host)) {
            opiResponse.setRespText(IngenicoResponseEnum.DEFAULT_ERROR_DECLINE.getResponseMessage());
        }
    }

    private void mapHostApprovalDetails(OPITransactionResponse opiResponse, IngenicoResponse response, Host host) {
        opiResponse.setTransAmount(host.getAuthorized_amount());
        opiResponse.setRRN(response.getEvent().getResource().getClient_txn_id());
        //opiResponse.setRRN(response.getEvent().getResource().getHost().getTxn_id());

        opiResponse.setAuthCode(host.getApproval_code());
        opiResponse.setIssuerId(host.getAuth_network_id());
        if (response.getEvent().getResource().getToken() != null) {
            opiResponse.setTransToken(response.getEvent().getResource().getToken().getValue());
        }
        Object cardObject = getResponseObject(response, RESPONSE_OBJECT_TYPE.CARD);
        if (cardObject instanceof Card card) {
            opiResponse.setPAN(card.getPan());
            opiResponse.setExpiryDate(card.getExp());
        }
    }

    private void mapDefaultDecline(OPITransactionResponse opiResponse) {
        opiResponse.setRespCode(IngenicoResponseEnum.DEFAULT_ERROR_DECLINE.getOpi_retial_response_code());
        opiResponse.setRespText(IngenicoResponseEnum.DEFAULT_ERROR_DECLINE.getResponseMessage());
    }

    private boolean isHostApproved(Host host) {
        return host.getResponse_code() != null && "00".equalsIgnoreCase(host.getResponse_code().trim());
    }

    private Host getHost(IngenicoResponse response) {
        Object hostObject = getResponseObject(response, RESPONSE_OBJECT_TYPE.HOST);
        if (hostObject instanceof Host host) {
            return host;
        }
        else return null;
    }

    private boolean hasResourceErrorInfo(IngenicoResponse response) {
        return response != null && response.getEvent() != null && response.getEvent().getResource() != null
                && response.getEvent().getResource().getError_info() != null;
    }

    private Object getResponseObject(IngenicoResponse response, RESPONSE_OBJECT_TYPE type) {
        Object obj = null;
        if (response !=null && response.getEvent() != null && response.getEvent().getResource() != null) {
            switch (type) {
            case HOST:
                if (response.getEvent().getResource().getHost() != null)
                    obj = response.getEvent().getResource().getHost();
                break;
            case CARD:
                if (response.getEvent().getResource().getCard() != null)
                    obj = response.getEvent().getResource().getCard();
                break;
            default:
                break;
            }

        }

        return obj;
    }

}
