package com.finops.financial_operations_platform.models;

import com.finops.financial_operations_platform.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus oldStatus;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus newStatus;

    @Column(nullable = false)
    private String actor;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = OffsetDateTime.now();
    }

    @Column(nullable = false)
    private String reason;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;
}
