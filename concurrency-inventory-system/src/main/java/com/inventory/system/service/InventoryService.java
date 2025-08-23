package com.inventory.system.service;

import com.inventory.system.entity.InventoryItemEntity;
import com.inventory.system.entity.ProductEntity;
import com.inventory.system.entity.WarehouseEntity;
import com.inventory.system.event.StockChangedEvent;
import com.inventory.system.repository.InventoryItemRepository;
import com.inventory.system.util.IdempotencyStore;
import com.inventory.system.util.RedisLockManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    private final RedisLockManager lockManager;
    private final IdempotencyStore idempotencyStore;
    private final ApplicationEventPublisher events;

    public InventoryService(
            InventoryItemRepository inventoryItemRepository,
            RedisLockManager lockManager,
            IdempotencyStore idempotencyStore,
            ApplicationEventPublisher events) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.lockManager = lockManager;
        this.idempotencyStore = idempotencyStore;
        this.events = events;
    }

    @Transactional
    public InventoryItemEntity adjustStock(
            ProductEntity product, WarehouseEntity warehouse, int delta, String requestId) {
        if (!idempotencyStore.firstTime(requestId, Duration.ofMinutes(10))) {
            return inventoryItemRepository.findByProductAndWarehouse(product, warehouse)
                    .orElseThrow(() -> new IllegalStateException("Inventory item missing for idempotent replay"));
        }
        String key = "lock:inv:" + product.getSkuId() + ":" + warehouse.getCode();
        String token = UUID.randomUUID().toString();
        boolean locked = lockManager.tryLock(key, token, 5);
        if (!locked) {
            throw new IllegalStateException("Concurrent update in progress. Please retry.");
        }
        try {
            InventoryItemEntity item = inventoryItemRepository
                    .findByProductAndWarehouse(product, warehouse)
                    .orElse(InventoryItemEntity.builder()
                            .product(product)
                            .warehouse(warehouse)
                            .onHand(0)
                            .reserved(0)
                            .build());

            int newOnHand = item.getOnHand() + delta;
            if (newOnHand < 0) {
                throw new IllegalArgumentException("Not enough stock to reduce by " + delta);
            }

            item.setOnHand(newOnHand);
            InventoryItemEntity saved = inventoryItemRepository.saveAndFlush(item);
            events.publishEvent(new StockChangedEvent(
                    saved.getProduct().getSkuId(),
                    saved.getWarehouse().getCode(),
                    saved.getOnHand()));

            return saved;

        } finally {
            lockManager.unlock(key, token);
        }
    }
}
