CREATE TABLE reconciliation_result(
    id BIGSERIAL PRIMARY KEY,
    run_id BIGSERIAL,
    internal_transaction_id VARCHAR(100),
    provider_transaction_id VARCHAR(50),
    result_type VARCHAR(30) NOT NULL,
    severity VARCHAR(10) NOT NULL,
    remarks VARCHAR(255)
);