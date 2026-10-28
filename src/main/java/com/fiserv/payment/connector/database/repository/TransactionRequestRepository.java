/**
 * 
 */
package com.fiserv.payment.connector.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fiserv.payment.connector.database.model.TransactionRequest;

/**
 * @author RejiSivaraman
 *
 */
@Repository
public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, Long> {

    public boolean existsByFlowid(String flowid);

    public List<TransactionRequest> findByFlowid(String flowid);
}
