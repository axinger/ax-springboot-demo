package com.axinger.inventory.web;

import com.axinger.inventory.InventoryModule;
import com.axinger.inventory.application.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 库存控制器 - REST API 端点
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * 初始化库存
     */
    @PostMapping("/products/{productId}/initialize")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse initializeInventory(
            @PathVariable String productId,
            @RequestBody InitializeInventoryRequest request) {

        InventoryModule.Inventory inventory = inventoryService.initializeInventory(
                productId,
                request.initialQuantity(),
                request.safetyStockLevel()
        );

        return toInventoryResponse(inventory);
    }

    /**
     * 获取库存信息
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable String productId) {
        Optional<InventoryModule.Inventory> inventory = inventoryService.findInventoryByProductId(productId);

        return inventory.map(inv -> ResponseEntity.ok(toInventoryResponse(inv)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 预留库存
     */
    @PostMapping("/products/{productId}/reserve")
    public ResponseEntity<InventoryResponse> reserveInventory(
            @PathVariable String productId,
            @RequestBody ReserveInventoryRequest request) {

        try {
            InventoryModule.Inventory inventory = inventoryService.reserveInventory(productId, request.quantity());
            return ResponseEntity.ok(toInventoryResponse(inventory));
        } catch (InventoryService.InventoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 释放预留库存
     */
    @PostMapping("/products/{productId}/release")
    public ResponseEntity<InventoryResponse> releaseInventory(
            @PathVariable String productId,
            @RequestBody ReleaseInventoryRequest request) {

        try {
            InventoryModule.Inventory inventory = inventoryService.releaseInventory(productId, request.quantity());
            return ResponseEntity.ok(toInventoryResponse(inventory));
        } catch (InventoryService.InventoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 消耗库存（出库）
     */
    @PostMapping("/products/{productId}/consume")
    public ResponseEntity<InventoryResponse> consumeInventory(
            @PathVariable String productId,
            @RequestBody ConsumeInventoryRequest request) {

        try {
            InventoryModule.Inventory inventory = inventoryService.consumeInventory(productId, request.quantity());
            return ResponseEntity.ok(toInventoryResponse(inventory));
        } catch (InventoryService.InventoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 增加库存（入库）
     */
    @PostMapping("/products/{productId}/add")
    public InventoryResponse addInventory(
            @PathVariable String productId,
            @RequestBody AddInventoryRequest request) {

        InventoryModule.Inventory inventory = inventoryService.addInventory(productId, request.quantity());
        return toInventoryResponse(inventory);
    }

    /**
     * 检查库存是否充足
     */
    @GetMapping("/products/{productId}/check")
    public StockCheckResponse checkStockAvailability(
            @PathVariable String productId,
            @RequestParam int requiredQuantity) {

        boolean hasStock = inventoryService.hasAvailableStock(productId, requiredQuantity);
        boolean isLowStock = inventoryService.isLowStock(productId);

        return new StockCheckResponse(productId, requiredQuantity, hasStock, isLowStock);
    }

    /**
     * 创建新产品
     */
    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody CreateProductRequest request) {
        InventoryModule.Product product = inventoryService.createProduct(
                request.productId(),
                request.name(),
                request.description(),
                InventoryModule.ProductCategory.valueOf(request.category())
        );

        return toProductResponse(product);
    }

    /**
     * 激活产品
     */
    @PostMapping("/products/{productId}/activate")
    public ProductResponse activateProduct(@PathVariable String productId) {
        InventoryModule.Product product = inventoryService.activateProduct(productId);
        return toProductResponse(product);
    }

    // 响应对象转换方法
    private InventoryResponse toInventoryResponse(InventoryModule.Inventory inventory) {
        return new InventoryResponse(
                inventory.productId(),
                inventory.totalQuantity(),
                inventory.availableQuantity(),
                inventory.reservedQuantity(),
                inventory.safetyStockLevel(),
                inventory.isLowStock(),
                inventory.lastUpdated()
        );
    }

    private ProductResponse toProductResponse(InventoryModule.Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.description(),
                product.category().name(),
                product.status().name(),
                product.createdAt(),
                product.updatedAt()
        );
    }

    // 请求/响应对象
    public record InitializeInventoryRequest(int initialQuantity, int safetyStockLevel) {}

    public record ReserveInventoryRequest(int quantity) {}

    public record ReleaseInventoryRequest(int quantity) {}

    public record ConsumeInventoryRequest(int quantity) {}

    public record AddInventoryRequest(int quantity) {}

    public record CreateProductRequest(String productId, String name, String description, String category) {}

    public record InventoryResponse(
            String productId,
            int totalQuantity,
            int availableQuantity,
            int reservedQuantity,
            int safetyStockLevel,
            boolean isLowStock,
            java.time.LocalDateTime lastUpdated
    ) {}

    public record ProductResponse(
            String id,
            String name,
            String description,
            String category,
            String status,
            java.time.LocalDateTime createdAt,
            java.time.LocalDateTime updatedAt
    ) {}

    public record StockCheckResponse(
            String productId,
            int requiredQuantity,
            boolean hasAvailableStock,
            boolean isLowStock
    ) {}
}