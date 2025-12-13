-- 插入用户
INSERT INTO sys_users (username, password, email, real_name, department, position)
VALUES ('admin', '123456', 'admin@example.com', '系统管理员',
        'IT部', '系统管理员'),
       ('sales_director', '123456', 'sales@example.com',
        '销售总监', '销售部', '销售总监'),
       ('order_manager', '123456', 'order@example.com',
        '订单经理', '订单部', '订单经理'),
       ('warehouse_manager', '123456', 'warehouse@example.com',
        '仓库经理', '仓储部', '仓库经理'),
       ('employee1', '123456', 'emp1@example.com', '员工1',
        '销售部', '销售专员');

-- 插入角色
INSERT INTO sys_roles (name, code, description)
VALUES ('系统管理员', 'ROLE_ADMIN', '系统最高权限管理员'),
       ('销售总监', 'ROLE_SALES_DIRECTOR', '销售部门领导，负责销售管理'),
       ('订单管理员', 'ROLE_ORDER_MANAGER', '订单处理和管理'),
       ('仓库管理员', 'ROLE_WAREHOUSE_MANAGER', '仓库管理和库存控制'),
       ('销售专员', 'ROLE_SALESPERSON', '销售业务处理');

-- 插入API
-- 订单相关API
INSERT INTO sys_apis (path, method, service_name, description, category)
VALUES ('/api/orders', 'GET', 'order-service', '查询订单列表', '订单管理'),
       ('/api/orders/{id}', 'GET', 'order-service', '查询单个订单详情', '订单管理'),
       ('/api/orders', 'POST', 'order-service', '创建新订单', '订单管理'),
       ('/api/orders/{id}', 'PUT', 'order-service', '更新订单', '订单管理'),
       ('/api/orders/{id}', 'DELETE', 'order-service', '删除订单', '订单管理'),
       ('/api/orders/approve', 'POST', 'order-service', '审批订单', '订单管理');

-- 销售相关API
INSERT INTO sys_apis (path, method, service_name, description, category)
VALUES ('/api/sales/reports', 'GET', 'sales-service', '销售报表', '销售管理'),
       ('/api/sales/data', 'GET', 'sales-service', '销售数据查询', '销售管理'),
       ('/api/sales/targets', 'GET', 'sales-service', '销售目标查询', '销售管理'),
       ('/api/sales/targets', 'POST', 'sales-service', '设置销售目标', '销售管理'),
       ('/api/customers', 'GET', 'customer-service', '客户信息查询', '客户管理'),
       ('/api/customers/assign', 'POST', 'customer-service', '客户分配', '客户管理'),
       ('/api/customers/{id}', 'PUT', 'customer-service', '更新客户信息', '客户管理');

-- 仓库相关API
INSERT INTO sys_apis (path, method, service_name, description, category)
VALUES ('/api/warehouses', 'GET', 'warehouse-service', '查询仓库列表', '仓库管理'),
       ('/api/warehouses/{id}', 'GET', 'warehouse-service', '查询单个仓库', '仓库管理'),
       ('/api/warehouses', 'POST', 'warehouse-service', '创建仓库', '仓库管理'),
       ('/api/warehouses/{id}', 'PUT', 'warehouse-service', '更新仓库', '仓库管理'),
       ('/api/warehouses/{id}', 'DELETE', 'warehouse-service', '删除仓库', '仓库管理'),
       ('/api/stocks', 'GET', 'warehouse-service', '查询库存', '库存管理'),
       ('/api/stocks/adjust', 'POST', 'warehouse-service', '库存调整', '库存管理');

-- 系统相关API
INSERT INTO sys_apis (path, method, service_name, description, category)
VALUES ('/api/users', 'GET', 'system-service', '查询用户列表', '系统管理'),
       ('/api/roles', 'GET', 'system-service', '查询角色列表', '系统管理'),
       ('/api/permissions', 'GET', 'system-service', '查询权限列表', '系统管理'),
       ('/api/apis', 'GET', 'system-service', '查询API列表', '系统管理');

-- 插入权限
-- 系统管理权限
INSERT INTO sys_permissions (name, code, description, category)
VALUES ('用户管理', 'user:manage', '允许管理用户', '系统权限'),
       ('角色管理', 'role:manage', '允许管理角色', '系统权限'),
       ('权限管理', 'permission:manage', '允许管理权限', '系统权限'),
       ('API管理', 'api:manage', '允许管理系统API', '系统权限');

-- 订单管理权限
INSERT INTO sys_permissions (name, code, description, category)
VALUES ('订单查看', 'order:read', '允许查看订单', '订单权限'),
       ('订单创建', 'order:create', '允许创建订单', '订单权限'),
       ('订单更新', 'order:update', '允许更新订单', '订单权限'),
       ('订单删除', 'order:delete', '允许删除订单', '订单权限'),
       ('订单审批', 'order:approve', '允许审批订单', '订单权限');

-- 销售管理权限
INSERT INTO sys_permissions (name, code, description, category)
VALUES ('销售数据查看', 'sales:data:read', '允许查看销售数据', '销售权限'),
       ('销售报表查看', 'sales:report:read', '允许查看销售报表', '销售权限'),
       ('销售目标设置', 'sales:target:set', '允许设置销售目标', '销售权限'),
       ('销售订单管理', 'sales:order:manage', '允许管理销售订单', '销售权限'),
       ('销售订单审批', 'sales:order:approve', '允许审批销售订单', '销售权限'),
       ('团队业绩查看', 'team:performance:read', '允许查看团队业绩', '销售权限'),
       ('客户信息查看', 'customer:info:read', '允许查看客户信息', '客户权限'),
       ('客户分配', 'customer:assign', '允许分配客户', '客户权限');

