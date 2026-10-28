package com.fiserv.payment.connector.service.xml.converter;

import java.io.StringReader;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

@Service
public class RequestResponseXMLConverter {

    private static Logger logger = LoggerFactory.getLogger(RequestResponseXMLConverter.class);

    public OPITransactionRequest convertPlainTextRequestToOPITransactionRequestObject(String xmlString) {
        JAXBContext jaxbContext;
        OPITransactionRequest opiTransactionRequest = null;
        try {
            jaxbContext = JAXBContext.newInstance(OPITransactionRequest.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            opiTransactionRequest = (OPITransactionRequest) unmarshaller.unmarshal(new StringReader(xmlString));
        } catch (JAXBException e) {
            logger.error("Error while converting XML String to TransactionRequest Object", e);
        }
        return opiTransactionRequest;
    }

    public String convertTransactionResponseObjectToPlainTextResponse(OPITransactionResponse opiTransactionResponse) {
        JAXBContext jaxbContext;
        String responseString = "";
        if (opiTransactionResponse != null) {
            try {
                jaxbContext = JAXBContext.newInstance(OPITransactionResponse.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(opiTransactionResponse, stringWriter);
                responseString = stringWriter.toString();
            } catch (JAXBException e) {
                logger.error("Error while converting TransactionResponse Object to XML String ", e);
            }
        }
        return responseString;
    }

    public String convertTransactionRequestToPlainTextRequest(OPITransactionRequest opiTransactionRequest) {
        JAXBContext jaxbContext;
        String requestString = "";
        if (opiTransactionRequest != null) {
            try {
                jaxbContext = JAXBContext.newInstance(OPITransactionRequest.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(opiTransactionRequest, stringWriter);
                requestString = stringWriter.toString();
            } catch (JAXBException e) {
                logger.error("Error while TransactionRequest Object to XML String ", e);
            }
        }
        return requestString;
    }

}
