package com.finops.financial_operations_platform.repos;

import com.finops.financial_operations_platform.models.BusinessRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRuleRepository extends JpaRepository<BusinessRule, Long> {
    Optional<BusinessRule> findByRuleCode(String ruleCode);
}
