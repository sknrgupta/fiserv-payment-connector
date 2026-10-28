package com.fiserv.payment.connector.model.arcsi.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiserv.payment.connector.model.arcsi.response.Card;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Resource {

    private Amount amount;
    private Boolean confirm_amount;
    private Boolean display_txn_result;
    private int language;
    private Boolean manual_entry;
    private Boolean partial_approval;

    @JsonProperty("payment_type")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> paymentType;

    private Integer qsr_limit;
    private List<String> readers;
    private Boolean return_to_idle;
    private Boolean token_request;
    private Boolean txn_status_events;
    private String type;

    // response
    @JsonProperty("error_info")
    private ErrorInfo error_info = null;
    private Card card;
    private String client_txn_id;
    private String date;
    private Host host;
    private String source;
    private String status;
    private String time;
    private String txn_type;
    private Token token;
    
  //Add Orginal class
    private Original original;
    
    public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Boolean getConfirm_amount() {
        return confirm_amount;
    }

    public void setConfirm_amount(Boolean confirm_amount) {
        this.confirm_amount = confirm_amount;
    }

    public Boolean getDisplay_txn_result() {
        return display_txn_result;
    }

    public void setDisplay_txn_result(Boolean display_txn_result) {
        this.display_txn_result = display_txn_result;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public Boolean getManual_entry() {
        return manual_entry;
    }

    public void setManual_entry(Boolean manual_entry) {
        this.manual_entry = manual_entry;
    }

    public Boolean getPartial_approval() {
        return partial_approval;
    }

    public void setPartial_approval(Boolean partial_approval) {
        this.partial_approval = partial_approval;
    }

    public Integer getQsr_limit() {
        return qsr_limit;
    }

    public void setQsr_limit(Integer qsr_limit) {
        this.qsr_limit = qsr_limit;
    }

    public List<String> getReaders() {
        return readers;
    }

    public void setReaders(List<String> readers) {
        this.readers = readers;
    }

    public Boolean getReturn_to_idle() {
        return return_to_idle;
    }

    public void setReturn_to_idle(Boolean return_to_idle) {
        this.return_to_idle = return_to_idle;
    }

    public Boolean getToken_request() {
        return token_request;
    }

    public void setToken_request(Boolean token_request) {
        this.token_request = token_request;
    }

    public Boolean getTxn_status_events() {
        return txn_status_events;
    }

    public void setTxn_status_events(Boolean txn_status_events) {
        this.txn_status_events = txn_status_events;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ErrorInfo getError_info() {
        return error_info;
    }

    public void setError_info(ErrorInfo error_info) {
        this.error_info = error_info;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getClient_txn_id() {
        return client_txn_id;
    }

    public void setClient_txn_id(String client_txn_id) {
        this.client_txn_id = client_txn_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTxn_type() {
        return txn_type;
    }

    public void setTxn_type(String txn_type) {
        this.txn_type = txn_type;
    }

    public List<String> getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(List<String> paymentType) {
        this.paymentType = paymentType;
    }

}
