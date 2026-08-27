package com.finops.financial_operations_platform.reconciliationEngine.repo;

import com.finops.financial_operations_platform.reconciliationEngine.models.ReconciliationRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconciliationRunRepository extends JpaRepository<ReconciliationRun, Long> {
}
