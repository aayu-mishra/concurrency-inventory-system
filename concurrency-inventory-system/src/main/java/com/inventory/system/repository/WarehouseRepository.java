package com.inventory.system.repository;

import com.inventory.system.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<WarehouseEntity, Long> {

    Optional<WarehouseEntity> findByCode(String code);

    Optional<WarehouseEntity> deleteByCode(String code);
}
