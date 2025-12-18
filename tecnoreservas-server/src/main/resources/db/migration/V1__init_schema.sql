-- V1: Initialize Full Schema based on JPA Entities

-- Drop existing types and tables if they exist, to ensure a clean slate.
-- This is useful for development but should be used with caution.
DROP TABLE IF EXISTS talent_project_lines CASCADE;
DROP TABLE IF EXISTS sessions_logs CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS digital_records CASCADE;
DROP TABLE IF EXISTS reservations_resources CASCADE;
DROP TABLE IF EXISTS equipment_history CASCADE;
DROP TABLE IF EXISTS biotechnology_resource CASCADE;
DROP TABLE IF EXISTS generic_resource CASCADE;
DROP TABLE IF EXISTS resources CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS experts CASCADE;
DROP TABLE IF EXISTS talent_project_details CASCADE;
DROP TABLE IF EXISTS talents CASCADE;
DROP TABLE IF EXISTS service_lines CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP TYPE IF EXISTS user_role;
DROP TYPE IF EXISTS history_event_type;
DROP TYPE IF EXISTS project_line;
DROP TYPE IF EXISTS reservation_status;
DROP TYPE IF EXISTS resource_status;
DROP TYPE IF EXISTS user_status;

-- Create Tables
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    lastname VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    email VARCHAR(255),
    user_status VARCHAR(100) NOT NULL,
    user_role VARCHAR(100) NOT NULL
);

CREATE TABLE service_lines (
    id BIGSERIAL PRIMARY KEY,
    service_line_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE talents (
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_talents_users FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE talent_project_details (
    id BIGSERIAL PRIMARY KEY,
    associated_project VARCHAR(255) UNIQUE,
    project_phase VARCHAR(50),
    name_trl VARCHAR(20),
    talent_id BIGINT NOT NULL,
    service_line_id BIGINT NOT NULL,
    CONSTRAINT fk_talent_project_details_talent FOREIGN KEY (talent_id) REFERENCES talents(id) ON DELETE CASCADE,
    CONSTRAINT fk_talent_project_details_service_line FOREIGN KEY (service_line_id) REFERENCES service_lines(id)
);

CREATE TABLE experts (
    id BIGINT PRIMARY KEY,
    service_line_id BIGINT NOT NULL,
    CONSTRAINT fk_experts_users FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_experts_service_lines FOREIGN KEY (service_line_id) REFERENCES service_lines(id)
);

CREATE TABLE resources (
    id BIGSERIAL PRIMARY KEY,
    resource_type VARCHAR(31) NOT NULL, -- Discriminator column
    name VARCHAR(255) NOT NULL,
    description TEXT,
    plate VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    status VARCHAR(100) NOT NULL,
    model VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    service_line_id BIGINT NOT NULL,
    CONSTRAINT fk_resources_service_lines FOREIGN KEY (service_line_id) REFERENCES service_lines(id)
);

CREATE TABLE biotechnology_resource (
    id BIGINT PRIMARY KEY,
    max_usuarios_simultaneos INT NOT NULL,
    condiciones_de_uso JSONB,
    CONSTRAINT fk_biotechnology_resource_resources FOREIGN KEY (id) REFERENCES resources(id) ON DELETE CASCADE
);

CREATE TABLE generic_resource (
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_generic_resource_resources FOREIGN KEY (id) REFERENCES resources(id) ON DELETE CASCADE
);

CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    date_time_start TIMESTAMP,
    end_date_time TIMESTAMP,
    reservation_status VARCHAR(100) NOT NULL,
    creation_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    expert_id BIGINT NOT NULL,
    talent_id BIGINT NOT NULL,
    CONSTRAINT fk_reservations_experts FOREIGN KEY (expert_id) REFERENCES experts(id),
    CONSTRAINT fk_reservations_talents FOREIGN KEY (talent_id) REFERENCES talents(id)
);

CREATE TABLE equipment_history (
    id BIGSERIAL PRIMARY KEY,
    event_date TIMESTAMP NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    details VARCHAR(1000),
    resource_id BIGINT NOT NULL,
    CONSTRAINT fk_equipment_history_resources FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE
);

CREATE TABLE reservations_resources (
    id BIGSERIAL PRIMARY KEY,
    resource_id BIGINT NOT NULL,
    reservation_id BIGINT NOT NULL,
    CONSTRAINT fk_reservations_resources_resources FOREIGN KEY (resource_id) REFERENCES resources(id),
    CONSTRAINT fk_reservations_resources_reservations FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

CREATE TABLE digital_records (
    id BIGSERIAL PRIMARY KEY,
    record_type VARCHAR(255),
    creation_date TIMESTAMP,
    damage_control VARCHAR(255),
    observations VARCHAR(255),
    expert_id BIGINT NOT NULL,
    talent_id BIGINT NOT NULL,
    reservation_id BIGINT NOT NULL,
    CONSTRAINT fk_digital_records_experts FOREIGN KEY (expert_id) REFERENCES experts(id),
    CONSTRAINT fk_digital_records_talents FOREIGN KEY (talent_id) REFERENCES talents(id),
    CONSTRAINT fk_digital_records_reservations FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

-- Recreate the notifications table with the updated structure
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    message VARCHAR(255),
    notification_type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    sent_at TIMESTAMP,
    user_id BIGINT NOT NULL,
    reservation_id BIGINT NOT NULL,
    CONSTRAINT fk_notifications_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_notifications_reservations FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

CREATE TABLE sessions_logs (
    id BIGSERIAL PRIMARY KEY,
    login_at TIMESTAMP NOT NULL,
    ip_address VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_sessions_logs_users FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Índices adicionales para mejorar el rendimiento
CREATE INDEX idx_talent_project_details_talent ON talent_project_details(talent_id);
CREATE INDEX idx_talent_project_details_service_line ON talent_project_details(service_line_id);