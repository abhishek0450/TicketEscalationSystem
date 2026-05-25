-- =============================================
-- SEED DATA — run manually after first startup
-- =============================================

-- Default roles
INSERT IGNORE INTO roles (name) VALUES ('ROLE_EMPLOYEE');
INSERT IGNORE INTO roles (name) VALUES ('ROLE_SUPPORT_AGENT');
INSERT IGNORE INTO roles (name) VALUES ('ROLE_ADMIN');

-- Default SLA configs
INSERT IGNORE INTO sla_configs (priority, max_resolution_hours, updated_at)
VALUES
  ('LOW',      48, NOW()),
  ('MEDIUM',   24, NOW()),
  ('HIGH',      4, NOW()),
  ('CRITICAL',  1, NOW());

-- Default categories
INSERT IGNORE INTO categories (name, description, is_active, created_at)
VALUES
  ('Hardware',        'Physical device issues',          true, NOW()),
  ('Software',        'Application and OS issues',       true, NOW()),
  ('Network',         'Connectivity and VPN issues',     true, NOW()),
  ('Access Request',  'Login and permission requests',   true, NOW()),
  ('Email',           'Email client and server issues',  true, NOW()),
  ('Other',           'General uncategorized issues',    true, NOW());

-- Default admin user (password = Admin@123 BCrypt encoded)
INSERT IGNORE INTO users (full_name, email, password, department, is_active, created_at, updated_at)
VALUES ('System Admin', 'admin@ticketing.com',
'$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
'IT', true, NOW(), NOW());
