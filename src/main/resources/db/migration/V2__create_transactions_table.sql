CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,

    transaction_id VARCHAR(100) NOT NULL,
    idempotency_key VARCHAR(255),

    provider VARCHAR(50) NOT NULL,
    provider_transaction_id VARCHAR(255),

    amount NUMERIC(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,

    status VARCHAR(30) NOT NULL,
    customer_id VARCHAR(100) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT uk_transactions_transaction_id
        UNIQUE (transaction_id)
);