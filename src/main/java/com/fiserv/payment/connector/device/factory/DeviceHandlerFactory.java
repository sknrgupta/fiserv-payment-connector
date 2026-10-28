/**
 * 
 */
package com.fiserv.payment.connector.device.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fiserv.payment.connector.device.DeviceHandler;
import com.fiserv.payment.connector.device.type.DeviceType;

/**
 * 
 */
@Component
public class DeviceHandlerFactory {

    private final Map<DeviceType, DeviceHandler> handlers;

    public DeviceHandlerFactory(List<DeviceHandler> handlers) {

        this.handlers = handlers.stream()
                .collect(Collectors.toUnmodifiableMap(handler -> handler.getDeviceType(), Function.identity()));
    }

    public DeviceHandler getDeviceHandler(String deviceType) {

        if (deviceType == null || deviceType.isBlank()) {
            throw new IllegalArgumentException("Device type cannot be null or empty");
        }

        DeviceHandler handler = handlers.get(DeviceType.fromString(deviceType));

        if (handler == null) {
            throw new IllegalArgumentException("Unsupported device type: " + deviceType);
        }

        return handler;
    }
}