CREATE TABLE reconciliation_run(
    id BIGSERIAL PRIMARY KEY,
    provider VARCHAR(25) NOT NULL,
    business_date DATE NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE NOT NULL,
    completed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(25) NOT NULL,
    total_records INTEGER NOT NULL,
    matched_count INTEGER NOT NULL,
    discrepancy_count INTEGER NOT NULL
);