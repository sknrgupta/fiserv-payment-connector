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
 * 
 */
@Component
public class IngenicoDeviceHandler implements DeviceHandler {

    private static final Logger logger = LoggerFactory.getLogger(IngenicoDeviceHandler.class);

    private final IngenicoTransactionHandlerFactory ingenicoTransactionHandlerFactory;

    public IngenicoDeviceHandler(IngenicoTransactionHandlerFactory ingenicoTransactionHandlerFactory) {
        this.ingenicoTransactionHandlerFactory = ingenicoTransactionHandlerFactory;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.INGENICO;
    }

    @Override
    public OPITransactionResponse connect(DeviceEntry deviceEntry, TransactionType transactionType,
            OPITransactionRequest opiTransactionRequest) {

        TransactionHandler handler = ingenicoTransactionHandlerFactory.getTransactionHandler(transactionType);

        logger.info("In IngenicoDeviceHandler : " + getDeviceType().getDeviceType() + " = "
                + opiTransactionRequest.getTransType());

        return handler.process(opiTransactionRequest, deviceEntry);
    }
}
