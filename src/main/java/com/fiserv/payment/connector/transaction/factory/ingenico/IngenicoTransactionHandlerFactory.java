/**
 * 
 */
package com.fiserv.payment.connector.transaction.factory.ingenico;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fiserv.payment.connector.device.DeviceHandler;
import com.fiserv.payment.connector.device.type.DeviceType;
import com.fiserv.payment.connector.transaction.TransactionHandler;
import com.fiserv.payment.connector.transaction.factory.TransactionHandlerFactory;
import com.fiserv.payment.connector.transaction.type.TransactionType;

/**
 * Ingenico Transaction Factory class Determines the transaction handler class
 * based on the transaction type This factory can additionally implement more
 * handlers in future.
 */
@Component
public class IngenicoTransactionHandlerFactory implements TransactionHandlerFactory {

    private static Logger logger = LoggerFactory.getLogger(IngenicoTransactionHandlerFactory.class);

    private final Map<TransactionType, TransactionHandler> handlers;

    /**
     * Constructor Mapping the Transaction Handler for Ingenico
     * 
     * @param transactionHandlers
     */
    public IngenicoTransactionHandlerFactory(List<TransactionHandler> transactionHandlers) {

        Map<TransactionType, TransactionHandler> map = new EnumMap<>(TransactionType.class);

        transactionHandlers.stream().filter(handler -> handler.getDeviceType().compareTo(DeviceType.INGENICO) == 0)
                .forEach(handler -> map.put(handler.getTransactionType(), handler));

        this.handlers = Map.copyOf(map);
    }

    /**
     * The incoming transaction type returns back the handler for the specified
     * transaction.
     * 
     * @param deviceType
     * @return {@link DeviceHandler}
     */
    @Override
    public TransactionHandler getTransactionHandler(TransactionType transactionType) {

        if (transactionType == null) {
            logger.error("Transaction type cannot be null");
            throw new IllegalArgumentException("Transaction type cannot be null");
        }

        TransactionHandler handler = handlers.get(transactionType);

        if (handler == null) {
            logger.error("Unsupported Ingenico transaction: " + transactionType);
            throw new IllegalArgumentException("Unsupported Engenico transaction: " + transactionType);
        }

        return handler;
    }
}
