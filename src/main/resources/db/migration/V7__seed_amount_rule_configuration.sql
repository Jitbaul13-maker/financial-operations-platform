INSERT INTO business_rule(
    rule_code,
    enabled,
    configuration,
    updated_at
) values (
    'AMOUNT_RULE',
    TRUE,
    '{"maxAmount": 99999}' :: jsonb,
    NOW()
);