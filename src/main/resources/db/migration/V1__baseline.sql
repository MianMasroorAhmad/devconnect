-- Baseline schema for DevConnect

CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  bio VARCHAR(255),
  profile_picture_url VARCHAR(512),
  role VARCHAR(32) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_user_email ON users(email);

CREATE TABLE posts (
  id BIGSERIAL PRIMARY KEY,
  content TEXT,
  image_url VARCHAR(512),
  visibility VARCHAR(50) NOT NULL,
  deleted BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE comments (
  id BIGSERIAL PRIMARY KEY,
  text TEXT,
  deleted BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
  post_id BIGINT REFERENCES posts(id) ON DELETE CASCADE,
  parent_comment_id BIGINT REFERENCES comments(id) ON DELETE SET NULL
);

-- Optionally add likes/follows tables if they exist in code (not included here)
