package com.inventory.system.controller;

import com.inventory.system.entity.InventoryItemEntity;
import com.inventory.system.entity.ProductEntity;
import com.inventory.system.entity.WarehouseEntity;
import com.inventory.system.exception.ResourceNotFoundException;
import com.inventory.system.model.TransferStockRequest;
import com.inventory.system.model.WarehouseRequest;
import com.inventory.system.repository.InventoryItemRepository;
import com.inventory.system.repository.ProductRepository;
import com.inventory.system.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InventoryItemRepository inventoryItemRepository;

    @GetMapping
    public ResponseEntity<Page<WarehouseEntity>> getWarehouse(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<WarehouseEntity> warehouseEntities=  warehouseRepository.findAll(PageRequest.of(page,size));
        return ResponseEntity.ok().body(warehouseEntities);
    }

    @PostMapping
    public ResponseEntity<WarehouseEntity> addWareHouse(@RequestBody WarehouseRequest warehouseRequest){
        if(warehouseRepository.findByCode(warehouseRequest.getWarehouseCode()).isPresent()){
            throw new RuntimeException("Warehouse Already Present");
        }
        WarehouseEntity warehouse= new WarehouseEntity();
        warehouse.setCode(warehouseRequest.getWarehouseCode());
        warehouse.setName(warehouse.getName());
        warehouse.setLocation(warehouseRequest.getLocation());
        warehouseRepository.saveAndFlush(warehouse);
        return ResponseEntity.ok().body(warehouse);
    }

    @GetMapping("/{code}")
    public ResponseEntity<WarehouseEntity> getWarehouseByCode(@PathVariable String code){
        Optional<WarehouseEntity> warehouseEntities=  warehouseRepository.findByCode(code);
        return ResponseEntity.ok().body(warehouseEntities.get());
    }

    @PutMapping
    public ResponseEntity<WarehouseEntity> updateWareHouse(@PathVariable String code,
                                                           @RequestBody WarehouseEntity updateDetails) {
        WarehouseEntity warehouseEntity= warehouseRepository.findByCode(code).orElseThrow(() ->
                new RuntimeException("Warehouse Doesn't Exist"));
        warehouseEntity.setName(updateDetails.getName());
        warehouseEntity.setLocation(updateDetails.getLocation());
        warehouseRepository.save(warehouseEntity);
       return ResponseEntity.ok().body(warehouseEntity);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> deleteWareHouse(@PathVariable String code){
        Optional<WarehouseEntity> warehouse= warehouseRepository.deleteByCode(code);
        return ResponseEntity.ok().body("Warehouse Deleted Successfully");
    }

    @PostMapping("/transfer")
    @Transactional
    public ResponseEntity<String> transferStock(@RequestBody TransferStockRequest request) {

        ProductEntity product = productRepository.findBySkuId(request.getSkuId());

        WarehouseEntity sourceWarehouse = warehouseRepository.findByCode(request.getSourceWarehouseCode())
                .orElseThrow(() -> new RuntimeException("Source warehouse not found"));

        WarehouseEntity destinationWarehouse = warehouseRepository.findByCode(request.getDestinationWarehouseCode())
                .orElseThrow(() -> new RuntimeException("Destination warehouse not found"));

        if (sourceWarehouse.getCode().equals(destinationWarehouse.getCode())) {
            throw new RuntimeException("Source and destination warehouses cannot be the same");
        }

        // 3. Get inventory for source warehouse
        InventoryItemEntity sourceInventory = inventoryItemRepository.findByProductAndWarehouse(product, sourceWarehouse)
                .orElseThrow(() -> new RuntimeException("No stock found in source warehouse"));

        if (sourceInventory.getOnHand() < request.getQuantity()) {
            throw new RuntimeException("Not enough stock in source warehouse");
        }

        // 4. Deduct stock from source
        sourceInventory.setOnHand(sourceInventory.getOnHand() - request.getQuantity());
        inventoryItemRepository.save(sourceInventory);

        // 5. Add stock to destination (create if not exists)
        InventoryItemEntity destinationInventory = inventoryItemRepository.findByProductAndWarehouse(product, destinationWarehouse)
                .orElse(new InventoryItemEntity(product, destinationWarehouse, 0));

        destinationInventory.setOnHand(destinationInventory.getOnHand() + request.getQuantity());
        inventoryItemRepository.save(destinationInventory);

        return ResponseEntity.ok("Transferred " + request.getQuantity() + " units of "
                + product.getSkuId() + " from " + sourceWarehouse.getCode() + " to " + destinationWarehouse.getCode());
    }


}
