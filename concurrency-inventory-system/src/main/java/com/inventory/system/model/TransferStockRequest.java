package com.inventory.system.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferStockRequest {
    private String skuId;
    private String sourceWarehouseCode;
    private String destinationWarehouseCode;
    private int quantity;
}