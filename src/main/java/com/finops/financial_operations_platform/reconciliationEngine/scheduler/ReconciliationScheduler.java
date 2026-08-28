package com.finops.financial_operations_platform.reconciliationEngine.scheduler;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.reconciliationEngine.service.ReconciliationRunService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReconciliationScheduler {

    private final ReconciliationRunService reconciliationRunService;

    public ReconciliationScheduler(ReconciliationRunService reconciliationRunService) {
        this.reconciliationRunService = reconciliationRunService;
    }

    @Scheduled(fixedRate = 12 * 60 * 60 * 10000)
    public void runDailyReconciliation(){

        for(Provider provider: Provider.values()) {

            reconciliationRunService.execute(
                    provider.name(),
                    LocalDate.now()
            );
        }
    }
}
