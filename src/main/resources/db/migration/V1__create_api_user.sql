CREATE TABLE IF NOT EXISTS api_user (
                                        id SERIAL PRIMARY KEY,
                                        email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
    );

ALTER TABLE api_user
    ADD CONSTRAINT uq_api_user_email UNIQUE (email);