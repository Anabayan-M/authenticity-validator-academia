CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE documents (
    id SERIAL PRIMARY KEY,
    document_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    hash VARCHAR(255) UNIQUE NOT NULL,
    uploaded_by INT NOT NULL,
    upload_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_documents_users FOREIGN KEY (uploaded_by) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE verification_logs (
    id SERIAL PRIMARY KEY,
    document_id INT,
    checked_hash VARCHAR(255) NOT NULL,
    verification_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    result VARCHAR(255) NOT NULL,
    CONSTRAINT fk_verification_logs_documents FOREIGN KEY (document_id) REFERENCES documents (id) ON DELETE CASCADE
);
