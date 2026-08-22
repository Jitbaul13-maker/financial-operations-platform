package com.finops.financial_operations_platform.repos;

import com.finops.financial_operations_platform.models.ProviderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderTransactionRepository extends JpaRepository<ProviderTransaction, Long> {
    Optional<ProviderTransaction> findByProviderAndProviderTransactionId(String provider, String providerTxnId);
}
