package com.inventory.system.service;

import com.inventory.system.entity.InventoryItemEntity;
import com.inventory.system.entity.ProductEntity;
import com.inventory.system.entity.WarehouseEntity;
import com.inventory.system.repository.InventoryItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

public class InventoryService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Transactional
    public InventoryItemEntity adjustStock(ProductEntity productEntity, WarehouseEntity warehouse, int delta){

        InventoryItemEntity item= inventoryItemRepository.findByProductAndWarehouse(productEntity,warehouse).orElse(InventoryItemEntity.builder().product(productEntity)
                .warehouse(warehouse).onHand(0).reserved(0).build());

        int newOnHand= item.getOnHand() +delta;

        if(newOnHand< 0){
            throw new IllegalArgumentException("Not Enough Stock to reduce By "+ delta);

        }

        item.setOnHand(newOnHand);
        return inventoryItemRepository.save(item);
    }
}
