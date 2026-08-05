package com.finops.financial_operations_platform.Transactions;

import com.finops.financial_operations_platform.Dtos.CreateTransactionRequest;
import com.finops.financial_operations_platform.Dtos.TransactionResponse;
import com.finops.financial_operations_platform.Services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest req){
        TransactionResponse response = service.createTransaction(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{txnId}")
    public ResponseEntity<TransactionResponse> getByTransactionId(@PathVariable("txnId") String txnId) {
        TransactionResponse response = service.getTransaction(txnId);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(Pageable pageable) {
        Page<TransactionResponse> response = service.getTransactions(pageable);
        return ResponseEntity.ok(response);
    }
}
