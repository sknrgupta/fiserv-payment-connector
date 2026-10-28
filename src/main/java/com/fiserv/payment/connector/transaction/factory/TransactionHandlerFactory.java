package com.fiserv.payment.connector.transaction.factory;

import org.springframework.stereotype.Component;

import com.fiserv.payment.connector.transaction.TransactionHandler;
import com.fiserv.payment.connector.transaction.type.TransactionType;

@Component
public interface TransactionHandlerFactory {

    public TransactionHandler getTransactionHandler(TransactionType transactionType);
}
