package com.katana.entities;

import java.util.Date;

/**
 * Created by Akwasi Owusu on 1/17/18
 */

public class InventoryItem extends BaseEntity {

    private String productId;
    private String inventoryDate;
    private int quantity;

    public InventoryItem() {
        super();
    }

    public InventoryItem(String productId, int quantity) {
        this.productId = productId;
        this.inventoryDate = String.valueOf(new Date().getTime());
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(String inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
