package com.fiserv.payment.connector.model.arcsi.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IngenicoEventAck {

    @JsonProperty("event_ack")
    private EventAck eventAck;

    public EventAck getEventAck() {
        return eventAck;
    }

    public void setEventAck(EventAck eventAck) {
        this.eventAck = eventAck;
    }

}
