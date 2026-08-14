package com.finops.financial_operations_platform.repos;

import com.finops.financial_operations_platform.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);
    boolean existsByProviderTransactionId(String providerTxnId);
}
