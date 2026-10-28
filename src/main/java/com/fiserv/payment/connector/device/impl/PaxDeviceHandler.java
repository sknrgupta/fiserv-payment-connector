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
import com.fiserv.payment.connector.transaction.factory.pax.PaxTransactionHandlerFactory;
import com.fiserv.payment.connector.transaction.type.TransactionType;

/**
 * 
 */
@Component
public class PaxDeviceHandler implements DeviceHandler {

    private static Logger logger = LoggerFactory.getLogger(PaxDeviceHandler.class);

    private final PaxTransactionHandlerFactory paxTransactionHandlerFactory;

    public PaxDeviceHandler(PaxTransactionHandlerFactory paxTransactionHandlerFactory) {
        this.paxTransactionHandlerFactory = paxTransactionHandlerFactory;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.PAX;
    }

    @Override
    public OPITransactionResponse connect(DeviceEntry deviceEntry, TransactionType transactionType,
            OPITransactionRequest opiTransactionRequest) {

        TransactionHandler handler = paxTransactionHandlerFactory.getTransactionHandler(transactionType);

        logger.info("In PaxDeviceHandler : " + getDeviceType().getDeviceType() + " = "
                + opiTransactionRequest.getTransType());

        return handler.process(opiTransactionRequest, deviceEntry);
    }
}