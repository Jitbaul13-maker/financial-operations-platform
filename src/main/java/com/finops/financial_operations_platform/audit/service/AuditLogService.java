package com.finops.financial_operations_platform.audit.service;

import com.finops.financial_operations_platform.Dtos.AuditResponse;
import com.finops.financial_operations_platform.Exceptions.TransactionNotFoundException;
import com.finops.financial_operations_platform.audit.model.AuditLog;
import com.finops.financial_operations_platform.enums.TransactionStatus;
import com.finops.financial_operations_platform.repos.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public List<AuditResponse> getAuditHistory(String id) {
        List<AuditLog> logs = auditLogRepository.findByTransactionIdOrderByCreatedAtAsc(id)
                .orElseThrow(() -> new TransactionNotFoundException("No valid transaction found!"));

        return logs.stream().map(this::mapToResponse).toList();
    }

    private AuditResponse mapToResponse(AuditLog log) {
        return new AuditResponse(
                log.getTransactionId(),
                log.getOldStatus(),
                log.getNewStatus(),
                log.getActor(),
                log.getReason(),
                log.getCreatedAt()
        );
    }

    public void recordAudit(String transactionId, TransactionStatus oldStatus, TransactionStatus newStatus,
                             String actor, String reason) {

//        throw new RuntimeException("Simulated audit failure");

        AuditLog log = new AuditLog(
                oldStatus, newStatus, actor, reason, transactionId
        );

        auditLogRepository.save(log);
    }
}
