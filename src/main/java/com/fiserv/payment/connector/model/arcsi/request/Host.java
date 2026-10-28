package com.fiserv.payment.connector.model.arcsi.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Host {

    private String approval_code;
    private String response_text;
    private String customer_text;
    private String response_code;
    private String authorized_amount;
    private String auth_network_id;
    private String auth_network_name;
    private String reference_number;
    private String avs_result;
    private String txn_id;
    private String auth_characteristics_indicator;
    private String card_level_result;

    public String getApproval_code() {
        return approval_code;
    }

    public void setApproval_code(String approval_code) {
        this.approval_code = approval_code;
    }

    public String getResponse_text() {
        return response_text;
    }

    public void setResponse_text(String response_text) {
        this.response_text = response_text;
    }

    public String getCustomer_text() {
        return customer_text;
    }

    public void setCustomer_text(String customer_text) {
        this.customer_text = customer_text;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getAuthorized_amount() {
        return authorized_amount;
    }

    public void setAuthorized_amount(String authorized_amount) {
        this.authorized_amount = authorized_amount;
    }

    public String getAuth_network_id() {
        return auth_network_id;
    }

    public void setAuth_network_id(String auth_network_id) {
        this.auth_network_id = auth_network_id;
    }

    public String getAuth_network_name() {
        return auth_network_name;
    }

    public void setAuth_network_name(String auth_network_name) {
        this.auth_network_name = auth_network_name;
    }

    public String getReference_number() {
        return reference_number;
    }

    public void setReference_number(String reference_number) {
        this.reference_number = reference_number;
    }

    public String getAvs_result() {
        return avs_result;
    }

    public void setAvs_result(String avs_result) {
        this.avs_result = avs_result;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getAuth_characteristics_indicator() {
        return auth_characteristics_indicator;
    }

    public void setAuth_characteristics_indicator(String auth_characteristics_indicator) {
        this.auth_characteristics_indicator = auth_characteristics_indicator;
    }

    public String getCard_level_result() {
        return card_level_result;
    }

    public void setCard_level_result(String card_level_result) {
        this.card_level_result = card_level_result;
    }

}
