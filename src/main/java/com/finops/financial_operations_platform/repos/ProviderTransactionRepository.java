package com.finops.financial_operations_platform.repos;

import com.finops.financial_operations_platform.models.ProviderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderTransactionRepository extends JpaRepository<ProviderTransaction, Long> {
    ProviderTransaction findByProviderAndProviderTransactionId(String provider, String providerTxnId);
    List<ProviderTransaction> findByProviderAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            String provider,
            OffsetDateTime start,
            OffsetDateTime end
    );
}
