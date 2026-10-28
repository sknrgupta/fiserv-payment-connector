package com.fiserv.payment.connector.model.arcsi.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Amount {

    private int cashback;
    private int tax;
    private int tip;
    private int total;

    public Integer getCashback() {
        return cashback;
    }

    public void setCashback(Integer cashback) {
        this.cashback = cashback;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public Integer getTip() {
        return tip;
    }

    public void setTip(Integer tip) {
        this.tip = tip;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
