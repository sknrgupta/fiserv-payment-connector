/**
 * 
 */
package com.fiserv.payment.connector.transaction;

import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;
import com.fiserv.payment.connector.device.type.DeviceType;
import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;
import com.fiserv.payment.connector.transaction.type.TransactionType;

/**
 * 
 */
public interface TransactionHandler {

    DeviceType getDeviceType();

    TransactionType getTransactionType();

    OPITransactionResponse process(OPITransactionRequest opiTransactionRequest, DeviceEntry deviceEntry);
}
