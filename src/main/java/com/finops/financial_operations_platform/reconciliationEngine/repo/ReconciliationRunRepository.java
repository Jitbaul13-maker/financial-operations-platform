package com.finops.financial_operations_platform.reconciliationEngine.repo;

import com.finops.financial_operations_platform.reconciliationEngine.models.ReconciliationRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReconciliationRunRepository extends JpaRepository<ReconciliationRun, Long> {
    Optional<ReconciliationRun> findByProviderAndBusinessDate(String provider, LocalDate business_date);
}
