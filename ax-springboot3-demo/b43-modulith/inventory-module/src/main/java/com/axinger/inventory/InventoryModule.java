package com.axinger.inventory;

import org.springframework.modulith.ApplicationModule;

import java.time.LocalDateTime;

/**
 * 库存模块 - Spring Modulith 应用模块
 */
@ApplicationModule(
    displayName = "库存模块"
)
public class InventoryModule {

    /**
     * 产品聚合根
     */
    public record Product(
            String id,
            String name,
            String description,
            ProductCategory category,
            ProductStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        public Product withStatus(ProductStatus newStatus) {
            return new Product(
                    this.id,
                    this.name,
                    this.description,
                    this.category,
                    newStatus,
                    this.createdAt,
                    LocalDateTime.now()
            );
        }

        public boolean isAvailable() {
            return this.status == ProductStatus.ACTIVE;
        }
    }

    /**
     * 库存聚合根
     */
    public record Inventory(
            String productId,
            int totalQuantity,
            int availableQuantity,
            int reservedQuantity,
            int safetyStockLevel,
            LocalDateTime lastUpdated
    ) {

        public Inventory {
            if (totalQuantity < 0) {
                throw new IllegalArgumentException("总库存数量不能为负数");
            }
            if (availableQuantity < 0) {
                throw new IllegalArgumentException("可用库存数量不能为负数");
            }
            if (reservedQuantity < 0) {
                throw new IllegalArgumentException("预留库存数量不能为负数");
            }
            if (safetyStockLevel < 0) {
                throw new IllegalArgumentException("安全库存水平不能为负数");
            }
            if (availableQuantity + reservedQuantity > totalQuantity) {
                throw new IllegalArgumentException("可用库存和预留库存之和不能超过总库存");
            }
        }

        public Inventory reserve(int quantity) {
            if (quantity <= 0) {
                throw new IllegalArgumentException("预留数量必须大于0");
            }
            if (quantity > availableQuantity) {
                throw new IllegalStateException("可用库存不足");
            }

            return new Inventory(
                    this.productId,
                    this.totalQuantity,
                    this.availableQuantity - quantity,
                    this.reservedQuantity + quantity,
                    this.safetyStockLevel,
                    LocalDateTime.now()
            );
        }

        public Inventory release(int quantity) {
            if (quantity <= 0) {
                throw new IllegalArgumentException("释放数量必须大于0");
            }
            if (quantity > reservedQuantity) {
                throw new IllegalStateException("预留库存不足");
            }

            return new Inventory(
                    this.productId,
                    this.totalQuantity,
                    this.availableQuantity + quantity,
                    this.reservedQuantity - quantity,
                    this.safetyStockLevel,
                    LocalDateTime.now()
            );
        }

        public Inventory consume(int quantity) {
            if (quantity <= 0) {
                throw new IllegalArgumentException("消耗数量必须大于0");
            }
            if (quantity > reservedQuantity) {
                throw new IllegalStateException("预留库存不足");
            }

            return new Inventory(
                    this.productId,
                    this.totalQuantity - quantity,
                    this.availableQuantity,
                    this.reservedQuantity - quantity,
                    this.safetyStockLevel,
                    LocalDateTime.now()
            );
        }

        public Inventory addStock(int quantity) {
            if (quantity <= 0) {
                throw new IllegalArgumentException("入库数量必须大于0");
            }

            return new Inventory(
                    this.productId,
                    this.totalQuantity + quantity,
                    this.availableQuantity + quantity,
                    this.reservedQuantity,
                    this.safetyStockLevel,
                    LocalDateTime.now()
            );
        }

        public boolean isLowStock() {
            return availableQuantity <= safetyStockLevel;
        }

        public boolean hasAvailableStock(int requiredQuantity) {
            return availableQuantity >= requiredQuantity;
        }
    }

    public enum ProductCategory {
        ELECTRONICS,    // 电子产品
        CLOTHING,       // 服装
        BOOKS,          // 图书
        HOME_APPLIANCES, // 家电
        SPORTS,         // 运动用品
        BEAUTY,         // 美妆
        FOOD,           // 食品
        OTHER           // 其他
    }

    public enum ProductStatus {
        DRAFT,          // 草稿
        ACTIVE,         // 活跃
        DISCONTINUED,   // 停产
        OUT_OF_STOCK    // 缺货
    }
}