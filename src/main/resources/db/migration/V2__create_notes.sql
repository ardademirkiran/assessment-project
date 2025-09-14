CREATE TABLE IF NOT EXISTS notes (
                                     id SERIAL PRIMARY KEY,
                                     user_id INT NOT NULL REFERENCES api_user(id) ON DELETE CASCADE,
    raw_text TEXT NOT NULL,
    summary TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    started_at TIMESTAMP,
    finished_at TIMESTAMP
    );
