/**
 * 
 */
package com.fiserv.payment.connector.device.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fiserv.payment.connector.device.DeviceHandler;
import com.fiserv.payment.connector.device.type.DeviceType;

/**
 * Device Factory class Determines the device handler class based on the device
 * type This factory can additionally implement more device handlers in future.
 */
@Component
public class DeviceHandlerFactory {

    private static Logger logger = LoggerFactory.getLogger(DeviceHandlerFactory.class);

    private final Map<DeviceType, DeviceHandler> handlers;

    /**
     * Constructor Mapping the Device Handlers
     * 
     * @param deviceHandlers
     */
    public DeviceHandlerFactory(List<DeviceHandler> deviceHandlers) {

        this.handlers = deviceHandlers.stream()
                .collect(Collectors.toUnmodifiableMap(handler -> handler.getDeviceType(), Function.identity()));
    }

    /**
     * The incoming device type returns back the handler for the device.
     * 
     * @param deviceType
     * @return {@link DeviceHandler}
     */
    public DeviceHandler getDeviceHandler(String deviceType) {

        if (deviceType == null || deviceType.isBlank()) {
            logger.error("Device type cannot be null or empty");
            throw new IllegalArgumentException("Device type cannot be null or empty");
        }

        DeviceHandler handler = handlers.get(DeviceType.fromString(deviceType));

        if (handler == null) {
            logger.error("Unsupported device type: " + deviceType);
            throw new IllegalArgumentException("Unsupported device type: " + deviceType);
        }

        return handler;
    }
}