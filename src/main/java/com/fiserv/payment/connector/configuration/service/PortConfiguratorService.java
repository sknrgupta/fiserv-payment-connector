/**
 * 
 */
package com.fiserv.payment.connector.configuration.service;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import com.fiserv.payment.connector.configuration.properties.PortConfigurationProperties;
import com.fiserv.payment.connector.configuration.properties.SSLConfigurationProperties;

/**
 * 
 */
@Component
public class PortConfiguratorService implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    private final PortConfigurationProperties portProperties;
    private final SSLConfigurationProperties sslProperties;

    public PortConfiguratorService(PortConfigurationProperties portProperties,
            SSLConfigurationProperties sslProperties) {

        this.portProperties = portProperties;
        this.sslProperties = sslProperties;
    }

    @Override
    public void customize(TomcatServletWebServerFactory factory) {

        if (!sslProperties.isEnabled()) {
            throw new IllegalStateException("SSL must be enabled for additional HTTPS ports");
        }

        portProperties.getAdditionalHttpsPorts()
                .forEach(entry -> factory.addAdditionalTomcatConnectors(createHttpsConnector(entry.getPort())));
    }

    private Connector createHttpsConnector(int port) {

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");

        connector.setPort(port);
        connector.setScheme("https");
        connector.setSecure(true);

        connector.setProperty("SSLEnabled", "true");

        SSLHostConfig sslHostConfig = new SSLHostConfig();

        sslHostConfig.setProtocols(String.join(",", sslProperties.getEnabledProtocols()));

        SSLHostConfigCertificate certificate = new SSLHostConfigCertificate(sslHostConfig,
                SSLHostConfigCertificate.Type.RSA);

        certificate.setCertificateKeystoreFile(sslProperties.getKeyStore());

        certificate.setCertificateKeystorePassword(sslProperties.getKeyStorePassword());

        certificate.setCertificateKeyPassword(sslProperties.getKeyPassword());

        certificate.setCertificateKeystoreType(sslProperties.getKeyStoreType());

        certificate.setCertificateKeyAlias(sslProperties.getKeyAlias());

        sslHostConfig.addCertificate(certificate);
        connector.addSslHostConfig(sslHostConfig);

        return connector;
    }
}