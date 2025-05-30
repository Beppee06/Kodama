CREATE TABLE users (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   username VARCHAR(50) NOT NULL UNIQUE,
   email VARCHAR(100) NOT NULL UNIQUE,
   password_hash VARCHAR(255) NOT NULL,
   is_active BOOLEAN NOT NULL DEFAULT TRUE,
   INDEX idx_username (username),
   INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;