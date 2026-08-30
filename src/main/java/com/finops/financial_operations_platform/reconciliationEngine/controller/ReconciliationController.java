package com.finops.financial_operations_platform.reconciliationEngine.controller;

import com.finops.financial_operations_platform.reconciliationEngine.models.ReconciliationResult;
import com.finops.financial_operations_platform.reconciliationEngine.service.ReconciliationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reconciliation")
public class ReconciliationController {

    private final ReconciliationService reconciliationService;

    public ReconciliationController(ReconciliationService reconciliationService) {
        this.reconciliationService = reconciliationService;
    }

    @PostMapping
    public ResponseEntity<ReconciliationResult> reconcile(@RequestParam("provider") String provider,
                                                          @RequestParam("transactionId") String transactionId,
                                                          @RequestParam("providerTransactionId") String providerTransactionId) {

        ReconciliationResult result = reconciliationService.reconcile(
                provider,
                providerTransactionId,
                1L
        );

        return ResponseEntity.ok(result);
    }
}