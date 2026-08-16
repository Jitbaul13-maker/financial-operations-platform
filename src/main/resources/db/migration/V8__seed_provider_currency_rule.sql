INSERT INTO business_rule(
    rule_code,
    enabled,
    configuration,
    updated_at
) values (
    'PROVIDER_RULE',
    TRUE,
    '{"supportedCurrencies":{"RAZORPAY":["INR"],"WALLET":["INR"],"PAYPAL":["USD"]}}'::jsonb,
    NOW()
);