-- 仓库管理权限
INSERT INTO sys_permissions (name, code, description, category)
VALUES ('仓库查看', 'warehouse:read', '允许查看仓库信息', '仓库权限'),
       ('仓库管理', 'warehouse:manage', '允许管理仓库', '仓库权限'),
       ('库存查看', 'stock:read', '允许查看库存', '仓库权限'),
       ('库存管理', 'stock:manage', '允许管理库存', '仓库权限');

-- 建立权限-API关联
-- 订单权限关联
INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'order:read'
  AND a.path IN ('/api/orders', '/api/orders/{id}')
  AND a.method = 'GET';

INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'order:create'
  AND a.path = '/api/orders'
  AND a.method = 'POST';

INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'order:update'
  AND a.path = '/api/orders/{id}'
  AND a.method = 'PUT';

INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'order:delete'
  AND a.path = '/api/orders/{id}'
  AND a.method = 'DELETE';

INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'order:approve'
  AND a.path = '/api/orders/approve'
  AND a.method = 'POST';

-- 销售权限关联
INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'sales:data:read'
  AND a.path = '/api/sales/data'
  AND a.method = 'GET';

INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'sales:report:read'
  AND a.path = '/api/sales/reports'
  AND a.method = 'GET';

INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'sales:target:set'
  AND a.path = '/api/sales/targets'
  AND a.method = 'POST';

INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'customer:info:read'
  AND a.path = '/api/customers'
  AND a.method = 'GET';

INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'customer:assign'
  AND a.path = '/api/customers/assign'
  AND a.method = 'POST';

-- 仓库权限关联
INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'warehouse:read'
  AND a.path IN ('/api/warehouses', '/api/warehouses/{id}')
  AND a.method = 'GET';

INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code = 'stock:read'
  AND a.path = '/api/stocks'
  AND a.method = 'GET';

-- 系统管理权限关联
INSERT INTO sys_permission_apis (permission_id, api_id)
SELECT p.id, a.id
FROM sys_permissions p,
     sys_apis a
WHERE p.code IN ('user:manage', 'role:manage', 'permission:manage', 'api:manage');

-- 建立角色-权限关联
-- 避免重复插入，先插入具体的权限分配，再处理系统管理员的特殊权限
-- 销售总监角色权限
INSERT INTO sys_role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM sys_roles r,
     sys_permissions p
WHERE r.code = 'ROLE_SALES_DIRECTOR'
  AND p.code IN (
                 'sales:data:read',
                 'sales:report:read',
                 'sales:target:set',
                 'customer:info:read',
                 'customer:assign',
                 'team:performance:read',
                 'order:read',
                 'order:approve',
                 'stock:read'
    );

-- 订单管理员角色权限
INSERT INTO sys_role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM sys_roles r,
     sys_permissions p
WHERE r.code = 'ROLE_ORDER_MANAGER'
  AND p.code LIKE 'order:%';

-- 仓库管理员角色权限
INSERT INTO sys_role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM sys_roles r,
     sys_permissions p
WHERE r.code = 'ROLE_WAREHOUSE_MANAGER'
  AND (p.code LIKE 'warehouse:%' OR p.code LIKE 'stock:%');

-- 销售专员角色权限
INSERT INTO sys_role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM sys_roles r,
     sys_permissions p
WHERE r.code = 'ROLE_SALESPERSON'
  AND p.code IN (
                 'sales:data:read',
                 'customer:info:read',
                 'order:create',
                 'order:read'
    );

-- 系统管理员拥有所有权限
INSERT INTO sys_role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM sys_roles r,
     sys_permissions p
WHERE r.code = 'ROLE_ADMIN'
  AND NOT EXISTS (SELECT 1
                  FROM sys_role_permissions rp
                  WHERE rp.role_id = r.id
                    AND rp.permission_id = p.id
                    AND rp.deleted = 0);
-- 注意：这里使用NOT EXISTS避免重复插入已分配的权限

-- 建立用户-角色关联
-- 系统管理员
INSERT INTO sys_user_roles (user_id, role_id)
SELECT u.id, r.id
FROM sys_users u,
     sys_roles r
WHERE u.username = 'admin'
  AND r.code = 'ROLE_ADMIN';

-- 销售总监
INSERT INTO sys_user_roles (user_id, role_id)
SELECT u.id, r.id
FROM sys_users u,
     sys_roles r
WHERE u.username = 'sales_director'
  AND r.code = 'ROLE_SALES_DIRECTOR';

-- 订单经理
INSERT INTO sys_user_roles (user_id, role_id)
SELECT u.id, r.id
FROM sys_users u,
     sys_roles r
WHERE u.username = 'order_manager'
  AND r.code = 'ROLE_ORDER_MANAGER';

-- 仓库经理
INSERT INTO sys_user_roles (user_id, role_id)
SELECT u.id, r.id
FROM sys_users u,
     sys_roles r
WHERE u.username = 'warehouse_manager'
  AND r.code = 'ROLE_WAREHOUSE_MANAGER';

-- 员工1（销售专员）
INSERT INTO sys_user_roles (user_id, role_id)
SELECT u.id, r.id
FROM sys_users u,
     sys_roles r
WHERE u.username = 'employee1'
  AND r.code = 'ROLE_SALESPERSON';
