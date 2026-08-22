package com.finops.financial_operations_platform.externalLedger;

import com.finops.financial_operations_platform.Dtos.ProviderStatementRow;
import com.finops.financial_operations_platform.models.ProviderTransaction;
import com.finops.financial_operations_platform.repos.ProviderTransactionRepository;
import com.finops.financial_operations_platform.validations.ProviderStatementValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProviderStatementImportService {
    private final ProviderStatementParser parser;
    private final ProviderStatementValidator validator;
    private final ProviderStatusNormalizer normalizer;
    private final ProviderTransactionRepository repository;

    public ProviderStatementImportService(ProviderStatementParser parser, ProviderStatementValidator validator,
                                          ProviderStatusNormalizer normalizer, ProviderTransactionRepository repository) {
        this.parser = parser;
        this.validator = validator;
        this.normalizer = normalizer;
        this.repository = repository;
    }

    @Transactional
    public void createProviderTransaction(InputStream stream) throws IOException {
        List<ProviderStatementRow> rows = parser.parse(stream);

        for (ProviderStatementRow row : rows) {
            validator.validate(row);
        }

        List<ProviderTransaction> transactions = new ArrayList<>();

        for (ProviderStatementRow row : rows) {
            ProviderTransaction providerTransaction = new ProviderTransaction();

            providerTransaction.setProvider(row.provider());
            providerTransaction.setProviderTransactionId(row.providerTransactionId());
            providerTransaction.setAmount(row.amount());
            providerTransaction.setCurrency(row.currency());
            providerTransaction.setStatus(normalizer.normalize(row.provider(), row.status()));
            providerTransaction.setCreatedAt(row.createdAt());

            transactions.add(providerTransaction);
        }

        repository.saveAll(transactions);
    }
}
