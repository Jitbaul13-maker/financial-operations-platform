package com.finops.financial_operations_platform.repos;

import com.finops.financial_operations_platform.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Optional<List<AuditLog>> findByTransactionIdOrderByCreatedAtAsc(String id);
}
