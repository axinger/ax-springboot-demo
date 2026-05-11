package com.axinger.inventory.application;

import com.axinger.inventory.InventoryModule;
import com.axinger.inventory.domain.InventoryRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 库存应用服务 - 处理库存业务逻辑
 */
@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public InventoryService(InventoryRepository inventoryRepository, ApplicationEventPublisher eventPublisher) {
        this.inventoryRepository = inventoryRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 创建新产品
     */
    public InventoryModule.Product createProduct(String id, String name, String description, InventoryModule.ProductCategory category) {
        InventoryModule.Product product = new InventoryModule.Product(
                id,
                name,
                description,
                category,
                InventoryModule.ProductStatus.DRAFT,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // 这里应该有产品仓储，暂时直接返回
        return product;
    }

    /**
     * 激活产品
     */
    public InventoryModule.Product activateProduct(String productId) {
        // 这里应该有产品仓储，暂时创建一个新的活跃产品
        return new InventoryModule.Product(
                productId,
                "产品" + productId,
                "产品描述",
                InventoryModule.ProductCategory.OTHER,
                InventoryModule.ProductStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    /**
     * 初始化库存
     */
    public InventoryModule.Inventory initializeInventory(String productId, int initialQuantity, int safetyStockLevel) {
        if (initialQuantity < 0) {
            throw new IllegalArgumentException("初始库存数量不能为负数");
        }
        if (safetyStockLevel < 0) {
            throw new IllegalArgumentException("安全库存水平不能为负数");
        }

        InventoryModule.Inventory inventory = new InventoryModule.Inventory(
                productId,
                initialQuantity,
                initialQuantity,
                0,
                safetyStockLevel,
                LocalDateTime.now()
        );

        InventoryModule.Inventory savedInventory = inventoryRepository.save(inventory);

        // 发布库存初始化事件
        eventPublisher.publishEvent(new InventoryInitializedEvent(productId, initialQuantity));

        return savedInventory;
    }

    /**
     * 查询库存
     */
    @Transactional(readOnly = true)
    public Optional<InventoryModule.Inventory> findInventoryByProductId(String productId) {
        return inventoryRepository.findById(productId);
    }

    /**
     * 预留库存
     */
    public InventoryModule.Inventory reserveInventory(String productId, int quantity) {
        InventoryModule.Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new InventoryNotFoundException("库存未找到: " + productId));

        InventoryModule.Inventory reservedInventory = inventory.reserve(quantity);
        InventoryModule.Inventory savedInventory = inventoryRepository.save(reservedInventory);

        // 发布库存预留事件
        eventPublisher.publishEvent(new InventoryReservedEvent(productId, quantity));

        return savedInventory;
    }

    /**
     * 释放预留库存
     */
    public InventoryModule.Inventory releaseInventory(String productId, int quantity) {
        InventoryModule.Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new InventoryNotFoundException("库存未找到: " + productId));

        InventoryModule.Inventory releasedInventory = inventory.release(quantity);
        InventoryModule.Inventory savedInventory = inventoryRepository.save(releasedInventory);

        // 发布库存释放事件
        eventPublisher.publishEvent(new InventoryReleasedEvent(productId, quantity));

        return savedInventory;
    }

    /**
     * 消耗库存（出库）
     */
    public InventoryModule.Inventory consumeInventory(String productId, int quantity) {
        InventoryModule.Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new InventoryNotFoundException("库存未找到: " + productId));

        InventoryModule.Inventory consumedInventory = inventory.consume(quantity);
        InventoryModule.Inventory savedInventory = inventoryRepository.save(consumedInventory);

        // 发布库存消耗事件
        eventPublisher.publishEvent(new InventoryConsumedEvent(productId, quantity));

        return savedInventory;
    }

    /**
     * 增加库存（入库）
     */
    public InventoryModule.Inventory addInventory(String productId, int quantity) {
        InventoryModule.Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new InventoryNotFoundException("库存未找到: " + productId));

        InventoryModule.Inventory updatedInventory = inventory.addStock(quantity);
        InventoryModule.Inventory savedInventory = inventoryRepository.save(updatedInventory);

        // 发布库存增加事件
        eventPublisher.publishEvent(new InventoryAddedEvent(productId, quantity));

        return savedInventory;
    }

    /**
     * 检查库存是否充足
     */
    @Transactional(readOnly = true)
    public boolean hasAvailableStock(String productId, int requiredQuantity) {
        return inventoryRepository.findById(productId)
                .map(inventory -> inventory.hasAvailableStock(requiredQuantity))
                .orElse(false);
    }

    /**
     * 检查是否为低库存
     */
    @Transactional(readOnly = true)
    public boolean isLowStock(String productId) {
        return inventoryRepository.findById(productId)
                .map(InventoryModule.Inventory::isLowStock)
                .orElse(true);
    }

    // 领域事件定义
    public record InventoryInitializedEvent(String productId, int quantity) {}
    public record InventoryReservedEvent(String productId, int quantity) {}
    public record InventoryReleasedEvent(String productId, int quantity) {}
    public record InventoryConsumedEvent(String productId, int quantity) {}
    public record InventoryAddedEvent(String productId, int quantity) {}

    // 异常类
    public static class InventoryNotFoundException extends RuntimeException {
        public InventoryNotFoundException(String message) {
            super(message);
        }
    }
}