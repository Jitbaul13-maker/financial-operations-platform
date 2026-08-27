package com.finops.financial_operations_platform.reconciliationEngine.models;

import com.finops.financial_operations_platform.reconciliationEngine.enums.ReconciliationResultType;
import com.finops.financial_operations_platform.reconciliationEngine.enums.ReconciliationSeverity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reconciliation_result")
@NoArgsConstructor
@Getter
@Setter
public class ReconciliationResult {

    public ReconciliationResult(String internalTransactionId, String providerTransactionId,
                                ReconciliationResultType resultType, ReconciliationSeverity severity,
                                String remarks) {
        this.internalTransactionId = internalTransactionId;
        this.providerTransactionId = providerTransactionId;
        this.resultType = resultType;
        this.severity = severity;
        this.remarks = remarks;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "run_id")
    private Long runId;

    @Column(name = "internal_transaction_id", length = 100)
    private String internalTransactionId;

    @Column(name = "provider_transaction_id", length = 50)
    private String providerTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_type", nullable = false)
    private ReconciliationResultType resultType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReconciliationSeverity severity;

    private String remarks;
}
