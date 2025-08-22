package com.inventory.system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Getter
@Setter
@Table(name="inventory_items", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "warehouse_id"}))
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouse_id")
    private WarehouseEntity warehouse;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(nullable = false)
    private int onHand;

    @Column(nullable = false)
    private int reserved;

    @Version
    private Long version;

    public InventoryItemEntity(ProductEntity product, WarehouseEntity destinationWarehouse, int i) {
    }

    public int getAvailable() {
        return onHand - reserved;
    }
}
