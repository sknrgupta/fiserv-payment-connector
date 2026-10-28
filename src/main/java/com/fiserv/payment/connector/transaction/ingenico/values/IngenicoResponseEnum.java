package com.fiserv.payment.connector.transaction.ingenico.values;

import java.util.Arrays;

public enum IngenicoResponseEnum {

    HOST_NOT_CONNECTED("15", "91", "Host Not Connected"),
    CARD_READ_TIMEOUT("74", "P5", "User Response Timed Out - Decline"),
    USER_CANCELLED("10", "P3", "User Cancel - Decline"),

    DEFAULT_ERROR_DECLINE("", "06", "Default Failure");

    private String ingenicoProcessorRespCode;

    private String opiRetailRespCode;

    private String responseMessage;

    private IngenicoResponseEnum(String processorCode, String opiResponseCode, String message) {
        this.ingenicoProcessorRespCode = processorCode;
        this.opiRetailRespCode = opiResponseCode;
        this.responseMessage = message;
    }

    public String getIngenicoProcessorRespCode() {
        return ingenicoProcessorRespCode;
    }

    public void setIngenicoProcessorRespCode(String ingProcessorRespcode) {
        this.ingenicoProcessorRespCode = ingProcessorRespcode;
    }

    public String getOpiRetailRespCode() {
        return opiRetailRespCode;
    }

    public void setOpiRetailRespCode(String opiRespCode) {
        this.opiRetailRespCode = opiRespCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public static IngenicoResponseEnum getResponseCode(String responseCode) {
        return Arrays.stream(IngenicoResponseEnum.values())
                .filter(param -> param.getIngenicoProcessorRespCode().equals(responseCode)).findFirst()
                .orElse(DEFAULT_ERROR_DECLINE);
    }
}
