package com.finops.financial_operations_platform.Services;

import com.finops.financial_operations_platform.Dtos.CreateTransactionRequest;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Service
public class RequestFingerprintService {

    public String generate(CreateTransactionRequest request) {

        try {
            String input = request.customerId() + "|"
                    + request.amount() + "|" +
                    request.currency().toUpperCase() + "|"
                    + request.provider();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
