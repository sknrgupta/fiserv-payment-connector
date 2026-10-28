/**
 * 
 */
package com.fiserv.payment.connector.configuration.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
@ConfigurationProperties(prefix = "connector.server")
public class DeviceConfigurationProperties {

    private List<DeviceEntry> additionalHttpsPorts = new ArrayList<>();

    public List<DeviceEntry> getAdditionalHttpsPorts() {
        return additionalHttpsPorts;
    }

    public void setAdditionalHttpsPorts(List<DeviceEntry> additionalHttpsPorts) {
        this.additionalHttpsPorts = additionalHttpsPorts;
    }

    public static class DeviceEntry {

        private int port;
        private String deviceType;
        private int timeoutMs;
        private long maxSaleLifecycleMs;
        private String websocketUrl;
        private String endPointUrl;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public int getTimeoutMs() {
            return timeoutMs;
        }

        public void setTimeoutMs(int timeoutMs) {
            this.timeoutMs = timeoutMs;
        }

        public long getMaxSaleLifecycleMs() {
            return maxSaleLifecycleMs;
        }

        public void setMaxSaleLifecycleMs(long maxSaleLifecycleMs) {
            this.maxSaleLifecycleMs = maxSaleLifecycleMs;
        }

        public String getWebsocketUrl() {
            return websocketUrl;
        }

        public void setWebsocketUrl(String websocketUrl) {
            this.websocketUrl = websocketUrl;
        }

        public String getEndPointUrl() {
            return endPointUrl;
        }

        public void setEndPointUrl(String endPointUrl) {
            this.endPointUrl = endPointUrl;
        }
    }

}
