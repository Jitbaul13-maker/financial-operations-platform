package com.finops.financial_operations_platform.Services;

import com.finops.financial_operations_platform.Dtos.ProviderStatementRow;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProviderStatementParser {
    public List<ProviderStatementRow> parse(InputStream inputStream) throws IOException {
        try(
                Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                CSVParser parser  = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get().parse(reader);
                ) {

            List<ProviderStatementRow> rows = new ArrayList<>();

            for(CSVRecord record : parser) {
                ProviderStatementRow row = new ProviderStatementRow(
                        record.get("provider"),
                        record.get("providerTransactionId"),
                        new BigDecimal(record.get("amount")),
                        record.get("currency"),
                        record.get("status"),
                        OffsetDateTime.parse(record.get("createdAt"))
                );
                rows.add(row);
            }
            return rows;
        }
    }
}
