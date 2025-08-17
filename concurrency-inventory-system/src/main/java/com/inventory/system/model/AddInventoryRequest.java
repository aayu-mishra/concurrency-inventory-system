package com.inventory.system.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInventoryRequest {

    private String skuId;

    private String code;

    private int initialStock;
}
