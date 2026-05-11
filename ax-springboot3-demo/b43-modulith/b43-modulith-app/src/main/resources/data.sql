-- Spring Modulith 演示数据初始化脚本

-- 插入测试客户
INSERT INTO customer (id, first_name, last_name, email, phone, province, city, district, street, detail, zip_code, status, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440000', '张', '三', 'zhangsan@example.com', '13800138000', '北京市', '北京市', '海淀区', '中关村大街', '1号楼101室', '100000', 'ACTIVE', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440001', '李', '四', 'lisi@example.com', '13800138001', '上海市', '上海市', '浦东新区', '世纪大道', '2号楼202室', '200000', 'ACTIVE', NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440002', '王', '五', 'wangwu@example.com', '13800138002', '广州市', '广州市', '天河区', '天河路', '3号楼303室', '510000', 'PENDING', NOW(), NOW());

-- 插入测试产品库存
INSERT INTO inventory (product_id, total_quantity, available_quantity, reserved_quantity, safety_stock_level, last_updated)
VALUES 
    ('PROD-001', 100, 95, 5, 10, NOW()),
    ('PROD-002', 200, 200, 0, 20, NOW()),
    ('PROD-003', 50, 45, 5, 5, NOW());
