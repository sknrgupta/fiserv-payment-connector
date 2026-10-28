package com.fiserv.payment.connector.model.eftlink.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "TransactionResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionResponse", propOrder = {

        "sequenceNo", "transType", "respCode", "respText", "transAmount", "rrn", "offlineFlag", "transToken",
        "authCode", "issuerId", "pan", "expiryDate", "entryMode", "alternateTransRef", "terminalId", "printData" })
public class OPITransactionResponse {

    @XmlElement(name = "SequenceNo", required = true)
    private String sequenceNo;

    @XmlElement(name = "TransType", required = true)
    private String transType;

    @XmlElement(name = "RespCode")
    private String respCode;

    @XmlElement(name = "RespText")
    private String respText;

    @XmlElement(name = "TransAmount", required = true)
    private String transAmount;

    @XmlElement(name = "RRN", required = true)
    private String rrn;

    @XmlElement(name = "OfflineFlag")
    private String offlineFlag;

    @XmlElement(name = "TransToken")
    private String transToken;

    @XmlElement(name = "AuthCode", required = true)
    private String authCode;

    @XmlElement(name = "IssuerId", required = true)
    private String issuerId;

    @XmlElement(name = "PAN")
    private String pan;

    @XmlElement(name = "ExpiryDate")
    private String expiryDate;

    @XmlElement(name = "EntryMode", required = true)
    private String entryMode;

    @XmlElement(name = "AlternateTransRef", required = true)
    private String alternateTransRef;

    @XmlElement(name = "TerminalId")
    private String terminalId;

    @XmlElement(name = "PrintData")
    private String printData;

    /**
     * @return the sequenceNo
     */
    public String getSequenceNo() {
        return sequenceNo;
    }

    /**
     * @param sequenceNo the sequenceNo to set
     */
    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    /**
     * @return the transType
     */
    public String getTransType() {
        return transType;
    }

    /**
     * @param transType the transType to set
     */
    public void setTransType(String transType) {
        this.transType = transType;
    }

    /**
     * @return the respCode
     */
    public String getRespCode() {
        return respCode;
    }

    /**
     * @param respCode the respCode to set
     */
    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    /**
     * @return the respText
     */
    public String getRespText() {
        return respText;
    }

    /**
     * @param respText the respText to set
     */
    public void setRespText(String respText) {
        this.respText = respText;
    }

    /**
     * @return the transAmount
     */
    public String getTransAmount() {
        return transAmount;
    }

    /**
     * @param transAmount the transAmount to set
     */
    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    /**
     * @return the RRN
     */
    public String getRRN() {
        return rrn;
    }

    /**
     * @param rrn the RRN to set
     */
    public void setRRN(String rrn) {
        this.rrn = rrn;
    }

    /**
     * @return the offlineFlag
     */
    public String getOfflineFlag() {
        return offlineFlag;
    }

    /**
     * @param offlineFlag the offlineFlag to set
     */
    public void setOfflineFlag(String offlineFlag) {
        this.offlineFlag = offlineFlag;
    }

    /**
     * @return the transToken
     */
    public String getTransToken() {
        return transToken;
    }

    /**
     * @param transToken the transToken to set
     */
    public void setTransToken(String transToken) {
        this.transToken = transToken;
    }

    /**
     * @return the authCode
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * @param authCode the authCode to set
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    /**
     * @return the issuerId
     */
    public String getIssuerId() {
        return issuerId;
    }

    /**
     * @param issuerId the issuerId to set
     */
    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    /**
     * @return the PAN
     */
    public String getPAN() {
        return pan;
    }

    /**
     * @param pan the PAN to set
     */
    public void setPAN(String pan) {
        this.pan = pan;
    }

    /**
     * @return the expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return the entryMode
     */
    public String getEntryMode() {
        return entryMode;
    }

    /**
     * @param entryMode the entryMode to set
     */
    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
    }

    /**
     * @return the alternateTransRef
     */
    public String getAlternateTransRef() {
        return alternateTransRef;
    }

    /**
     * @param alternateTransRef the alternateTransRef to set
     */
    public void setAlternateTransRef(String alternateTransRef) {
        this.alternateTransRef = alternateTransRef;
    }

    /**
     * @return the terminalId
     */
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * @param terminalId the terminalId to set
     */
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    /**
     * @return the printData
     */
    public String getPrintData() {
        return printData;
    }

    /**
     * @param printData the printData to set
     */
    public void setPrintData(String printData) {
        this.printData = printData;
    }
}
