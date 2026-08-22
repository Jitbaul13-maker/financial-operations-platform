package com.finops.financial_operations_platform.reconcilliationEngine.rules;

import com.finops.financial_operations_platform.externalLedger.ProviderStatusNormalizer;
import com.finops.financial_operations_platform.models.ProviderTransaction;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.reconcilliationEngine.enums.ReconciliationResultType;
import com.finops.financial_operations_platform.reconcilliationEngine.enums.ReconciliationSeverity;
import com.finops.financial_operations_platform.reconcilliationEngine.models.ReconciliationResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReconciliationMatcher {

    private final ProviderStatusNormalizer normalizer;

    public ReconciliationMatcher(ProviderStatusNormalizer normalizer) {
        this.normalizer = normalizer;
    }

    private int resultPriority(ReconciliationResultType resultType) {
        return switch (resultType) {
            case AMOUNT_MISMATCH -> 3;
            case CURRENCY_MISMATCH -> 2;
            case STATUS_MISMATCH -> 1;
            default -> 0;
        };
    }

    private boolean shouldReplace(
            ReconciliationResultType currentType,
            ReconciliationSeverity currentSeverity,
            ReconciliationResultType newType,
            ReconciliationSeverity newSeverity) {

        if (newSeverity.getPriority() > currentSeverity.getPriority()) {
            return true;
        }

        if (newSeverity.getPriority() < currentSeverity.getPriority()) {
            return false;
        }

        return resultPriority(newType) > resultPriority(currentType);
    }

    public ReconciliationResult matcher(ProviderTransaction providerTransaction, List<Transaction> transactions) {

        if (transactions.size() > 1) {
            return new ReconciliationResult(
                    null,
                    providerTransaction.getProviderTransactionId(),
                    ReconciliationResultType.DUPLICATE,
                    ReconciliationSeverity.HIGH,
                    "Duplicate internal records detected!"
            );
        }

        List<String> remarks = new ArrayList<>();
        ReconciliationSeverity severity = ReconciliationSeverity.NONE;
        ReconciliationResultType resultType = ReconciliationResultType.MATCHED;

        Transaction txn = transactions.getFirst();
        String status = normalizer.normalize(providerTransaction.getProvider(), providerTransaction.getStatus());

        if (txn.getStatus().name().equals(status)) {
            remarks.add("status ✅");
        } else {
            ReconciliationSeverity newSeverity = ReconciliationSeverity.MEDIUM;
            ReconciliationResultType newResultType = ReconciliationResultType.STATUS_MISMATCH;

            if (shouldReplace(resultType, severity,
                    newResultType, newSeverity)) {
                resultType = newResultType;
                severity = newSeverity;
            }

            remarks.add("status mismatch: " + txn.getStatus() + ", " + status);
        }

        if (txn.getAmount().compareTo(providerTransaction.getAmount()) == 0) {
            remarks.add("amount ✅");
        } else {
            ReconciliationSeverity newSeverity = ReconciliationSeverity.CRITICAL;
            ReconciliationResultType newResultType = ReconciliationResultType.AMOUNT_MISMATCH;

            if (shouldReplace(resultType, severity,
                    newResultType, newSeverity)) {
                resultType = newResultType;
                severity = newSeverity;
            }

            remarks.add("amount mismatch: " + txn.getAmount() + ", " + providerTransaction.getAmount());
        }

        if (txn.getCurrency().equalsIgnoreCase(providerTransaction.getCurrency())) {
            remarks.add("currency ✅");
        } else {
            ReconciliationSeverity newSeverity = ReconciliationSeverity.CRITICAL;
            ReconciliationResultType newResultType = ReconciliationResultType.CURRENCY_MISMATCH;

            if (shouldReplace(resultType, severity,
                    newResultType, newSeverity)) {
                resultType = newResultType;
                severity = newSeverity;
            }

            remarks.add("currency mismatch: " + txn.getCurrency() + ", " + providerTransaction.getCurrency());
        }

        String remark = String.join("; ", remarks);

        return new ReconciliationResult(
                transactions.getFirst().getTransactionId(),
                providerTransaction.getProviderTransactionId(),
                resultType,
                severity,
                remark
        );
    }
}
