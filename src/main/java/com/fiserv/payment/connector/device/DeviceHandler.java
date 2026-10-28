/**
 * 
 */
package com.fiserv.payment.connector.device;

import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;
import com.fiserv.payment.connector.device.type.DeviceType;
import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;
import com.fiserv.payment.connector.transaction.type.TransactionType;

/**
 * 
 */
public interface DeviceHandler {

    DeviceType getDeviceType();

    OPITransactionResponse connect(DeviceEntry deviceEntry, TransactionType transactionType,
            OPITransactionRequest opiTransactionRequest);
}