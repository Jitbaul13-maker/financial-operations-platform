CREATE TABLE business_rule(
    id BIGSERIAL PRIMARY KEY,
    rule_code VARCHAR(25) NOT NULL,
    enabled BOOLEAN DEFAULT FALSE,
    configuration JSONB,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT uk_rule_code UNIQUE(rule_code)
);