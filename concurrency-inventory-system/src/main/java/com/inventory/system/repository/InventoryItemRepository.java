package com.inventory.system.repository;

import com.inventory.system.entity.InventoryItemEntity;
import com.inventory.system.entity.ProductEntity;
import com.inventory.system.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Long> {

    Optional<InventoryItemEntity> findByProductAndWarehouse(ProductEntity productEntity, WarehouseEntity warehouse);
}
