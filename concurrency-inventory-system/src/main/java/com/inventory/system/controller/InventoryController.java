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
import org.apache.coyote.BadRequestException;
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
    public ResponseEntity<InventoryItemEntity> adjustStock(@RequestBody AdjustStockRequest adjustStockRequest) throws BadRequestException {
        ProductEntity productEntity = productRepository.findBySkuId(adjustStockRequest.getSkuId());
        if(productEntity==null){
            throw new  ResourceNotFoundException("Product not found with specified skuId");
        }
        Optional<WarehouseEntity> warehouse= Optional.ofNullable(warehouseRepository.findByCode(adjustStockRequest.getWarehouseCode())
                .orElseThrow(() -> new ResourceNotFoundException("wareHouse Not Found")));

        Optional<InventoryItemEntity> inventoryItemEntity= Optional.ofNullable(inventoryItemRepository.findByProductAndWarehouse(productEntity, warehouse.get()).orElseThrow(() -> new ResourceNotFoundException("Product or wareHouse Not Found")));

        int newQty= inventoryItemEntity.get().getOnHand()+adjustStockRequest.getDelta();

        if(newQty<0){
            throw new BadRequestException("Not Enough Stock" + inventoryItemEntity.get().getOnHand());
        }

        inventoryItemEntity.get().setOnHand(newQty);
        inventoryItemRepository.save(inventoryItemEntity.get());
        return ResponseEntity.ok().body(inventoryItemEntity.get());

    }

}
