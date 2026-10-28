/**
 * 
 */
package com.fiserv.payment.connector.database.service;

import java.util.List;

import com.fiserv.payment.connector.database.model.TransactionRequest;

public interface TransactionRequestDataService {

    public String createTransactionRequest(TransactionRequest transactionRequest);

    public List<TransactionRequest> readTransactionRequests();

    public String updateTransactionRequest(TransactionRequest transactionRequest);

    public String deleteTransactionRequest(TransactionRequest transactionRequest);

    public TransactionRequest readTransactionRequest(String flow_id);
}
