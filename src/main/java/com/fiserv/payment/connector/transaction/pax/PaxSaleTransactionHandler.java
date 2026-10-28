/**
 * 
 */
package com.fiserv.payment.connector.transaction.pax;

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
public class PaxSaleTransactionHandler implements TransactionHandler {

    private static Logger logger = LoggerFactory.getLogger(PaxSaleTransactionHandler.class);

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.PAX;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.SALE;
    }

    @Override
    public OPITransactionResponse process(OPITransactionRequest opiTransactionRequest, DeviceEntry deviceEntry) {

        // 1. Convert EFTLink request -> Pax Software Sale request
        // 2. Send request to Pax Software
        // 3. Receive Pax Software response
        // 4. Convert Pax Software response -> EFTLink response

        logger.info("Pax SALE processed: " + opiTransactionRequest.getTransType());

        return MockResponse.getMockResponse(opiTransactionRequest);
    }
}
