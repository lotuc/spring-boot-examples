CREATE SCHEMA demo;

CREATE TABLE user (
id VARCHAR(40) PRIMARY KEY,
name VARCHAR(200) NOT NULL UNIQUE,
password VARCHAR(128)
);