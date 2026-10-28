/**
 * 
 */
package com.fiserv.payment.connector.transaction.verifone;

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
public class VerifoneReversalTransactionHandler implements TransactionHandler {

    private static Logger logger = LoggerFactory.getLogger(VerifoneReversalTransactionHandler.class);

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.VERIFONE;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.REVERSAL;
    }

    @Override
    public OPITransactionResponse process(OPITransactionRequest opiTransactionRequest, DeviceEntry deviceEntry) {

        // 1. Convert EFTLink request -> Verifone Software Reversal request
        // 2. Send request to Verifone Software
        // 3. Receive Verifone Software response
        // 4. Convert Verifone Software response -> EFTLink response

        logger.info("Verifone REVERSAL processed: " + opiTransactionRequest.getTransType());

        return MockResponse.getMockResponse(opiTransactionRequest);
    }
}
