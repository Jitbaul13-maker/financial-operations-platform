INSERT INTO business_rule(
    rule_code,
    enabled,
    configuration,
    updated_at
) values (
    'VELOCITY_RULE',
    TRUE,
    '{"maxTransactions" : 5, "windowMinutes" : 2}' :: jsonb,
    NOW()
)