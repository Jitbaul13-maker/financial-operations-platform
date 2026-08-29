CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    customer_id VARCHAR(100) UNIQUE,

    username VARCHAR(100) NOT NULL UNIQUE,

    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(20) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);