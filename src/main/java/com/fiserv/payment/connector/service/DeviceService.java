/**
 * 
 */
package com.fiserv.payment.connector.service;

import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;
import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;

/**
 * Interface Device service processing incoming requests by port and forwarding
 * to request object to appropriate device handler
 */
public interface DeviceService {
    public OPITransactionResponse processDeviceRequest(DeviceEntry deviceEntry,
            OPITransactionRequest opiTransactionRequest);
}
