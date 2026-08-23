package com.finops.financial_operations_platform.reconcilliationEngine.service;

import com.finops.financial_operations_platform.models.ProviderTransaction;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.reconcilliationEngine.enums.ReconciliationResultType;
import com.finops.financial_operations_platform.reconcilliationEngine.enums.ReconciliationSeverity;
import com.finops.financial_operations_platform.reconcilliationEngine.models.ReconciliationResult;
import com.finops.financial_operations_platform.reconcilliationEngine.repo.ReconciliationRepository;
import com.finops.financial_operations_platform.reconcilliationEngine.rules.ReconciliationMatcher;
import com.finops.financial_operations_platform.repos.ProviderTransactionRepository;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReconciliationService {

    private final ReconciliationMatcher matcher;
    private final TransactionRepository transactionRepository;
    private final ProviderTransactionRepository providerTransactionRepository;
    private final ReconciliationRepository reconciliationRepository;

    public ReconciliationService(ReconciliationMatcher matcher, TransactionRepository transactionRepository,
                                 ProviderTransactionRepository providerTransactionRepository,
                                 ReconciliationRepository reconciliationRepository) {
        this.matcher = matcher;
        this.transactionRepository = transactionRepository;
        this.providerTransactionRepository = providerTransactionRepository;
        this.reconciliationRepository = reconciliationRepository;
    }

    public ReconciliationResult reconcile(String provider, String transactionId, String providerTransactionId) {
        List<Transaction> transactions = transactionRepository
                .findByProviderAndProviderTransactionId(provider, transactionId)
                .orElse(null);

        ProviderTransaction providerTransaction = providerTransactionRepository
                .findByProviderAndProviderTransactionId(provider, providerTransactionId)
                .orElse(null);

        if (transactions == null) {
            ReconciliationResult record = new ReconciliationResult(
                    null,
                    providerTransactionId,
                    ReconciliationResultType.MISSING_INTERNAL,
                    ReconciliationSeverity.HIGH,
                    "Missing corresponding internal record!"
            );
            return reconciliationRepository.save(record);
        }

        if (providerTransaction == null) {
            ReconciliationResult record = new ReconciliationResult(
                    transactionId,
                    null,
                    ReconciliationResultType.MISSING_PROVIDER,
                    ReconciliationSeverity.HIGH,
                    "Missing corresponding external record!"
            );
            return reconciliationRepository.save(record);
        }

        ReconciliationResult record = matcher.matcher(providerTransaction, transactions);
        return reconciliationRepository.save(record);
    }
}
