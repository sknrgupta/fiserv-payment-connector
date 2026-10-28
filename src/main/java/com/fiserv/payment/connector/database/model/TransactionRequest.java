/**
 * 
 */
package com.fiserv.payment.connector.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

/**
 * @author RejiSivaraman
 *
 */
@Entity
public class TransactionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private String existingFlowid;

    private String flowid;
    private String requestJSON;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the existingFlowid
     */
    public String getExistingFlowid() {
        return existingFlowid;
    }

    /**
     * @param existingFlowid the existingFlowid to set
     */
    public void setExistingFlowId(String existingFlowid) {
        this.existingFlowid = existingFlowid;
    }

    /**
     * @return the flowid
     */
    public String getFlowid() {
        return flowid;
    }

    /**
     * @param flowid the flowid to set
     */
    public void setFlowid(String flowid) {
        this.flowid = flowid;
    }

    /**
     * @return the requestJSON
     */
    public String getRequestJSON() {
        return requestJSON;
    }

    /**
     * @param requestJSON the requestJSON to set
     */
    public void setRequestJSON(String requestJSON) {
        this.requestJSON = requestJSON;
    }

}
