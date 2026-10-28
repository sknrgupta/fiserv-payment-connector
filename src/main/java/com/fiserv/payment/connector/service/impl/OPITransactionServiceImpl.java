/**
 * 
 */
package com.fiserv.payment.connector.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiserv.payment.connector.FiservOPIConnector;
import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;
import com.fiserv.payment.connector.configuration.service.DeviceConfigurationService;
import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;
import com.fiserv.payment.connector.service.DeviceService;
import com.fiserv.payment.connector.service.OPITransactionService;

/**
 * 
 */
@Service
public class OPITransactionServiceImpl implements OPITransactionService {

    private static Logger logger = LoggerFactory.getLogger(FiservOPIConnector.class);

    @Autowired
    DeviceConfigurationService deviceConfigurationService;

    private final DeviceService deviceService;

    public OPITransactionServiceImpl(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public OPITransactionResponse processRequest(OPITransactionRequest opiTransactionRequest, String port) {

        for (DeviceEntry deviceEntry : deviceConfigurationService.getAllDevices()) {
            logger.info(deviceEntry.getPort() + " - " + deviceEntry.getDeviceType() + " - " + deviceEntry.getTimeoutMs()
                    + " - " + deviceEntry.getWebsocketUrl() + " - " + " - " + deviceEntry.getEndPointUrl());
        }

        logger.info("\n\n\n");
        DeviceEntry deviceEntry = deviceConfigurationService.getDeviceByPort(Integer.valueOf(port));
        logger.info(deviceEntry.getPort() + " - " + deviceEntry.getDeviceType() + " - " + deviceEntry.getTimeoutMs()
                + " - " + deviceEntry.getWebsocketUrl() + " - " + deviceEntry.getEndPointUrl());

        OPITransactionResponse opiTransactionResponse = deviceService.processDeviceRequest(deviceEntry,
                opiTransactionRequest);

        return opiTransactionResponse;
    }

}
