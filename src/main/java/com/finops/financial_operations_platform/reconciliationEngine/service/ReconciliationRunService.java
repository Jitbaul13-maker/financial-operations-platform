package com.finops.financial_operations_platform.reconciliationEngine.service;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.models.ProviderTransaction;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.reconciliationEngine.enums.ReconciliationRunStatus;
import com.finops.financial_operations_platform.reconciliationEngine.models.ReconciliationRun;
import com.finops.financial_operations_platform.reconciliationEngine.repo.ReconciliationRunRepository;
import com.finops.financial_operations_platform.repos.ProviderTransactionRepository;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReconciliationRunService {

    private final ReconciliationRunRepository reconciliationRunRepository;
    private final TransactionRepository transactionRepository;
    private final ProviderTransactionRepository providerTransactionRepository;
    private final ReconciliationService reconciliationService;

    public ReconciliationRun start(String provider, LocalDate businessDate) {

        ReconciliationRun run = new ReconciliationRun();

        run.setProvider(provider);
        run.setBusinessDate(businessDate);
        run.setStartedAt(OffsetDateTime.now());
        run.setStatus(ReconciliationRunStatus.RUNNING);

        return reconciliationRunRepository.save(run);
    }

    public void execute(String provider, LocalDate businessDate) {
        ReconciliationRun run = start(provider, businessDate);

        Long runId = run.getId();

        OffsetDateTime start = businessDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime end = businessDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();

        List<Transaction> transactions = transactionRepository
                .findByProviderAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                        Provider.valueOf(provider),
                        start,
                        end
                );

        List<ProviderTransaction> providerTransactions = providerTransactionRepository
                .findByProviderAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                        provider,
                        start,
                        end
                );
    }
}
