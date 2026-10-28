/**
 * 
 */
package com.fiserv.payment.connector.database.service;

import java.util.List;

/**
 * @author RejiSivaraman
 *
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiserv.payment.connector.database.model.TransactionRequest;
import com.fiserv.payment.connector.database.repository.TransactionRequestRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionRequestDataServiceImpl implements TransactionRequestDataService {

    @Autowired
    private TransactionRequestRepository transactionRequestRepository;

    @Transactional
    public String createTransactionRequest(TransactionRequest transactionRequest) {
        try {
            if (!transactionRequestRepository.existsByFlowid(transactionRequest.getFlowid())) {
                transactionRequestRepository.save(transactionRequest);
                return "TransactionRequest record created successfully.";
            } else {
                return "TransactionRequest already exists in the database.";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public List<TransactionRequest> readTransactionRequests() {
        return transactionRequestRepository.findAll();
    }

    @Transactional
    public String updateTransactionRequest(TransactionRequest transactionRequest) {
        if (transactionRequestRepository.existsByFlowid(transactionRequest.getExistingFlowid())) {
            try {
                List<TransactionRequest> transactionRequests = transactionRequestRepository
                        .findByFlowid(transactionRequest.getExistingFlowid());
                transactionRequests.stream().forEach(s -> {
                    TransactionRequest transactionRequestToBeUpdate = transactionRequestRepository.findById(s.getId())
                            .get();
                    transactionRequestToBeUpdate.setFlowid(transactionRequest.getFlowid());
                    transactionRequestToBeUpdate.setRequestJSON(transactionRequest.getRequestJSON());
                    transactionRequestRepository.save(transactionRequestToBeUpdate);
                });
                return "TransactionRequest record updated.";
            } catch (Exception e) {
                throw e;
            }
        } else {
            return "TransactionRequest does not exists in the database.";
        }
    }

    @Transactional
    public String deleteTransactionRequest(TransactionRequest transactionRequest) {
        if (transactionRequestRepository.existsByFlowid(transactionRequest.getFlowid())) {
            try {
                List<TransactionRequest> transactionRequests = transactionRequestRepository
                        .findByFlowid(transactionRequest.getFlowid());
                transactionRequests.stream().forEach(s -> {
                    transactionRequestRepository.delete(s);
                });
                return "TransactionRequest record deleted successfully.";
            } catch (Exception e) {
                throw e;
            }

        } else {
            return "TransactionRequest does not exist";
        }
    }

    @Override
    public TransactionRequest readTransactionRequest(String flow_id) {
        List<TransactionRequest> transactionRequests = transactionRequestRepository.findByFlowid(flow_id);
        return transactionRequests.isEmpty() ? null : transactionRequests.get(0);
    }
}
