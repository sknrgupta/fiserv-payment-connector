/**
 * 
 */
package com.fiserv.payment.connector.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;
import com.fiserv.payment.connector.device.DeviceHandler;
import com.fiserv.payment.connector.device.factory.DeviceHandlerFactory;
import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;
import com.fiserv.payment.connector.service.DeviceService;
import com.fiserv.payment.connector.transaction.type.TransactionType;

/**
 * 
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceHandlerFactory deviceHandlerFactory;

    public DeviceServiceImpl(DeviceHandlerFactory deviceHandlerFactory) {
        this.deviceHandlerFactory = deviceHandlerFactory;
    }

    @Override
    public OPITransactionResponse processDeviceRequest(DeviceEntry deviceEntry,
            OPITransactionRequest opiTransactionRequest) {

        DeviceHandler deviceHandler = deviceHandlerFactory.getDeviceHandler(deviceEntry.getDeviceType());

        TransactionType transactionType = TransactionType.fromString(opiTransactionRequest.getTransType());

        logger.info(
                "In DeviceServiceImpl : " + deviceEntry.getDeviceType() + " = " + opiTransactionRequest.getTransType());

        return deviceHandler.connect(deviceEntry, transactionType, opiTransactionRequest);
    }

}
