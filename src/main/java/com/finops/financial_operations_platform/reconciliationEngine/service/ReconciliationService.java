package com.finops.financial_operations_platform.reconciliationEngine.service;

import com.finops.financial_operations_platform.models.ProviderTransaction;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.reconciliationEngine.enums.ReconciliationResultType;
import com.finops.financial_operations_platform.reconciliationEngine.enums.ReconciliationSeverity;
import com.finops.financial_operations_platform.reconciliationEngine.models.ReconciliationResult;
import com.finops.financial_operations_platform.reconciliationEngine.repo.ReconciliationRepository;
import com.finops.financial_operations_platform.reconciliationEngine.rules.ReconciliationMatcher;
import com.finops.financial_operations_platform.repos.ProviderTransactionRepository;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReconciliationService {

    private final ReconciliationMatcher matcher;
    private final TransactionRepository transactionRepository;
    private final ProviderTransactionRepository providerTransactionRepository;
    private final ReconciliationRepository reconciliationRepository;

    public ReconciliationResult reconcile(String provider, String providerTransactionId, Long runId) {
        List<Transaction> transactions = transactionRepository
                .findByProviderAndProviderTransactionId(provider, providerTransactionId);

        ProviderTransaction providerTransaction = providerTransactionRepository
                .findByProviderAndProviderTransactionId(provider, providerTransactionId);

        if (transactions.isEmpty()) {
            ReconciliationResult record = new ReconciliationResult(
                    null,
                    providerTransactionId,
                    ReconciliationResultType.MISSING_INTERNAL,
                    ReconciliationSeverity.HIGH,
                    "Missing corresponding internal record!"
            );
            record.setRunId(runId);
            return reconciliationRepository.save(record);
        }

        if (transactions.size() > 1 && providerTransaction != null) {
            ReconciliationResult record = new ReconciliationResult(
                    null,
                    providerTransactionId,
                    ReconciliationResultType.DUPLICATE,
                    ReconciliationSeverity.HIGH,
                    "Duplicate internal records detected!"
            );
            record.setRunId(runId);
            return reconciliationRepository.save(record);
        }

        if (providerTransaction == null && transactions.size() == 1) {
            ReconciliationResult record = new ReconciliationResult(
                    transactions.getFirst().getTransactionId(),
                    null,
                    ReconciliationResultType.MISSING_PROVIDER,
                    ReconciliationSeverity.HIGH,
                    "Missing corresponding external record!"
            );
            record.setRunId(runId);
            return reconciliationRepository.save(record);
        }

        if (providerTransaction == null) {
            ReconciliationResult record = new ReconciliationResult(
                    "Multiple transactions detected!",
                    null,
                    ReconciliationResultType.MISSING_PROVIDER,
                    ReconciliationSeverity.HIGH,
                    "Missing corresponding external record!"
            );
            record.setRunId(runId);
            return reconciliationRepository.save(record);
        }

        ReconciliationResult record = matcher.matcher(providerTransaction, transactions);
        record.setRunId(runId);
        return reconciliationRepository.save(record);
    }
}
