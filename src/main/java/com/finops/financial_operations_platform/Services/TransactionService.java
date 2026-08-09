package com.finops.financial_operations_platform.Services;

import com.finops.financial_operations_platform.Dtos.CreateTransactionRequest;
import com.finops.financial_operations_platform.Dtos.IdempotencyResult;
import com.finops.financial_operations_platform.Dtos.TransactionResponse;
import com.finops.financial_operations_platform.Exceptions.IdempotencyInProgressException;
import com.finops.financial_operations_platform.Exceptions.IdempotencyStateException;
import com.finops.financial_operations_platform.Exceptions.TransactionNotFoundException;
import com.finops.financial_operations_platform.businesslogics.TransactionStateMachine;
import com.finops.financial_operations_platform.enums.TransactionStatus;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionStateMachine stateMachine;
    private final AuditLogService auditLogService;
    private final IdempotencyService idempotencyService;
    private final RequestFingerprintService fingerprintService;

    public TransactionService(TransactionRepository transactionRepository,
                              TransactionStateMachine stateMachine,
                              AuditLogService auditLogService,
                              IdempotencyService idempotencyService,
                              RequestFingerprintService fingerprintService) {
        this.transactionRepository = transactionRepository;
        this.stateMachine = stateMachine;
        this.auditLogService = auditLogService;
        this.idempotencyService = idempotencyService;
        this.fingerprintService = fingerprintService;
    }

    private TransactionResponse mapToResponse(Transaction tx) {
        return new TransactionResponse(
                tx.getCustomerId(),
                tx.getTransactionId(),
                tx.getProvider(),
                tx.getAmount(),
                tx.getCurrency(),
                tx.getStatus(),
                tx.getCreatedAt(),
                tx.getUpdatedAt()
        );
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest req, String key) {

        String fingerprint = fingerprintService.generate(req);
        IdempotencyResult result = idempotencyService.claim(key, fingerprint);

        switch (result.claimResult()){
            case ACQUIRED -> {
                Transaction tx = new Transaction();

                tx.setCustomerId(req.customerId());
                tx.setAmount(req.amount());
                tx.setProvider(req.provider());
                tx.setCurrency(req.currency().toUpperCase());

                tx.setTransactionId("TXN-" + UUID.randomUUID());
                tx.setStatus(TransactionStatus.INITIATED);

                Transaction saved_tx = transactionRepository.save(tx);
                auditLogService.recordAudit(saved_tx.getTransactionId(), null,
                        TransactionStatus.INITIATED, "SYSTEM",
                        "Initial Transaction creation");

                idempotencyService.complete(key, saved_tx.getTransactionId());

                return mapToResponse(saved_tx);
            }

            case COMPLETED -> {

                String id = result.record().transactionId();

                Transaction txn = transactionRepository.findByTransactionId(id)
                        .orElseThrow(() -> new TransactionNotFoundException("Transaction not found:" + id));

                return mapToResponse(txn);
            }

            case IN_PROGRESS -> throw new IdempotencyInProgressException(
                    "Transaction with this idempotency key is already in progress");

            case CONFLICT -> throw new IdempotencyInProgressException(
                    "Idempotency key has already been used for a different request.");
        }

        throw new IdempotencyStateException("Unexpected idempotency decision");
    }

    public TransactionResponse getTransaction(String txId) {
        Transaction tx = transactionRepository.findByTransactionId(txId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found:" + txId));

        return mapToResponse(tx);
    }

    public Page<TransactionResponse> getTransactions(Pageable pageable){
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        return transactions.map(this::mapToResponse);
    }

    @Transactional
    public TransactionResponse updateTransactionStatus(String transactionId, TransactionStatus requestedStatus) {
        Transaction tx = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("No valid transaction found"));

        stateMachine.validateTransition(tx.getStatus(), requestedStatus);

        TransactionStatus oldStatus = tx.getStatus();
        tx.setStatus(requestedStatus);

        auditLogService.recordAudit(tx.getTransactionId(), oldStatus, requestedStatus, "SYSTEM",
                "Transaction status updated");

        return mapToResponse(tx);
    }

}
