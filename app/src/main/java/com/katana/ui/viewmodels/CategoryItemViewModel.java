package com.katana.ui.viewmodels;

import android.databinding.Bindable;

/**
 * Created by Akwasi Owusu on 1/14/18
 */

public class CategoryItemViewModel extends BaseListItemViewModel {
    private String categoryName;
    private int quantity;
    private double totalValue;

    CategoryItemViewModel(String categoryName, int quantity, double total) {
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.totalValue = total;
    }

    @Bindable
    public String getCategoryName() {
        return categoryName;
    }

    @Bindable
    public int getQuantity() {
        return quantity;
    }

    @Bindable
    public double getTotalValue() {
        return totalValue;
    }
}
