package com.fiserv.payment.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot Application. This class triggers the
 * framework initialization, component scanning, and auto-configuration
 * procedures.
 */
@SpringBootApplication(scanBasePackages = "com.fiserv.payment.connector")
// @ComponentScan: Tells Spring to look for other components, configurations, and services in this package.
public class FiservOPIConnector {

    private static Logger logger = LoggerFactory.getLogger(FiservOPIConnector.class);

    /**
     * Main method: The standard entry point for a Java Application.
     * 
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {

        // Bootstraps the application, starting the embedded web server (e.g., Tomcat)
        // and initializing the Spring ApplicationContext container.
        SpringApplication.run(FiservOPIConnector.class, args);
        logger.info("STARTED...............................");
    }

}
