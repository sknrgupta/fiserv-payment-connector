/**
 * 
 */
package com.fiserv.payment.connector.transaction.factory.verifone;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fiserv.payment.connector.device.type.DeviceType;
import com.fiserv.payment.connector.transaction.TransactionHandler;
import com.fiserv.payment.connector.transaction.factory.TransactionHandlerFactory;
import com.fiserv.payment.connector.transaction.type.TransactionType;

/**
 * 
 */
@Component
public class VerifoneTransactionHandlerFactory implements TransactionHandlerFactory {

    private static Logger logger = LoggerFactory.getLogger(VerifoneTransactionHandlerFactory.class);

    private final Map<TransactionType, TransactionHandler> handlers;

    public VerifoneTransactionHandlerFactory(List<TransactionHandler> transactionHandlers) {

        Map<TransactionType, TransactionHandler> map = new EnumMap<>(TransactionType.class);

        transactionHandlers.stream().filter(handler -> handler.getDeviceType().compareTo(DeviceType.VERIFONE) == 0)
                .forEach(handler -> map.put(handler.getTransactionType(), handler));

        this.handlers = Map.copyOf(map);
    }

    @Override
    public TransactionHandler getTransactionHandler(TransactionType transactionType) {

        logger.info(handlers.keySet().toString());

        TransactionHandler handler = handlers.get(transactionType);

        if (handler == null) {
            throw new IllegalArgumentException("Unsupported Terifone transaction: " + transactionType);
        }

        return handler;
    }
}
