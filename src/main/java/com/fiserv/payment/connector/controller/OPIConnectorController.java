/**
 * 
 */
package com.fiserv.payment.connector.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiserv.payment.connector.log.PortLoggerFactory;
import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;
import com.fiserv.payment.connector.service.OPITransactionService;
import com.fiserv.payment.connector.service.xml.converter.RequestResponseXMLConverter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Spring/Jackson/JAXB infrastructure can handle the HTTP XML serialization
 * boundary, so you don't need to manually manipulate XML strings in the
 * controller. But the EFTLink sends request though XML is still
 * application/text raw string format. This main controller handles post request
 * both xml and text incoming requests
 * 
 * The overall design Client XML → REST Controller → JAXB DTO → JSON → WebSocket
 * → JSON response → JAXB DTO → XML → Client The XML DTOs, ARC-SI JSON DTOs,
 * transformation logic, WebSocket connectivity, and logging concerns separate.
 */
@RestController
@RequestMapping("/")
public class OPIConnectorController {

    private final RequestResponseXMLConverter requestResponseXMLConverter;
    private final OPITransactionService opiTransactionService;

    /**
     * Constructor
     * 
     * @param requestResponseXMLConverter
     * @param opiTransactionService
     */
    public OPIConnectorController(RequestResponseXMLConverter requestResponseXMLConverter,
            OPITransactionService opiTransactionService) {
        this.requestResponseXMLConverter = requestResponseXMLConverter;
        this.opiTransactionService = opiTransactionService;
    }

    /**
     * Incoming request raw text (as in the case of EFTLink
     * 
     * @param httpServletRequest
     * @param xmlRequest
     * @return {@link ResponseEntity<String>}
     */
    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> handleRequest(HttpServletRequest httpServletRequest,
            @Valid @RequestBody String xmlRequest) {
        String portStr = Integer.toString(httpServletRequest.getLocalPort());
        PortLoggerFactory.LoggersContainer loggers = PortLoggerFactory.getLoggersForPort(portStr);
        loggers.reqResLogger.info("String : " + xmlRequest);
        OPITransactionRequest opiTransactionRequest = requestResponseXMLConverter
                .convertPlainTextRequestToOPITransactionRequestObject(xmlRequest);
        OPITransactionResponse opiTransactionResponse = opiTransactionService.processRequest(opiTransactionRequest,
                portStr);
        String plainTextResponse = requestResponseXMLConverter
                .convertTransactionResponseObjectToPlainTextResponse(opiTransactionResponse);
        loggers.reqResLogger.debug("Outgoing Plain Text Response Sent: {}", plainTextResponse);
        return ResponseEntity.ok(plainTextResponse);
    }

    /**
     * Incoming request as XML e.g Symphony
     * 
     * @param httpServletRequest
     * @param opiTransactionRequest
     * @return {@link ResponseEntity<OPITransactionResponse>}
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<OPITransactionResponse> handleRequest(HttpServletRequest httpServletRequest,
            @Valid @RequestBody OPITransactionRequest opiTransactionRequest) {
        String portStr = Integer.toString(httpServletRequest.getLocalPort());
        PortLoggerFactory.LoggersContainer loggers = PortLoggerFactory.getLoggersForPort(portStr);
        loggers.reqResLogger.info("OPITransactionRequest : " + opiTransactionRequest.toString());
        OPITransactionResponse opiTransactionResponse = opiTransactionService.processRequest(opiTransactionRequest,
                portStr);
        loggers.reqResLogger.debug("Outgoing XML Response Object Sent: {}", opiTransactionResponse.toString());
        return ResponseEntity.ok(opiTransactionResponse);
    }
}