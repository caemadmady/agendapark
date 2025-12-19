-- V2: Insert initial super admin user using Flyway placeholders

INSERT INTO users (name, lastname, username, password, email, user_status, user_role)
VALUES (
    '${SUPER_ADMIN_NAME}',
    '${SUPER_ADMIN_LASTNAME}',
    '${SUPER_ADMIN_USERNAME}',
    '${SUPER_ADMIN_PASSWORD}',
    '${SUPER_ADMIN_EMAIL}',
    'ACTIVO',
    'SUPERADMIN'
);
