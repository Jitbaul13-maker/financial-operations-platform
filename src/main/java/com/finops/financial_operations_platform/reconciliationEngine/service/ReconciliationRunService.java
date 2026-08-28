package com.finops.financial_operations_platform.reconciliationEngine.service;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.models.ProviderTransaction;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.reconciliationEngine.enums.ReconciliationResultType;
import com.finops.financial_operations_platform.reconciliationEngine.enums.ReconciliationRunStatus;
import com.finops.financial_operations_platform.reconciliationEngine.models.ReconciliationResult;
import com.finops.financial_operations_platform.reconciliationEngine.models.ReconciliationRun;
import com.finops.financial_operations_platform.reconciliationEngine.repo.ReconciliationRunRepository;
import com.finops.financial_operations_platform.repos.ProviderTransactionRepository;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReconciliationRunService {

    private final ReconciliationRunRepository reconciliationRunRepository;
    private final TransactionRepository transactionRepository;
    private final ProviderTransactionRepository providerTransactionRepository;
    private final ReconciliationService reconciliationService;

    private ReconciliationRun start(String provider, LocalDate businessDate) {

        ReconciliationRun run = new ReconciliationRun();

        run.setProvider(provider);
        run.setBusinessDate(businessDate);
        run.setStartedAt(OffsetDateTime.now());
        run.setStatus(ReconciliationRunStatus.RUNNING);

        return reconciliationRunRepository.save(run);
    }

    private void complete(ReconciliationRun run, int totalRecords, int matchedCount, int discrepancyCount) {
        run.setCompletedAt(OffsetDateTime.now());
        run.setTotalRecords(totalRecords);
        run.setMatchedCount(matchedCount);
        run.setDiscrepancyCount(discrepancyCount);
        run.setStatus(ReconciliationRunStatus.COMPLETED);
        reconciliationRunRepository.save(run);
    }

    private void fail(ReconciliationRun run, int totalRecords, int matchedCount, int discrepancyCount) {
        run.setCompletedAt(OffsetDateTime.now());
        run.setTotalRecords(totalRecords);
        run.setMatchedCount(matchedCount);
        run.setDiscrepancyCount(discrepancyCount);
        run.setStatus(ReconciliationRunStatus.FAILED);
        reconciliationRunRepository.save(run);
    }

    public void execute(String provider, LocalDate businessDate) {

        Optional<ReconciliationRun> existing = reconciliationRunRepository
                .findByProviderAndBusinessDate(provider, businessDate);

        if (existing.isPresent()) return;

        ReconciliationRun run = start(provider, businessDate);

        int totalRecords = 0;
        int matchedCount = 0;
        int discrepancyCount = 0;

        try {
            Long runId = run.getId();

            OffsetDateTime start = businessDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
            OffsetDateTime end = businessDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();

            List<Transaction> transactions = transactionRepository
                    .findByProviderAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                            Provider.valueOf(provider),
                            start,
                            end
                    );

            Set<String> internalProviderTxnIds = new HashSet<>();

            for(Transaction transaction : transactions){
                internalProviderTxnIds.add(transaction.getProviderTransactionId());
            }

            List<ProviderTransaction> providerTransactions = providerTransactionRepository
                    .findByProviderAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                            provider,
                            start,
                            end
                    );

            Set<String> externalProviderTxnIds = new HashSet<>();

            for(ProviderTransaction transaction : providerTransactions){
                externalProviderTxnIds.add(transaction.getProviderTransactionId());
            }

            Set<String> allTxnIds = new HashSet<>(internalProviderTxnIds);
            allTxnIds.addAll(externalProviderTxnIds);

            for (String providerTxnId : allTxnIds) {
                ReconciliationResult result = reconciliationService.reconcile(provider, providerTxnId, runId);

                totalRecords++;

                if (result.getResultType() == ReconciliationResultType.MATCHED) matchedCount++;
                else discrepancyCount++;
            }

            complete(run, totalRecords, matchedCount, discrepancyCount);
        } catch (Exception e){
            fail(run, totalRecords, matchedCount, discrepancyCount);
            throw e;
        }

    }
}
