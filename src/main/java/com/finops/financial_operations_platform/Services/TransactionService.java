package com.finops.financial_operations_platform.Services;

import com.finops.financial_operations_platform.Dtos.CreateTransactionRequest;
import com.finops.financial_operations_platform.Dtos.IdempotencyResult;
import com.finops.financial_operations_platform.Dtos.TransactionResponse;
import com.finops.financial_operations_platform.Exceptions.*;
import com.finops.financial_operations_platform.audit.service.AuditLogService;
import com.finops.financial_operations_platform.businesslogics.TransactionStateMachine;
import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.enums.TransactionStatus;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import com.finops.financial_operations_platform.rules.RuleEngine;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.security.userDetails.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionStateMachine stateMachine;
    private final AuditLogService auditLogService;
    private final IdempotencyService idempotencyService;
    private final RequestFingerprintService fingerprintService;
    private final RuleEngine ruleEngine;

    public TransactionService(TransactionRepository transactionRepository, TransactionStateMachine stateMachine,
                              AuditLogService auditLogService, IdempotencyService idempotencyService,
                              RequestFingerprintService fingerprintService, RuleEngine ruleEngine) {
        this.transactionRepository = transactionRepository;
        this.stateMachine = stateMachine;
        this.auditLogService = auditLogService;
        this.idempotencyService = idempotencyService;
        this.fingerprintService = fingerprintService;
        this.ruleEngine = ruleEngine;
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

    private String getCustomerId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new UnauthorizedException("User is not authenticated");
        }

        return userDetails.getCustomerId();
    }

    private String getActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new UnauthorizedException("Unauthorized user!");
        }

        return userDetails.getAuthorities() + userDetails.getUsername();
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest req, String key) {

        String fingerprint = fingerprintService.generate(req);
        IdempotencyResult result = idempotencyService.claim(key, fingerprint);

        switch (result.claimResult()){
            case ACQUIRED -> {
                Transaction tx = new Transaction();

                tx.setCustomerId(getCustomerId());
                tx.setAmount(req.amount());
                tx.setProvider(req.provider());
                tx.setProviderTransactionId(req.providerTransactionId());
                tx.setCurrency(req.currency().toUpperCase());

                tx.setTransactionId("TXN-" + UUID.randomUUID());
                tx.setStatus(TransactionStatus.INITIATED);

                TransactionContext context = new TransactionContext(
                        tx.getTransactionId(),
                        tx.getProviderTransactionId(),
                        tx.getCustomerId(),
                        tx.getAmount(),
                        tx.getCurrency(),
                        tx.getProvider(),
                        Instant.now()
                );

                List<RuleResult> ruleResult = ruleEngine.evaluate(context);

                for(RuleResult ruleResult1: ruleResult) {
                    if (ruleResult1.decision() == RuleDecision.REJECT) {
                        throw new TransactionRuleRejectedException("Transaction rejected by business rule: "
                                + ruleResult1.ruleCode());
                    }
                }

                Transaction saved_tx = transactionRepository.save(tx);
                auditLogService.recordAudit(saved_tx.getTransactionId(), null,
                        TransactionStatus.INITIATED, getActor(),
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

            case CONFLICT -> throw new IdempotencyConflictException(
                    "Idempotency key has already been used for a different request.");
        }

        throw new IdempotencyStateException("Unexpected idempotency decision");
    }

    public TransactionResponse getTransaction() {

        String custId = getCustomerId();

        Transaction tx = transactionRepository.findByCustomerId(custId)
                .orElseThrow(() -> new TransactionNotFoundException("Np Transaction found"));

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

        auditLogService.recordAudit(tx.getTransactionId(), oldStatus, requestedStatus, getActor(),
                "Transaction status updated from: " + oldStatus + "to: " + requestedStatus);

        return mapToResponse(tx);
    }

}
