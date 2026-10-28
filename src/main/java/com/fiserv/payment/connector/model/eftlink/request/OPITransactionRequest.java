package com.fiserv.payment.connector.model.eftlink.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Request Object Here XmlType(propOrder=...) controls the exact XML element
 * order. required=true causes JAXB schema metadata to treat the field as
 * required. For runtime REST validation, I would additionally use Jakarta Bean
 * Validation such as @NotBlank and @NotNull.
 */
@XmlRootElement(name = "TransactionRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionRequest", propOrder = {

        "sequenceNo", "transType", "transDateTime", "siteId", "wsNo", "proxyInfo", "posInfo", "industryCode",
        "operator", "guestNo", "transAmount", "transCurrency", "cardPresent","originalRRN", "partialAuthFlag"

})
public class OPITransactionRequest {

    @XmlElement(name = "SequenceNo", required = true)
    protected String sequenceNo;

    @XmlElement(name = "TransType", required = true)
    protected String transType;

    @XmlElement(name = "TransDateTime", required = true)
    protected String transDateTime;

    @XmlElement(name = "SiteId", required = true)
    protected String siteId;

    @XmlElement(name = "WSNo", required = true)
    protected String wsNo;

    @XmlElement(name = "ProxyInfo", required = true)
    protected String proxyInfo;

    @XmlElement(name = "POSInfo", required = true)
    protected String posInfo;

    @XmlElement(name = "IndustryCode")
    protected String industryCode;

    @XmlElement(name = "Operator")
    protected String operator;

    @XmlElement(name = "GuestNo")
    protected String guestNo;

    @XmlElement(name = "TransAmount")
    protected String transAmount;

    @XmlElement(name = "TransCurrency")
    protected String transCurrency;

    public String getOriginalRRN() {
		return originalRRN;
	}

	public void setOriginalRRN(String originalRRN) {
		this.originalRRN = originalRRN;
	}

	@XmlElement(name = "CardPresent")
    protected String cardPresent;
    
    @XmlElement(name = "OriginalRRN")
    protected String originalRRN;

    @XmlElement(name = "PartialAuthFlag")
    protected String partialAuthFlag;

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
     * @return the transDateTime
     */
    public String getTransDateTime() {
        return transDateTime;
    }

    /**
     * @param transDateTime the transDateTime to set
     */
    public void setTransDateTime(String transDateTime) {
        this.transDateTime = transDateTime;
    }

    /**
     * @return the siteId
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the wsNo
     */
    public String getWsNo() {
        return wsNo;
    }

    /**
     * @param wsNo the wsNo to set
     */
    public void setWsNo(String wsNo) {
        this.wsNo = wsNo;
    }

    /**
     * @return the proxyInfo
     */
    public String getProxyInfo() {
        return proxyInfo;
    }

    /**
     * @param proxyInfo the proxyInfo to set
     */
    public void setProxyInfo(String proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    /**
     * @return the posInfo
     */
    public String getPosInfo() {
        return posInfo;
    }

    /**
     * @param posInfo the posInfo to set
     */
    public void setPosInfo(String posInfo) {
        this.posInfo = posInfo;
    }

    /**
     * @return the industryCode
     */
    public String getIndustryCode() {
        return industryCode;
    }

    /**
     * @param industryCode the industryCode to set
     */
    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    /**
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * @return the guestNo
     */
    public String getGuestNo() {
        return guestNo;
    }

    /**
     * @param guestNo the guestNo to set
     */
    public void setGuestNo(String guestNo) {
        this.guestNo = guestNo;
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
     * @return the transCurrency
     */
    public String getTransCurrency() {
        return transCurrency;
    }

    /**
     * @param transCurrency the transCurrency to set
     */
    public void setTransCurrency(String transCurrency) {
        this.transCurrency = transCurrency;
    }

    /**
     * @return the cardPresent
     */
    public String getCardPresent() {
        return cardPresent;
    }

    /**
     * @param cardPresent the cardPresent to set
     */
    public void setCardPresent(String cardPresent) {
        this.cardPresent = cardPresent;
    }

    /**
     * @return the partialAuthFlag
     */
    public String getPartialAuthFlag() {
        return partialAuthFlag;
    }

    /**
     * @param partialAuthFlag the partialAuthFlag to set
     */
    public void setPartialAuthFlag(String partialAuthFlag) {
        this.partialAuthFlag = partialAuthFlag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SequenceNo : " + getSequenceNo()).append("\n");
        sb.append("TransType : " + getTransType()).append("\n");
        sb.append("WSNo : " + getWsNo()).append("\n");
        sb.append("TransAmount : " + getTransAmount());
        return sb.toString();
    }

}
