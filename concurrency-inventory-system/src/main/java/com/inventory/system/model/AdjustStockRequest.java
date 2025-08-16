package com.inventory.system.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjustStockRequest {

    private String skuId;

    private String warehouseCode;

    private int delta;
}
