package com.inventory.system.service;

import com.inventory.system.entity.ProductEntity;
import com.inventory.system.exception.ResourceNotFoundException;
import com.inventory.system.repository.ProductRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public String deleteProductBySkuId(String skuId){
        ProductEntity product = productRepository.findBySkuId(skuId);
        if(product==null){
            throw new ResourceNotFoundException("Product doesn't exist with this skuId");
        }

        productRepository.deleteBySkuId(skuId);
        return "Product Deleted Successfully";
    }

    public ProductEntity updateProduct(ProductEntity productEntity){
        ProductEntity product = productRepository.findBySkuId(productEntity.getSkuId());
        if(product==null){
            throw new ResourceNotFoundException("Product doesn't exist with this skuId");
        }
        product.setName(productEntity.getName());
        product.setDescription(productEntity.getDescription());
        product.setLastModifiedBy(productEntity.getLastModifiedBy());
        productRepository.save(product);
        return product;
    }
}
