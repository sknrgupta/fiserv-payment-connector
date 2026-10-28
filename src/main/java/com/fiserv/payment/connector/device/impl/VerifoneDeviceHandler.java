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
import com.fiserv.payment.connector.transaction.factory.verifone.VerifoneTransactionHandlerFactory;
import com.fiserv.payment.connector.transaction.type.TransactionType;

/**
 * 
 */
@Component
public class VerifoneDeviceHandler implements DeviceHandler {

    private static Logger logger = LoggerFactory.getLogger(VerifoneDeviceHandler.class);

    private final VerifoneTransactionHandlerFactory verifoneTransactionHandlerFactory;

    public VerifoneDeviceHandler(VerifoneTransactionHandlerFactory verifoneTransactionHandlerFactory) {
        this.verifoneTransactionHandlerFactory = verifoneTransactionHandlerFactory;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.VERIFONE;
    }

    @Override
    public OPITransactionResponse connect(DeviceEntry deviceEntry, TransactionType transactionType,
            OPITransactionRequest opiTransactionRequest) {

        TransactionHandler handler = verifoneTransactionHandlerFactory.getTransactionHandler(transactionType);

        logger.info("In VerifoneDeviceHandler : " + getDeviceType().getDeviceType() + " = "
                + opiTransactionRequest.getTransType());

        return handler.process(opiTransactionRequest, deviceEntry);
    }
}