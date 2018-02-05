package com.katana.ui.viewmodels;

import android.databinding.Bindable;
import android.text.format.DateFormat;

import com.katana.entities.InventoryItem;
import com.katana.entities.Product;

import java.util.Date;

/**
 * Created by Akwasi Owusu on 1/18/18
 */

public class InventoryItemViewModel extends BaseListItemViewModel {

    private InventoryItem inventoryItem;
    private Product product;

    InventoryItemViewModel(InventoryItem inventoryItem, Product product) {
        assert inventoryItem != null && product != null;

        this.inventoryItem = inventoryItem;
        this.product = product;
    }

    @Bindable
    public String getProductName() {
        return product.getProductName();
    }

    @Bindable
    public int getOriginalQuantity() {
        return inventoryItem.getQuantity();
    }

    @Bindable
    public int getCurrentQuantity() {
        return product.getQuantity();
    }

    @Bindable
    public String getInventoryDate() {
        return DateFormat.format("dd-MM-yyyy",
                new Date(Long.parseLong(inventoryItem.getInventoryDate()))).toString();
    }

    @Bindable
    public double getPrice() {
        return product.getPrice();
    }

    @Bindable
    public double getTotal() {
        return getCurrentQuantity() * getPrice();
    }
}
