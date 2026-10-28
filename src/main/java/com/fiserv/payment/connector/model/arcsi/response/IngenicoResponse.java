package com.fiserv.payment.connector.model.arcsi.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IngenicoResponse {

    @JsonProperty("event")
    private Event event;

    @JsonProperty("response")
    private Response response;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}
