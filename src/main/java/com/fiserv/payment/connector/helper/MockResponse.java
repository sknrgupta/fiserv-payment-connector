/**
 * 
 */
package com.fiserv.payment.connector.helper;

import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;

/**
 * 
 */
public class MockResponse {

    public static OPITransactionResponse getMockResponse(OPITransactionRequest opiTransactionRequest) {
        OPITransactionResponse opiTransactionResponse = new OPITransactionResponse();
        opiTransactionResponse.setSequenceNo(opiTransactionRequest.getSequenceNo());
        opiTransactionResponse.setTransType(opiTransactionRequest.getTransType());
        opiTransactionResponse.setRespCode("00");
        opiTransactionResponse.setRespText("Transaction Approved");
        opiTransactionResponse.setTransAmount(opiTransactionRequest.getTransAmount());
        opiTransactionResponse.setRRN("963905");
        opiTransactionResponse.setOfflineFlag("N");
        opiTransactionResponse.setTransToken("9196495########8885");
        opiTransactionResponse.setAuthCode("853431");
        opiTransactionResponse.setIssuerId("01");
        opiTransactionResponse.setPAN("XXXXXXXXXXXX8885");
        opiTransactionResponse.setExpiryDate("1127");
        opiTransactionResponse.setEntryMode("23");
        opiTransactionResponse.setAlternateTransRef("020046397fe3b46a406dae4f81e49a717667");
        opiTransactionResponse.setTerminalId("12345678");
        opiTransactionResponse.setPrintData(" WIll UPDATE ONCE WE DISCUSSED WITH INGENICO TEAM ");
        return opiTransactionResponse;
    }
}
