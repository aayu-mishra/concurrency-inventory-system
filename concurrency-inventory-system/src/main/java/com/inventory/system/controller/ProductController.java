package com.inventory.system.controller;

import com.inventory.system.entity.ProductEntity;
import com.inventory.system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProduct(){
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/skuId")
    public ResponseEntity<Optional<ProductEntity>> getProductBySkuId(@RequestParam String skuId){
        return ResponseEntity.ok(productService.getProductBySkuId(skuId));
    }

    @PostMapping
    public ResponseEntity<ProductEntity> addProduct(@RequestBody ProductEntity productEntity){
        return ResponseEntity.ok(productService.addProduct(productEntity));
    }
}
