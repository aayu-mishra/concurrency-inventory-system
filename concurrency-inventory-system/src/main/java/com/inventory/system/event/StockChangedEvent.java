package com.inventory.system.event;

public record StockChangedEvent(String masterSkuId, String warehouseCode, int onHand) {}
