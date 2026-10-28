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
@ConfigurationProperties(prefix = "server.ssl")
public class SSLConfigurationProperties {

    private boolean enabled;
    private String keyStore;
    private String keyStorePassword;
    private String keyPassword;
    private String keyStoreType;
    private String keyAlias;
    private List<String> enabledProtocols = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    public List<String> getEnabledProtocols() {
        return enabledProtocols;
    }

    public void setEnabledProtocols(List<String> enabledProtocols) {
        this.enabledProtocols = enabledProtocols;
    }
}
