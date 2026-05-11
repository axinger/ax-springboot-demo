package com.axinger.inventory.domain;

import com.axinger.inventory.InventoryModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 库存仓储接口
 */
@Repository
public interface InventoryRepository extends JpaRepository<InventoryModule.Inventory, String> {

    /**
     * 根据产品ID查找库存（已在主键中实现）
     */

    /**
     * 查找低库存产品
     */
    List<InventoryModule.Inventory> findByAvailableQuantityLessThanEqual(int threshold);

    /**
     * 查找可用库存大于0的产品
     */
    List<InventoryModule.Inventory> findByAvailableQuantityGreaterThan(int quantity);

    /**
     * 查找有预留库存的产品
     */
    List<InventoryModule.Inventory> findByReservedQuantityGreaterThan(int quantity);

    /**
     * 检查产品是否存在库存记录
     */
    boolean existsById(String productId);

    /**
     * 获取总库存量
     */
    long count();
}