package com.finops.financial_operations_platform.audit.controller;

import com.finops.financial_operations_platform.Dtos.AuditResponse;
import com.finops.financial_operations_platform.audit.service.AuditLogService;
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
public class AuditController {

    private final AuditLogService auditLogService;

    @GetMapping("/{txnId}")
    public ResponseEntity<List<AuditResponse>> getAuditHistory(@PathVariable("txnId") String id) {
        List<AuditResponse> response = auditLogService.getAuditHistory(id);
        return ResponseEntity.ok(response);
    }
}
