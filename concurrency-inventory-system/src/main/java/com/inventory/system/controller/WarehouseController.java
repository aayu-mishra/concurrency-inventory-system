package com.inventory.system.controller;

import com.inventory.system.entity.WarehouseEntity;
import com.inventory.system.model.WarehouseRequest;
import com.inventory.system.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @GetMapping
    public ResponseEntity<List<WarehouseEntity>> getWarehouse(){
        List<WarehouseEntity> warehouseEntities=  warehouseRepository.findAll();

        return ResponseEntity.ok().body(warehouseEntities);
    }

    @PostMapping
    public ResponseEntity<WarehouseEntity> addWareHouse(@RequestBody WarehouseRequest warehouseRequest){
        WarehouseEntity warehouse= new WarehouseEntity();
        warehouse.setCode(warehouseRequest.getWarehouseCode());
        warehouse.setName(warehouse.getName());
        warehouse.setLocation(warehouseRequest.getLocation());
        warehouseRepository.saveAndFlush(warehouse);
        return ResponseEntity.ok().body(warehouse);
    }
}
