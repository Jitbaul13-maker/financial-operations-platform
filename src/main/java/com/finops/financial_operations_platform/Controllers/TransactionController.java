package com.finops.financial_operations_platform.Controllers;

import com.finops.financial_operations_platform.Dtos.CreateTransactionRequest;
import com.finops.financial_operations_platform.Dtos.TransactionResponse;
import com.finops.financial_operations_platform.Dtos.TransactionStatusUpdateRequest;
import com.finops.financial_operations_platform.Services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Transactions",
        description = "APIs for creating, retrieving, and updating financial transactions"
)
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Create transaction",
            description = "Creates a new transaction for the authenticated customer using an idempotency key."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Transaction created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid transaction data"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Idempotency key is already in progress or conflicts with an existing transaction"
            )
    })
    @PostMapping("/my")
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest req,
                                                                 @RequestHeader("Idempotency-Key") String key) {
        TransactionResponse response = transactionService.createTransaction(req, key);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get transaction",
            description = "Retrieves a transaction belonging to the authenticated customer."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Transaction retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transaction not found"
            )
    })
    @GetMapping("/my")
    public ResponseEntity<TransactionResponse> getByTransactionId() {
        TransactionResponse response = transactionService.getTransaction();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get all transactions",
            description = "Retrieves all transaction records. Restricted to users with the OPERATIONAL_USER role."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Transactions retrieved successfully"
    )
    @GetMapping("/all")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(Pageable pageable) {
        Page<TransactionResponse> response = transactionService.getTransactions(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update transaction",
            description = "Updates an existing transaction."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Transaction updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid transaction data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transaction not found"
            )
    })
    @PatchMapping("/{txnId}/status")
    public ResponseEntity<TransactionResponse> updateTransaction
            (@PathVariable("txnId") String id, @RequestBody @Valid TransactionStatusUpdateRequest request) {
        TransactionResponse response = transactionService.updateTransactionStatus(id, request.status());
        return ResponseEntity.ok(response);
    }
}