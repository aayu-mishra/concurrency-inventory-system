package com.inventory.system.controller;

import com.inventory.system.entity.InventoryItemEntity;
import com.inventory.system.entity.ProductEntity;
import com.inventory.system.entity.WarehouseEntity;
import com.inventory.system.model.AdjustStockRequest;
import com.inventory.system.repository.InventoryItemRepository;
import com.inventory.system.repository.ProductRepository;
import com.inventory.system.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @PostMapping("/adjust")
    public ResponseEntity<InventoryItemEntity> adjustStock(@RequestBody AdjustStockRequest adjustStockRequest){

        Optional<ProductEntity> productEntity = productRepository.findBySkuId(adjustStockRequest.getSkuId());

        Optional<WarehouseEntity> warehouse= warehouseRepository.findByCode(adjustStockRequest.getWarehouseCode());

        Optional<InventoryItemEntity> inventoryItemEntity= inventoryItemRepository.findByProductAndWarehouse(productEntity.get(),warehouse.get());

        return ResponseEntity.ok().body(inventoryItemEntity.get());

    }

}
