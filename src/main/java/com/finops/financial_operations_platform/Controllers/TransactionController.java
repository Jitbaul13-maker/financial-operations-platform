package com.finops.financial_operations_platform.Controllers;

import com.finops.financial_operations_platform.Dtos.AuditResponse;
import com.finops.financial_operations_platform.Dtos.CreateTransactionRequest;
import com.finops.financial_operations_platform.Dtos.TransactionResponse;
import com.finops.financial_operations_platform.Dtos.TransactionStatusUpdateRequest;
import com.finops.financial_operations_platform.audit.service.AuditLogService;
import com.finops.financial_operations_platform.Services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/my")
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest req,
                                                                 @RequestHeader("Idempotency-Key") String key) {
        TransactionResponse response = transactionService.createTransaction(req, key);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<TransactionResponse> getByTransactionId() {
        TransactionResponse response = transactionService.getTransaction();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(Pageable pageable) {
        Page<TransactionResponse> response = transactionService.getTransactions(pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{txnId}/status")
    public ResponseEntity<TransactionResponse> updateTransaction
            (@PathVariable("txnId") String id, @RequestBody @Valid TransactionStatusUpdateRequest request) {
        TransactionResponse response = transactionService.updateTransactionStatus(id, request.status());
        return ResponseEntity.ok(response);
    }
}