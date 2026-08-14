package com.finops.financial_operations_platform.rules.implementations;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.TransactionRule;
import org.springframework.stereotype.Component;

@Component
public class DuplicateRule implements TransactionRule {

    private final TransactionRepository transactionRepository;

    public DuplicateRule(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    String ruleCode = "DUPLICATE_RULE";

    @Override
    public RuleResult evaluate(TransactionContext context) {
        if (transactionRepository.existsByProviderTransactionId(context.providerTransactionId())) {
            return new RuleResult(
                    ruleCode,
                    RuleDecision.REJECT,
                    "Transaction with the provided provider transaction ID already exists."
            );
        }
        return new RuleResult(
                ruleCode,
                RuleDecision.PASS,
                "No existing transaction found with the provided provider transaction ID."
        );
    }
}
