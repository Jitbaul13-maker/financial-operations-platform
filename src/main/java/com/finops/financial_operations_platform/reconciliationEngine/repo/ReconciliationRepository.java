package com.finops.financial_operations_platform.reconciliationEngine.repo;

import com.finops.financial_operations_platform.reconciliationEngine.models.ReconciliationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconciliationRepository extends JpaRepository<ReconciliationResult, Long> {
}
