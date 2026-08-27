package com.finops.financial_operations_platform.repos;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);
    boolean existsByProviderTransactionId(String providerTxnId);
    List<Transaction> findByProviderAndProviderTransactionId(String provider, String providerTxnId);
    List<Transaction> findByProviderAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            Provider provider,
            OffsetDateTime start,
            OffsetDateTime end
    );
}
