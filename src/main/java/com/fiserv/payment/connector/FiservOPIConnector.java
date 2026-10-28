package com.fiserv.payment.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Client XML → REST Controller → JAXB DTO → JSON → WebSocket → JSON response → JAXB DTO → XML → Client
 * For your EFTLink/ARC-SI style integration, its recommend keeping 
 * the XML DTOs, ARC-SI JSON DTOs, transformation logic, WebSocket connectivity, and logging concerns separate.
 */
@SpringBootApplication(scanBasePackages = "com.fiserv.payment.connector")
public class FiservOPIConnector {

    private static Logger logger = LoggerFactory.getLogger(FiservOPIConnector.class);

    public static void main(String[] args) {
        SpringApplication.run(FiservOPIConnector.class, args);
        logger.info("STARTED...............................");
    }

}
