package com.inventory.system.controller;

import com.inventory.system.entity.InventoryItemEntity;
import com.inventory.system.entity.ProductEntity;
import com.inventory.system.entity.WarehouseEntity;
import com.inventory.system.exception.ResourceNotFoundException;
import com.inventory.system.model.AddInventoryRequest;
import com.inventory.system.model.AdjustStockRequest;
import com.inventory.system.repository.InventoryItemRepository;
import com.inventory.system.repository.ProductRepository;
import com.inventory.system.repository.WarehouseRepository;
import com.inventory.system.service.InventoryService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add/inventory")
    public ResponseEntity<InventoryItemEntity> createInventoryItem(@RequestBody AddInventoryRequest addInventoryRequest){
        ProductEntity productEntity = productRepository.findBySkuId(addInventoryRequest.getSkuId());
        Optional<WarehouseEntity> warehouse= warehouseRepository.findByCode(addInventoryRequest.getCode());

        InventoryItemEntity inventoryItemEntity= new InventoryItemEntity();

        inventoryItemEntity.setProduct(productEntity);
        inventoryItemEntity.setWarehouse(warehouse.get());

        inventoryItemEntity.setOnHand(addInventoryRequest.getInitialStock());

        InventoryItemEntity saved= inventoryItemRepository.saveAndFlush(inventoryItemEntity);

        return  ResponseEntity.ok().body(saved);
    }

    @PostMapping("/adjust")
    public ResponseEntity<InventoryItemEntity> adjustStock(@RequestBody AdjustStockRequest adjustStockRequest, @RequestHeader("X-Idempotency-Key") String reqId) throws BadRequestException {
        ProductEntity productEntity = productRepository.findBySkuId(adjustStockRequest.getSkuId());
        if(productEntity==null){
            throw new  ResourceNotFoundException("Product not found with specified skuId");
        }
        Optional<WarehouseEntity> warehouse= Optional.ofNullable(warehouseRepository.findByCode(adjustStockRequest.getWarehouseCode())
                .orElseThrow(() -> new ResourceNotFoundException("wareHouse Not Found")));

        InventoryItemEntity updatedItem = inventoryService.adjustStock(
                productEntity,
                warehouse.get(),
                adjustStockRequest.getDelta(),
                reqId
        );


        return ResponseEntity.ok().body(updatedItem);

    }

}
