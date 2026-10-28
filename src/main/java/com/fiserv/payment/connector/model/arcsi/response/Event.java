package com.fiserv.payment.connector.model.arcsi.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiserv.payment.connector.model.arcsi.request.Resource;

public class Event {

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonAlias({ "flow_id", "flowId" })
    private String flowId;

    @JsonProperty("resource")
    private Resource resource;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
