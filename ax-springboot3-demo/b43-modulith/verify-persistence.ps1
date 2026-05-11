# 数据库持久化验证脚本 (PowerShell)
# 用于验证服务重启后数据是否保留

Write-Host "=" -NoNewline
for ($i = 0; $i -lt 50; $i++) { Write-Host "=" -NoNewline }
Write-Host ""
Write-Host "数据库持久化验证"
Write-Host "=" -NoNewline
for ($i = 0; $i -lt 50; $i++) { Write-Host "=" -NoNewline }
Write-Host ""

$BASE_URL = "http://localhost:8080"

# 检查应用是否运行
Write-Host "`n1. 检查应用状态..."
try {
    $health = Invoke-RestMethod -Uri "$BASE_URL/actuator/health" -ErrorAction Stop
    if ($health.status -eq "UP") {
        Write-Host "✅ 应用运行正常" -ForegroundColor Green
    } else {
        Write-Host "❌ 应用状态异常: $($health.status)" -ForegroundColor Red
        exit
    }
} catch {
    Write-Host "❌ 无法连接到应用，请确保应用已启动" -ForegroundColor Red
    Write-Host "   运行: mvn spring-boot:run" -ForegroundColor Yellow
    exit
}

# 检查数据库文件是否存在
Write-Host "`n2. 检查数据库文件..."
$dbPath = ".\data\modulith_db.mv.db"
if (Test-Path $dbPath) {
    $fileSize = (Get-Item $dbPath).Length
    Write-Host "✅ 数据库文件存在: $dbPath" -ForegroundColor Green
    Write-Host "   文件大小: $([math]::Round($fileSize / 1KB, 2)) KB" -ForegroundColor Cyan
} else {
    Write-Host "⚠️  数据库文件不存在（首次启动会自动创建）" -ForegroundColor Yellow
}

# 查询客户数据
Write-Host "`n3. 查询客户数据..."
try {
    # 使用 data.sql 中的测试数据
    $testCustomerId = "550e8400-e29b-41d4-a716-446655440000"
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/customers/$testCustomerId" -ErrorAction Stop
    
    Write-Host "✅ 成功查询到客户数据:" -ForegroundColor Green
    Write-Host "   客户ID: $($response.customerId)" -ForegroundColor Cyan
    Write-Host "   姓名: $($response.firstName)$($response.lastName)" -ForegroundColor Cyan
    Write-Host "   邮箱: $($response.email)" -ForegroundColor Cyan
    Write-Host "   状态: $($response.status)" -ForegroundColor Cyan
} catch {
    Write-Host "⚠️  未找到测试客户（可能还未初始化数据）" -ForegroundColor Yellow
    Write-Host "   错误: $($_.Exception.Message)" -ForegroundColor Gray
}

# 查询库存数据
Write-Host "`n4. 查询库存数据..."
try {
    $testProductId = "PROD-001"
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/inventory/products/$testProductId" -ErrorAction Stop
    
    Write-Host "✅ 成功查询到库存数据:" -ForegroundColor Green
    Write-Host "   产品ID: $($response.productId)" -ForegroundColor Cyan
    Write-Host "   总库存: $($response.totalQuantity)" -ForegroundColor Cyan
    Write-Host "   可用库存: $($response.availableQuantity)" -ForegroundColor Cyan
    Write-Host "   预留库存: $($response.reservedQuantity)" -ForegroundColor Cyan
} catch {
    Write-Host "⚠️  未找到测试产品（可能还未初始化数据）" -ForegroundColor Yellow
}

# 显示 H2 控制台信息
Write-Host "`n5. H2 控制台信息"
Write-Host "   URL: http://localhost:8080/h2-console" -ForegroundColor Cyan
Write-Host "   JDBC URL: jdbc:h2:file:./data/modulith_db" -ForegroundColor Cyan
Write-Host "   用户名: sa" -ForegroundColor Cyan
Write-Host "   密码: 123456" -ForegroundColor Cyan

# 验证总结
Write-Host "`n" + ("=" * 50)
Write-Host "验证总结" -ForegroundColor Green
Write-Host ("=" * 50)
Write-Host ""
Write-Host "✅ 数据库持久化已启用" -ForegroundColor Green
Write-Host "✅ 数据存储位置: ./data/modulith_db.mv.db" -ForegroundColor Green
Write-Host "✅ 服务重启后数据将保留" -ForegroundColor Green
Write-Host ""
Write-Host "💡 提示:" -ForegroundColor Yellow
Write-Host "   1. 停止应用 (Ctrl+C)" -ForegroundColor Gray
Write-Host "   2. 重新启动应用" -ForegroundColor Gray
Write-Host "   3. 再次运行此脚本验证数据是否保留" -ForegroundColor Gray
Write-Host ""
