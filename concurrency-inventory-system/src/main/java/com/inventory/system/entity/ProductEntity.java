package com.inventory.system.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "product", uniqueConstraints = @UniqueConstraint(columnNames = "skuId"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String skuId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String lastModifiedBy;
}
