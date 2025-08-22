package com.inventory.system.repository;

import com.inventory.system.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findBySkuId(String skuId);

    void deleteBySkuId(String skuId);
}
