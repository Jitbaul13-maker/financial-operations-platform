package com.finops.financial_operations_platform.externalLedger;

import org.springframework.stereotype.Component;

@Component
public class ProviderStatusNormalizer {

    public String normalize(String provider, String status) {

        return switch (provider.toUpperCase()) {

            case "RAZORPAY" -> normalizeRazorpay(status);

            case "PAYPAL" -> normalizePaypal(status);

            case "WALLET" -> normalizeWallet(status);

            default -> throw new IllegalArgumentException(
                    "Unsupported provider: " + provider
            );
        };
    }

    private String normalizeRazorpay(String status) {
        return switch (status.toUpperCase()) {
            case "SUCCESS" -> "COMPLETED";
            case "FAILED" -> "FAILED";
            case "PENDING" -> "PROCESSING";
            default -> throw new IllegalArgumentException(
                    "Unknown Razorpay status: " + status
            );
        };
    }

    private String normalizePaypal(String status) {
        return switch (status.toUpperCase()) {
            case "COMPLETED" -> "COMPLETED";
            case "DENIED" -> "FAILED";
            case "PENDING" -> "PROCESSING";
            default -> throw new IllegalArgumentException(
                    "Unknown PayPal status: " + status
            );
        };
    }

    private String normalizeWallet(String status) {
        return switch (status.toUpperCase()) {
            case "SETTLED" -> "COMPLETED";
            case "REJECTED" -> "FAILED";
            case "PROCESSING" -> "PROCESSING";
            default -> throw new IllegalArgumentException(
                    "Unknown wallet status: " + status
            );
        };
    }
}
