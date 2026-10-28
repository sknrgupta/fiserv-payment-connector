package com.fiserv.payment.connector.model.arcsi.request;

public class Token {

    private String card_mnemonic;
    private String exp;
    private String value;

    public String getCard_mnemonic() {
        return card_mnemonic;
    }

    public void setCard_mnemonic(String card_mnemonic) {
        this.card_mnemonic = card_mnemonic;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
