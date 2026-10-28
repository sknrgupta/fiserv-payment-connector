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
 * Interface Device handler As of phase I Ingenico device is being used. This
 * Handler with factory pattern can implement more device additions in future.
 * 
 * @see for details in IngenicoDeviceHandler...
 */
public interface DeviceHandler {

    /**
     * Determined device type.
     * 
     * @return {@link DeviceType}
     */
    DeviceType getDeviceType();

    /**
     * Forwarding requests to the appropriate device handler and transaction
     * handler.
     * 
     * @param deviceEntry
     * @param transactionType
     * @param opiTransactionRequest
     * @return {@link OPITransactionResponse}
     */
    OPITransactionResponse connect(DeviceEntry deviceEntry, TransactionType transactionType,
            OPITransactionRequest opiTransactionRequest);
}