package com.finops.financial_operations_platform.rules;

import com.finops.financial_operations_platform.Exceptions.BusinessRuleNotFoundException;
import com.finops.financial_operations_platform.Exceptions.InactiveBusinessRuleException;
import com.finops.financial_operations_platform.Exceptions.InvalidBusinessRuleConfigurationException;
import com.finops.financial_operations_platform.models.BusinessRule;
import com.finops.financial_operations_platform.repos.BusinessRuleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RuleConfigurationService {
    private final BusinessRuleRepository ruleRepository;

    public RuleConfigurationService(BusinessRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public BusinessRule findRule(String rule_code) {
        BusinessRule rule = ruleRepository.findByRuleCode(rule_code)
                .orElseThrow(() -> new BusinessRuleNotFoundException(
                        "No business rule found associated with rule code: " + rule_code));

        if (!rule.getEnabled()) {
            throw new InactiveBusinessRuleException("Business rule is not active");
        }

        return rule;
    }

    public BigDecimal getRequiredDecimal(String ruleCode, String key) {
        BusinessRule rule = findRule(ruleCode);
        Object value = rule.getConfiguration().get(key);

        if (!(value instanceof Number)) {
            throw new InvalidBusinessRuleConfigurationException(
                    "Configuration " + key + " for rule " + ruleCode + " must be numeric"
            );
        }

        return new BigDecimal(value.toString());
    }
}
