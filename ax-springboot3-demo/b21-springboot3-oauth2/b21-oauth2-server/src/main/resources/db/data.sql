USE ax_oauth2;

-- 初始化用户 (密码: 123456, BCrypt加密)
INSERT IGNORE INTO sys_user (id, username, password, email, phone, real_name, enabled, account_non_expired, account_non_locked, credentials_non_expired)
VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'admin@example.com', '13800138000', '系统管理员', 1, 1, 1, 1),
(2, 'user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'user@example.com', '13800138001', '普通用户', 1, 1, 1, 1);

-- 初始化角色
INSERT IGNORE INTO sys_role (id, role_name, role_code, description, sort_order)
VALUES
(1, '系统管理员', 'ROLE_ADMIN', '拥有系统所有权限', 1),
(2, '普通用户', 'ROLE_USER', '拥有基本操作权限', 2),
(3, '访客', 'ROLE_GUEST', '仅拥有查看权限', 3);

-- 初始化权限
INSERT IGNORE INTO sys_permission (id, permission_name, permission_code, resource_type, resource_url, http_method, description, sort_order)
VALUES
(1, '消息查看', 'message:read', 'api', '/api/messages', 'GET', '查看消息列表和详情', 1),
(2, '消息编辑', 'message:write', 'api', '/api/messages', 'POST', '创建、修改、删除消息', 2),
(3, '用户查看', 'user:read', 'api', '/api/user/**', 'GET', '查看用户信息', 3),
(4, '系统管理', 'system:manage', 'api', '/api/admin/**', '*', '系统管理权限', 4);

-- 用户角色关联
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES (1, 1), (1, 2), (2, 2);

-- 角色权限关联
INSERT IGNORE INTO sys_role_permission (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 1), (2, 3),
(3, 1);

-- 初始化消息
INSERT IGNORE INTO sys_message (id, title, content, message_type, status, author_id)
VALUES
(1, '系统公告', '欢迎使用OAuth2演示系统', 'NORMAL', 'PUBLISHED', 1),
(2, '重要通知', '请妥善保管您的访问令牌', 'URGENT', 'PUBLISHED', 1),
(3, '测试消息', '这是一条测试消息', 'NORMAL', 'DRAFT', 2);
