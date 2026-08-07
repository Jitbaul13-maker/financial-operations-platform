CREATE TABLE audit_log(
    id BIGSERIAL PRIMARY KEY,

    transaction_id VARCHAR(255) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    old_status VARCHAR(15) NOT NULL,
    new_status VARCHAR(15) NOT NULL,

    actor VARCHAR(100) NOT NULL,
    reason VARCHAR(255) NOT NULL,

    CONSTRAINT fk_audit_transaction FOREIGN KEY (transaction_id)
    REFERENCES transactions(transaction_id)
);

CREATE INDEX idx_audit_transaction ON audit_log(transaction_id);