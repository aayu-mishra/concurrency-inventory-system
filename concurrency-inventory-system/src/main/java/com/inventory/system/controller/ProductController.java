package com.inventory.system.controller;

import com.inventory.system.entity.ProductEntity;
import com.inventory.system.repository.ProductRepository;
import com.inventory.system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

//    @GetMapping
//    public ResponseEntity<List<ProductEntity>> getAllProduct(){
//        return ResponseEntity.ok(productService.getAllProduct());
//    }

    @GetMapping
    public ResponseEntity<Page<ProductEntity>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok().body(productRepository.findAll(pageable));
    }


    @GetMapping("/{skuId}")
    public ResponseEntity<ProductEntity> getProductBySkuId(@PathVariable String skuId){
        return ResponseEntity.ok(productService.getProductBySkuId(skuId));
    }

    @PostMapping
    public ResponseEntity<ProductEntity> addProduct(@RequestBody ProductEntity productEntity){
        return ResponseEntity.ok(productService.addProduct(productEntity));
    }

    @DeleteMapping("/{skuId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String skuId){
        return ResponseEntity.ok().body(productService.deleteProductBySkuId(skuId));
    }

    @PutMapping
    public ResponseEntity<ProductEntity> updateProduct(@RequestBody ProductEntity productEntity){
        System.out.println(productEntity.getName());
        return ResponseEntity.ok(productService.updateProduct(productEntity));
    }

}
