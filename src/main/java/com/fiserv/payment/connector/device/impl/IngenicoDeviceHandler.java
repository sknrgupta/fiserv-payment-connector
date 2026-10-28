/**
 * 
 */
package com.fiserv.payment.connector.device.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;
import com.fiserv.payment.connector.device.DeviceHandler;
import com.fiserv.payment.connector.device.type.DeviceType;
import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;
import com.fiserv.payment.connector.transaction.TransactionHandler;
import com.fiserv.payment.connector.transaction.factory.ingenico.IngenicoTransactionHandlerFactory;
import com.fiserv.payment.connector.transaction.type.TransactionType;

/**
 * Component Device Handler. This will determine the transaction handlers to
 * whihc the request is forwarded.
 */
@Component
public class IngenicoDeviceHandler implements DeviceHandler {

    private static final Logger logger = LoggerFactory.getLogger(IngenicoDeviceHandler.class);

    private final IngenicoTransactionHandlerFactory ingenicoTransactionHandlerFactory;

    /**
     * Constructor
     * 
     * @param ingenicoTransactionHandlerFactory
     */
    public IngenicoDeviceHandler(IngenicoTransactionHandlerFactory ingenicoTransactionHandlerFactory) {
        this.ingenicoTransactionHandlerFactory = ingenicoTransactionHandlerFactory;
    }

    /**
     * Marking device type for which this handler is for.
     * {@see DeviceHandlerFactory} checks this.
     */
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.INGENICO;
    }

    /**
     * Ingenico Handler Factory determines appropriate transaction handler for the
     * incoming request transaction type to forward to.
     * 
     * @param deviceEntry
     * @param transactionType
     * @param opiTransactionRequest
     * @return {@link OPITransactionResponse}
     */
    @Override
    public OPITransactionResponse connect(DeviceEntry deviceEntry, TransactionType transactionType,
            OPITransactionRequest opiTransactionRequest) {

        TransactionHandler handler = ingenicoTransactionHandlerFactory.getTransactionHandler(transactionType);

        logger.debug("In IngenicoDeviceHandler : " + getDeviceType().getDeviceType() + " - "
                + opiTransactionRequest.getTransType());

        return handler.process(opiTransactionRequest, deviceEntry);
    }
}
