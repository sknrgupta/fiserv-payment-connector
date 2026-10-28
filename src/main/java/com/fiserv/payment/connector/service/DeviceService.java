/**
 * 
 */
package com.fiserv.payment.connector.service;

import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;
import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;

/**
 * 
 */
public interface DeviceService {
    public OPITransactionResponse processDeviceRequest(DeviceEntry deviceEntry,
            OPITransactionRequest opiTransactionRequest);
}
