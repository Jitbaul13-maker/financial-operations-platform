package com.finops.financial_operations_platform.reconciliationEngine.models;

import com.finops.financial_operations_platform.reconciliationEngine.enums.ReconciliationRunStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "reconciliation_run")
public class ReconciliationRun {

    public ReconciliationRun(String provider, LocalDate businessDate, OffsetDateTime startedAt, ReconciliationRunStatus status) {
        this.provider = provider;
        this.businessDate = businessDate;
        this.startedAt = startedAt;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String provider;

    @Column(name = "business_date", nullable = false)
    private LocalDate businessDate;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReconciliationRunStatus status;

    @Column(name = "total_records", nullable = false)
    private int totalRecords = 0;

    @Column(name = "matched_count", nullable = false)
    private int matchedCount = 0;

    @Column(name = "discrepancy_count", nullable = false)
    private int discrepancyCount = 0;
}
