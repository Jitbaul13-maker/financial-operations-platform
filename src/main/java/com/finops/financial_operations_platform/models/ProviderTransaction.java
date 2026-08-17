package com.finops.financial_operations_platform.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "provider_transaction",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_provider_txn_id",
                        columnNames = {"provider", "provider_transaction_id"}
                )
        }
)
public class ProviderTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String provider;

    @Column(name = "provider_transaction_id", nullable = false, length = 50)
    private String providerTransactionId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, length = 15)
    private String status;

    @Column(name = "created_at", nullable = false)
    OffsetDateTime createdAt;
}
