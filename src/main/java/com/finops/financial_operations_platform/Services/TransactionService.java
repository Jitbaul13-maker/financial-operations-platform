package com.finops.financial_operations_platform.Services;

import com.finops.financial_operations_platform.Dtos.CreateTransactionRequest;
import com.finops.financial_operations_platform.Dtos.TransactionResponse;
import com.finops.financial_operations_platform.Exceptions.TransactionNotFoundException;
import com.finops.financial_operations_platform.enums.TransactionStatus;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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

    public TransactionResponse createTransaction(CreateTransactionRequest req) {
        Transaction tx = new Transaction();

        tx.setCustomerId(req.getCustomerId());
        tx.setAmount(req.getAmount());
        tx.setProvider(req.getProvider());
        tx.setCurrency(req.getCurrency().toUpperCase());

        tx.setTransactionId("TXN-" + UUID.randomUUID());
        tx.setStatus(TransactionStatus.INITIATED);

        Transaction saved_tx = transactionRepository.save(tx);

        return mapToResponse(saved_tx);
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
}
