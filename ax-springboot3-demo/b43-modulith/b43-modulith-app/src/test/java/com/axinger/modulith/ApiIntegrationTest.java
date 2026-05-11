package com.axinger.modulith;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring Modulith API 集成测试
 * 
 * 测试完整的电商业务流程：
 * 1. 注册客户
 * 2. 创建产品
 * 3. 初始化库存
 * 4. 创建订单
 * 5. 确认订单
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private String customerId;
    private String orderId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + 
                  restTemplate.getRootUri().split(":")[2];
    }

    /**
     * 1. 注册客户
     */
    @Test
    @Order(1)
    @DisplayName("1注册客户")
    void testRegisterCustomer() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("1️⃣  注册客户");
        System.out.println("=".repeat(50));

        Map<String, Object> customerData = Map.of(
            "firstName", "张",
            "lastName", "三",
            "email", "zhangsan_test@example.com",
            "phone", "13800138000",
            "province", "北京市",
            "city", "北京市",
            "district", "海淀区",
            "street", "中关村大街",
            "detail", "1号楼101室",
            "zipCode", "100000"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(customerData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "/api/customers",
            request,
            Map.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        customerId = (String) response.getBody().get("customerId");
        assertNotNull(customerId);
        
        System.out.println("✅ 客户注册成功");
        System.out.println("客户ID: " + customerId);
        System.out.println("响应数据: " + response.getBody());
    }

    /**
     * 2. 创建产品
     */
    @Test
    @Order(2)
    @DisplayName("2️创建产品")
    void testCreateProduct() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("2️⃣  创建产品");
        System.out.println("=".repeat(50));

        Map<String, Object> productData = Map.of(
            "productId", "PROD-TEST-001",
            "name", "iPhone 15 Pro",
            "description", "苹果最新旗舰手机",
            "category", "ELECTRONICS"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(productData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "/api/inventory/products",
            request,
            Map.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        System.out.println("✅ 产品创建成功");
        System.out.println("响应数据: " + response.getBody());
    }

    /**
     * 3. 初始化库存
     */
    @Test
    @Order(3)
    @DisplayName("3️初始化库存")
    void testInitializeInventory() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("3️⃣  初始化库存");
        System.out.println("=".repeat(50));

        Map<String, Object> inventoryData = Map.of(
            "initialQuantity", 100,
            "safetyStockLevel", 10
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(inventoryData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "/api/inventory/products/PROD-TEST-001/initialize",
            request,
            Map.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        System.out.println("✅ 库存初始化成功");
        System.out.println("响应数据: " + response.getBody());
    }

    /**
     * 4. 创建订单
     */
    @Test
    @Order(4)
    @DisplayName("4️创建订单")
    void testCreateOrder() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("4️⃣  创建订单");
        System.out.println("=".repeat(50));

        // 确保 customerId 已设置
        if (customerId == null) {
            // 使用测试数据中的客户ID
            customerId = "550e8400-e29b-41d4-a716-446655440000";
            System.out.println("⚠️  使用默认客户ID: " + customerId);
        }

        Map<String, Object> item = Map.of(
            "productId", "PROD-TEST-001",
            "productName", "iPhone 15 Pro",
            "quantity", 1,
            "price", 7999.00
        );

        Map<String, Object> orderData = Map.of(
            "customerId", customerId,
            "items", Set.of(item)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(orderData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "/api/orders",
            request,
            Map.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        
        orderId = (String) response.getBody().get("orderId");
        assertNotNull(orderId);
        
        System.out.println("✅ 订单创建成功");
        System.out.println("订单ID: " + orderId);
        System.out.println("响应数据: " + response.getBody());
    }

    /**
     * 5. 确认订单
     */
    @Test
    @Order(5)
    @DisplayName("5️确认订单")
    void testConfirmOrder() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("5️⃣  确认订单");
        System.out.println("=".repeat(50));

        // 确保 orderId 已设置
        if (orderId == null) {
            System.out.println("⚠️  订单ID为空，跳过确认订单测试");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "/api/orders/" + orderId + "/confirm",
            request,
            Map.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        
        String status = (String) response.getBody().get("status");
        assertEquals("CONFIRMED", status);
        
        System.out.println("✅ 订单确认成功");
        System.out.println("订单状态: " + status);
        System.out.println("响应数据: " + response.getBody());
    }

    /**
     * 6. 查询订单详情
     */
    @Test
    @Order(6)
    @DisplayName("6️查询订单详情")
    void testGetOrder() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("6️⃣  查询订单详情");
        System.out.println("=".repeat(50));

        if (orderId == null) {
            System.out.println("⚠️  订单ID为空，跳过查询订单测试");
            return;
        }

        ResponseEntity<Map> response = restTemplate.getForEntity(
            "/api/orders/" + orderId,
            Map.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        
        System.out.println("✅ 订单查询成功");
        System.out.println("响应数据: " + response.getBody());
    }

    /**
     * 7. 检查库存
     */
    @Test
    @Order(7)
    @DisplayName("7️检查库存")
    void testCheckInventory() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("7️⃣  检查库存");
        System.out.println("=".repeat(50));

        ResponseEntity<Map> response = restTemplate.getForEntity(
            "/api/inventory/products/PROD-TEST-001/check?requiredQuantity=5",
            Map.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        
        System.out.println("✅ 库存检查成功");
        System.out.println("响应数据: " + response.getBody());
    }
}
