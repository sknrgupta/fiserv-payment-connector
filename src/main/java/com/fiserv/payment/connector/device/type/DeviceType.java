/**
 * 
 */
package com.fiserv.payment.connector.device.type;

/**
 * 
 */
public enum DeviceType {

    // 1. Define constants with values passed to the constructor
    INGENICO("INGENICO"),
    VERIFONE("VERIFONE"),
    PAX("PAX");

    // 2. Define internal state fields
    private final String deviceType;

    // 3. Define the constructor (always private or package-private)
    DeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    // 4. Define getter methods to retrieve the custom values
    public String getDeviceType() {
        return this.deviceType;
    }

    // Custom lookup method
    public static DeviceType fromString(String value) {
        for (DeviceType deviceType : DeviceType.values()) {
            if (deviceType.getDeviceType().equalsIgnoreCase(value)) {
                return deviceType;
            }
        }
        return null; // Or throw an IllegalArgumentException
    }

}
