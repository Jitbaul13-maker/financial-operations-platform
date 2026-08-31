package com.finops.financial_operations_platform.audit.controller;

import com.finops.financial_operations_platform.audit.dto.AuditResponse;
import com.finops.financial_operations_platform.audit.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audit")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Audit",
        description = "APIs for accessing transaction and system audit records"
)
public class AuditController {

    private final AuditLogService auditLogService;

    @Operation(
            summary = "Get audit record",
            description = "Retrieves the audit record for the specified transaction."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Audit record retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Audit record not found"
            )
    })
    @GetMapping("/{txnId}")
    public ResponseEntity<List<AuditResponse>> getAuditHistory(@PathVariable("txnId") String id) {
        List<AuditResponse> response = auditLogService.getAuditHistory(id);
        return ResponseEntity.ok(response);
    }
}
