package com.fiserv.payment.connector.transaction.ingenico.websocket.pending;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fiserv.payment.connector.model.arcsi.response.IngenicoResponse;

import jakarta.annotation.PreDestroy;

@Component
public class ArcPendingRequests {

    private static final Logger logger = LoggerFactory.getLogger(ArcPendingRequests.class);

    public enum ArcFlowState {
        WAITING,
        STARTED,
        TXN_DONE
    }

    private final ConcurrentHashMap<String, PendingTransactions> openSales = new ConcurrentHashMap<>();
    private final ScheduledExecutorService timeoutExecutor;

    @Autowired
    public ArcPendingRequests() {
        timeoutExecutor = Executors.newScheduledThreadPool(2);
    }

    public ArcPendingRequests(ScheduledExecutorService timeoutExecutor) {
        this.timeoutExecutor = timeoutExecutor;
    }

    @PreDestroy
    void shutdown() {
        timeoutExecutor.shutdownNow();
    }

    PendingTransactions track(String flowId) {
        return track(flowId, 0L);
    }

    public PendingTransactions track(String flowId, long maxLifecycleMs) {
        PendingTransactions sale = new PendingTransactions(this, flowId, maxLifecycleMs);
        if (openSales.putIfAbsent(flowId, sale) != null) {
            logger.warn("Sale already in progress for flow_id {}", flowId);
            return null;
        }
        return sale;
    }

    public PendingTransactions find(String flowId) {
        return flowId == null ? null : openSales.get(flowId);
    }

    public void drop(String flowId, PendingTransactions sale) {
        openSales.remove(flowId, sale);
    }

    private ScheduledFuture<?> armTimeout(PendingTransactions sale, long timeoutMs) {
        return timeoutExecutor.schedule(sale::onTimeout, timeoutMs, TimeUnit.MILLISECONDS);
    }

    public void refreshTimeout(PendingTransactions sale, long timeoutMs) {
        sale.cancelTimeoutTask();
        if (sale.isClosed()) {
            return;
        }
        long delayMs = timeoutMs;
        long deadlineMs = sale.getLifecycleDeadlineMs();
        if (deadlineMs > 0) {
            long remaining = deadlineMs - System.currentTimeMillis();
            if (remaining <= 0) {
                sale.onTimeout();
                return;
            }
            delayMs = Math.min(timeoutMs, remaining);
        }
        sale.setTimeoutTask(armTimeout(sale, delayMs)); // new changes
    }

    public static class ArcPendingResult {
        private IngenicoResponse response;
        private boolean timedOut;
        private boolean duplicateInFlight;

        public ArcPendingResult() {
         // Intentionally empty; result fields are initialized by factory methods or by the caller when required.
        }

        public static ArcPendingResult duplicateInFlight() {
            ArcPendingResult r = new ArcPendingResult();
            r.duplicateInFlight = true;
            return r;
        }

        public IngenicoResponse getResponse() {
            return response;
        }

        public boolean isTimedOut() {
            return timedOut;
        }

        public boolean isDuplicateInFlight() {
            return duplicateInFlight;
        }
    }

    public final class PendingTransactions {

        private final ArcPendingRequests owner;
        private final String flowId;
        private final long lifecycleDeadlineMs;
        private final CompletableFuture<ArcPendingResult> waiter = new CompletableFuture<>();
        private final Object lifecycleLock = new Object();

        private boolean closed;
        private ArcFlowState flowState = ArcFlowState.WAITING;

        private IngenicoResponse txnCompletedMsg;
        private ScheduledFuture<?> timeoutTask;
        private WebSocket socket;

        PendingTransactions(ArcPendingRequests owner, String flowId, long maxLifecycleMs) {
            this.owner = owner;
            this.flowId = flowId;
            this.lifecycleDeadlineMs = maxLifecycleMs > 0 ? System.currentTimeMillis() + maxLifecycleMs : 0L;
        }

        long getLifecycleDeadlineMs() {
            return lifecycleDeadlineMs;
        }

        public CompletableFuture<ArcPendingResult> getWaiter() {
            return waiter;
        }

        public void setSocket(WebSocket socket) {
            synchronized (lifecycleLock) {
                this.socket = socket;
            }
        }

        WebSocket getSocket() {
            synchronized (lifecycleLock) {
                return socket;
            }
        }

        void setTimeoutTask(ScheduledFuture<?> nextTask) {
            synchronized (lifecycleLock) {
                cancelTimeoutTaskUnsafe();
                this.timeoutTask = nextTask;
            }
        }

        public void cancelTimeoutTask() {
            synchronized (lifecycleLock) {
                cancelTimeoutTaskUnsafe();
            }
        }

        private void cancelTimeoutTaskUnsafe() {
            if (timeoutTask != null) {
                timeoutTask.cancel(false);
                timeoutTask = null;
            }
        }

        public void onStarted() {
            synchronized (lifecycleLock) {
                if (closed) {
                    return;
                }
                if (flowState == ArcFlowState.WAITING) {
                    flowState = ArcFlowState.STARTED;
                }
            }
        }

        public boolean onTxnCompleted(IngenicoResponse msg) {
            synchronized (lifecycleLock) {
                if (closed) {
                    return false;
                }
                if (flowState == ArcFlowState.TXN_DONE) {
                    return false;
                }
                txnCompletedMsg = msg;
                flowState = ArcFlowState.TXN_DONE;
                return true;
            }
        }

        public boolean onPostTxnReset() {
            synchronized (lifecycleLock) {
                if (closed) {
                    return false;
                }
                if (flowState != ArcFlowState.TXN_DONE) {
                    return false;
                }
                return finishSuccess();
            }
        }

        boolean onTimeout() {
            synchronized (lifecycleLock) {
                if (closed) {
                    return false;
                }
                return finishTimedOut();
            }
        }

        public void onError() {
            synchronized (lifecycleLock) {
                if (closed) {
                    return;
                }
                finishFailed(null);
            }
        }

        public void onArcError(IngenicoResponse msg) {
            synchronized (lifecycleLock) {
                if (closed) {
                    return;
                }
                finishFailed(msg);
            }
        }

        public boolean isClosed() {
            synchronized (lifecycleLock) {
                return closed;
            }
        }

        ArcFlowState getFlowState() {
            synchronized (lifecycleLock) {
                return flowState;
            }
        }

        private boolean finishSuccess() {
            closed = true;
            cancelTimeoutTaskUnsafe();
            owner.drop(flowId, this);
            ArcPendingResult out = new ArcPendingResult();
            out.response = txnCompletedMsg;
            waiter.complete(out);
            return true;
        }

        private boolean finishTimedOut() {
            closed = true;
            cancelTimeoutTaskUnsafe();
            owner.drop(flowId, this);
            ArcPendingResult out = new ArcPendingResult();
            out.timedOut = true;
            waiter.complete(out);
            logger.info("Sale timed out flow_id={}", flowId);
            return true;
        }

        private void finishFailed(IngenicoResponse msg) {
            closed = true;
            cancelTimeoutTaskUnsafe();
            owner.drop(flowId, this);
            ArcPendingResult out = new ArcPendingResult();
            out.response = msg;
            waiter.complete(out);
        }
    }
}
