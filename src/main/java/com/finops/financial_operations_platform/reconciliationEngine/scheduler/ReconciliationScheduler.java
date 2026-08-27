package com.finops.financial_operations_platform.reconciliationEngine.scheduler;

import com.finops.financial_operations_platform.reconciliationEngine.service.ReconciliationRunService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReconciliationScheduler {

    private final ReconciliationRunService reconciliationRunServiceService;

    public ReconciliationScheduler(ReconciliationRunService reconciliationRunServiceService) {
        this.reconciliationRunServiceService = reconciliationRunServiceService;
    }

    @Scheduled(fixedRate = 10000)
    public void runDailyReconciliation(){
        reconciliationRunServiceService.execute(
                "RAZORPAY",
                LocalDate.now().minusDays(2)
        );
    }
}

