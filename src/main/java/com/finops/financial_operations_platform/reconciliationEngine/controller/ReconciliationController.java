package com.finops.financial_operations_platform.reconciliationEngine.controller;

import com.finops.financial_operations_platform.reconciliationEngine.models.ReconciliationResult;
import com.finops.financial_operations_platform.reconciliationEngine.service.ReconciliationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reconciliation")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Reconciliation",
        description = "APIs for performing and retrieving financial reconciliation operations"
)
public class ReconciliationController {

    private final ReconciliationService reconciliationService;

    public ReconciliationController(ReconciliationService reconciliationService) {
        this.reconciliationService = reconciliationService;
    }

    @PostMapping
    @Operation(
            summary = "Trigger reconciliation",
            description = "Manually triggers a reconciliation process and returns the resulting reconciliation details, including severity and other relevant findings."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Reconciliation completed successfully"
    )
    public ResponseEntity<ReconciliationResult> reconcile(@RequestParam("provider") String provider,
                                                          @RequestParam("providerTransactionId") String providerTransactionId,
                                                          @RequestParam("runId") Long runId) {

        ReconciliationResult result = reconciliationService.reconcile(
                provider,
                providerTransactionId,
                runId
        );

        return ResponseEntity.ok(result);
    }
}