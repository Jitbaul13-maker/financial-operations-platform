ALTER TABLE reconciliation_run
ADD CONSTRAINT uk_provider_business_date UNIQUE(provider, business_date);