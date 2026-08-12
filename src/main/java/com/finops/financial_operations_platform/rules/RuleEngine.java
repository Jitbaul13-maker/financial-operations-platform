package com.finops.financial_operations_platform.rules;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleEngine {
    private final List<TransactionRule> rules;

    public RuleEngine(List<TransactionRule> rules) {
        this.rules = rules;
    }

    public List<RuleResult> evaluate(TransactionContext context) {

        List<RuleResult> results = new ArrayList<>();

        for(TransactionRule rule: rules){
            results.add(rule.evaluate(context));
        }

        return results;
    }
}
