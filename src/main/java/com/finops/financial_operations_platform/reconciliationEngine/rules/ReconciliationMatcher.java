package com.finops.financial_operations_platform.reconciliationEngine.rules;

import com.finops.financial_operations_platform.externalLedger.ProviderStatusNormalizer;
import com.finops.financial_operations_platform.models.ProviderTransaction;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.reconciliationEngine.enums.ReconciliationResultType;
import com.finops.financial_operations_platform.reconciliationEngine.enums.ReconciliationSeverity;
import com.finops.financial_operations_platform.reconciliationEngine.models.ReconciliationResult;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class ReconciliationMatcher {

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

        List<String> remarks = new ArrayList<>();
        ReconciliationSeverity severity = ReconciliationSeverity.NONE;
        ReconciliationResultType resultType = ReconciliationResultType.MATCHED;

        Transaction txn = transactions.getFirst();
        Duration difference = Duration.between(txn.getCreatedAt(), providerTransaction.getCreatedAt()).abs();

        if (txn.getStatus().name().equals(providerTransaction.getStatus())) {
            remarks.add("status ✅");
        } else {
            ReconciliationSeverity newSeverity = ReconciliationSeverity.MEDIUM;
            ReconciliationResultType newResultType = ReconciliationResultType.STATUS_MISMATCH;

            if (shouldReplace(resultType, severity, newResultType, newSeverity)) {
                resultType = newResultType;
                severity = newSeverity;
            }

            remarks.add("status mismatch: " + txn.getStatus() + ", " + providerTransaction.getStatus());
        }

        if (txn.getAmount().compareTo(providerTransaction.getAmount()) == 0) {
            remarks.add("amount ✅");
        } else {
            ReconciliationSeverity newSeverity = ReconciliationSeverity.CRITICAL;
            ReconciliationResultType newResultType = ReconciliationResultType.AMOUNT_MISMATCH;

            if (shouldReplace(resultType, severity, newResultType, newSeverity)) {
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

            if (shouldReplace(resultType, severity, newResultType, newSeverity)) {
                resultType = newResultType;
                severity = newSeverity;
            }

            remarks.add("currency mismatch: " + txn.getCurrency() + ", " + providerTransaction.getCurrency());
        }

        if (difference.compareTo(Duration.ofSeconds(30)) <= 0) {
            remarks.add("time ✅");
        } else {
            remarks.add("time mismatch: " + txn.getCreatedAt() + ", " + providerTransaction.getCreatedAt());
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
