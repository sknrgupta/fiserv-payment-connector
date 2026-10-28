/**
 * 
 */
package com.fiserv.payment.connector.service;

import com.fiserv.payment.connector.model.eftlink.request.OPITransactionRequest;
import com.fiserv.payment.connector.model.eftlink.response.OPITransactionResponse;

/**
 * 
 */
public interface OPITransactionService {

    OPITransactionResponse processRequest(OPITransactionRequest opiTransactionRequest, String port);

}
