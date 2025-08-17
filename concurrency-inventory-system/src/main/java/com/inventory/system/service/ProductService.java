package com.inventory.system.service;

import com.inventory.system.entity.ProductEntity;
import com.inventory.system.exception.ResourceNotFoundException;
import com.inventory.system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductEntity> getAllProduct(){
        return productRepository.findAll();
    }

    public ProductEntity addProduct(ProductEntity productEntity){
        return productRepository.save(productEntity);
    }

    public ProductEntity getProductBySkuId(String skuId){
        ProductEntity product=productRepository.findBySkuId(skuId);
        if(product==null){
            throw new ResourceNotFoundException("Product doesn't exist with this skuId");
        }
        return product;
    }
}
