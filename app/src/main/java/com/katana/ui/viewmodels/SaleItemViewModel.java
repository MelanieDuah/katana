package com.katana.ui.viewmodels;

import android.databinding.Bindable;

import com.katana.ui.BR;

import static com.katana.ui.support.Constants.INCREASE;

/**
 * Created by Akwasi Owusu on 1/8/18.
 */

public class SaleItemViewModel extends BaseViewModel {

    private String productName = "John";
    private String categoryName;
    private int quantity;
    private double price;

    public SaleItemViewModel() {
        super();
        this.productName = "John Foster";
        this.categoryName = "shoes";
        this.quantity = 3;
        this.price = 20D;
    }

    public SaleItemViewModel(String productName, String categoryName, int quantity, double price) {
        super();
        this.productName = productName;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.price = price;
    }

    @Bindable
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
        notifyPropertyChanged(BR.productName);
    }

    @Bindable
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        notifyPropertyChanged(BR.categoryName);
    }

    @Bindable
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantity);
        notifyPropertyChanged(BR.total);
    }

    @Bindable
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public double getTotal() {
        return price * quantity;
    }

    public void changeQuantity(String changeType) {
        if (changeType.equals(INCREASE)) {
             setQuantity(quantity + 1);
        } else {
            if (quantity > 0)
                setQuantity(quantity - 1);
        }

    }
}
