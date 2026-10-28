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
 * Interface Transaction handler As of phase I Sale, Return, Cancel, Void,
 * Inquiry will be implemented. This Handler with factory pattern can implement
 * more transaction additions in future.
 * 
 * @see for details in IngenicoSaleTransactionHandler...
 */
public interface TransactionHandler {

    /**
     * Determined device type at the handler level to determine which device
     * transaction is being handled.
     * 
     * @return {@link DeviceType}
     */
    DeviceType getDeviceType();

    /**
     * Determine the transaction type the implementation will handle
     * 
     * @return {@link TransactionType}
     */
    TransactionType getTransactionType();

    /**
     * Forwarding requests to the appropriate transaction handler of this device.
     * 
     * @param opiTransactionRequest
     * @param deviceEntry
     * @return {@link OPITransactionResponse}
     */
    OPITransactionResponse process(OPITransactionRequest opiTransactionRequest, DeviceEntry deviceEntry);
}
