package com.inventory.system.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="warehouses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String location;

    private String name;
}
