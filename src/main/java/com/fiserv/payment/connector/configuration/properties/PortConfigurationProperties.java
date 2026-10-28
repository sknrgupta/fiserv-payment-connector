/**
 * 
 */
package com.fiserv.payment.connector.configuration.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "connector.server")
public class PortConfigurationProperties {

    private List<PortEntry> additionalHttpsPorts = List.of();

    public List<PortEntry> getAdditionalHttpsPorts() {
        return additionalHttpsPorts;
    }

    public void setAdditionalHttpsPorts(List<PortEntry> additionalHttpsPorts) {
        this.additionalHttpsPorts = additionalHttpsPorts;
    }

    public static class PortEntry {

        private int port;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}