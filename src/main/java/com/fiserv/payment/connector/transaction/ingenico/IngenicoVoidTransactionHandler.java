/**
 * 
 */
package com.fiserv.payment.connector.transaction.ingenico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;
import com.fiserv.payment.connector.device.type.DeviceType;
import com.fiserv.payment.connector.helper.MockResponse;
import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;
import com.fiserv.payment.connector.transaction.TransactionHandler;
import com.fiserv.payment.connector.transaction.type.TransactionType;

/**
 * 
 */
@Component
public class IngenicoVoidTransactionHandler implements TransactionHandler {

    private static Logger logger = LoggerFactory.getLogger(IngenicoVoidTransactionHandler.class);

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.INGENICO;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.VOID;
    }

    @Override
    public OPITransactionResponse process(OPITransactionRequest opiTransactionRequest, DeviceEntry deviceEntry) {

        // 1. Convert EFTLink request -> ARC-SI Void request
        // 2. Send request to ARC-SI
        // 3. Receive ARC-SI response
        // 4. Convert ARC-SI response -> EFTLink response

        logger.info("Ingenico VOID processed: " + opiTransactionRequest.getTransType());

        return MockResponse.getMockResponse(opiTransactionRequest);
    }
}
