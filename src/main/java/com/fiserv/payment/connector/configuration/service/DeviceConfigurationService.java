/**
 * 
 */
package com.fiserv.payment.connector.configuration.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties;
import com.fiserv.payment.connector.configuration.properties.DeviceConfigurationProperties.DeviceEntry;

/**
 * 
 */
@Service
public class DeviceConfigurationService {

    private final DeviceConfigurationProperties properties;

    public DeviceConfigurationService(DeviceConfigurationProperties properties) {
        this.properties = properties;
    }

    public DeviceEntry getDeviceByPort(int port) {

        return properties.getAdditionalHttpsPorts().stream().filter(device -> device.getPort() == port).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Device configuration not found for port: " + port));
    }

    public List<DeviceEntry> getDevicesByType(String deviceType) {

        return properties.getAdditionalHttpsPorts().stream()
                .filter(device -> deviceType.equalsIgnoreCase(device.getDeviceType())).toList();
    }

    public List<DeviceEntry> getAllDevices() {
        return List.copyOf(properties.getAdditionalHttpsPorts());
    }
}