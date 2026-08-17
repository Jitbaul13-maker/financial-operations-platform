package com.finops.financial_operations_platform.repos;

import com.finops.financial_operations_platform.models.ProviderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderTransactionRepository extends JpaRepository<ProviderTransaction, Long> {

}
