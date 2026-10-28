package com.fiserv.payment.connector.transaction.ingenico.values;

import java.util.Arrays;

public enum HostResponseCode {

    APPOVED("00"),
    UNKNOWN("");

    private String responseCode;

    HostResponseCode(String code) {
        this.responseCode = code;
    }

    public static HostResponseCode getHostResponseTye(String name) {
        return Arrays.stream(HostResponseCode.values()).filter(param -> param.getResponseCode().equalsIgnoreCase(name))
                .findFirst().orElse(UNKNOWN);
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

}
