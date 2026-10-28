/**
 * 
 */
package com.fiserv.payment.connector.service;

import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;

/**
 * Interface OPI Transaction processing incoming requests
 */
public interface OPITransactionService {

    /**
     * Interface call for processing the request
     * 
     * @param opiTransactionRequest - the incoming request converted to POJO
     * @param port                  - the port at which the incoming request came.
     *                              further this port will determine what is the
     *                              payment device being redirected to.
     * @return {@link OPITransactionResponse}
     */
    OPITransactionResponse processRequest(OPITransactionRequest opiTransactionRequest, String port);

}
