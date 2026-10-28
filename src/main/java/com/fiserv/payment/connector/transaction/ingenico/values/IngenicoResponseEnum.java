package com.fiserv.payment.connector.transaction.ingenico.values;

import java.util.Arrays;

public enum IngenicoResponseEnum {

    HOST_NOT_CONNECTED("15", "91", "Host Not Connected"),
    CARD_READ_TIMEOUT("74", "P5", "User Response Timed Out - Decline"),
    USER_CANCELLED("10", "P3", "User Cancel - Decline"),

    DEFAULT_ERROR_DECLINE("", "06", "Default Failure");

    private String ingenico_processorResponse_code;

    private String opi_retial_response_code;

    private String responseMessage;

    private IngenicoResponseEnum(String processorCode, String opi_ResponseCode, String message) {

        this.ingenico_processorResponse_code = processorCode;
        this.opi_retial_response_code = opi_ResponseCode;
        this.responseMessage = message;

    }

    public String getIngenico_processorResponse_code() {
        return ingenico_processorResponse_code;
    }

    public void setIngenico_processorResponse_code(String ingenico_processorResponse_code) {
        this.ingenico_processorResponse_code = ingenico_processorResponse_code;
    }

    public String getOpi_retial_response_code() {
        return opi_retial_response_code;
    }

    public void setOpi_retial_response_code(String opi_retial_response_code) {
        this.opi_retial_response_code = opi_retial_response_code;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public static IngenicoResponseEnum getResponseCode(String responseCode) {
        return Arrays.stream(IngenicoResponseEnum.values())
                .filter(param -> param.getIngenico_processorResponse_code().equals(responseCode)).findFirst()
                .orElse(DEFAULT_ERROR_DECLINE);
    }
}
