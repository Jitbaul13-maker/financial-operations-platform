package com.finops.financial_operations_platform.rules;

import com.finops.financial_operations_platform.Exceptions.BusinessRuleNotFoundException;
import com.finops.financial_operations_platform.Exceptions.InactiveBusinessRuleException;
import com.finops.financial_operations_platform.Exceptions.InvalidBusinessRuleConfigurationException;
import com.finops.financial_operations_platform.models.BusinessRule;
import com.finops.financial_operations_platform.repos.BusinessRuleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleConfigurationService {
    private final BusinessRuleRepository ruleRepository;

    public RuleConfigurationService(BusinessRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    private BusinessRule findRule(String rule_code) {
        BusinessRule rule = ruleRepository.findByRuleCode(rule_code)
                .orElseThrow(() -> new BusinessRuleNotFoundException(
                        "No business rule found associated with rule code: " + rule_code));

        if (!rule.getEnabled()) {
            throw new InactiveBusinessRuleException("Business rule is not active");
        }

        return rule;
    }

    private Map<String, List<String>> convertToStringListMap(Map<?, ?> map) {

        Map<String, List<String>> result = new HashMap<>();

        for (Map.Entry<?, ?> entry : map.entrySet()){
            if (!(entry.getKey() instanceof String)) {
                throw new InvalidBusinessRuleConfigurationException(
                        "Inner key for rule must be String"
                );
            }

            if (!(entry.getValue() instanceof List<?> values)) {
                throw new InvalidBusinessRuleConfigurationException(
                            "Inner key value must be a list of String"
                );
            }

            for (Object value : values) {
                if (!(value instanceof String)) {
                    throw new InvalidBusinessRuleConfigurationException(
                                "Inner key value must be a String"
                    );
                }
            }

            List<String> valueList = values.stream().map(value -> (String)value).toList();

            result.put((String) entry.getKey(), valueList);
        }

        return result;
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

    public Map<String, List<String>> getRequiredMap(String ruleCode, String key) {
        BusinessRule rule = findRule(ruleCode);
        Object value = rule.getConfiguration().get(key);

        if (!(value instanceof Map<?,?>)) {
            throw new InvalidBusinessRuleConfigurationException(
                    "Configuration " + key + " for rule " + ruleCode + " must be map"
            );
        }

        return convertToStringListMap((Map<?, ?>) value);
    }

    public Long getRequiredLong(String ruleCode, String key){
        BusinessRule rule = findRule(ruleCode);
        Object value = rule.getConfiguration().get(key);

        if (!(value instanceof Number)) {
            throw new InvalidBusinessRuleConfigurationException(
                    "Configuration " + key + " for rule " + ruleCode + " must be numeric"
            );
        }

        return ((Number) value).longValue();
    }
}
