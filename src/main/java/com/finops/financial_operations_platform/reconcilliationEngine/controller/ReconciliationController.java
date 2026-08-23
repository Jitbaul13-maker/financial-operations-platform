package com.finops.financial_operations_platform.reconcilliationEngine.controller;

import com.finops.financial_operations_platform.reconcilliationEngine.models.ReconciliationResult;
import com.finops.financial_operations_platform.reconcilliationEngine.service.ReconciliationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reconciliation")
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
                transactionId,
                providerTransactionId
        );

        return ResponseEntity.ok(result);
    }
}