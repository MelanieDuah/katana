package com.katana.ui.viewmodels;

import android.databinding.Bindable;

import com.katana.entities.Product;
import com.katana.entities.Sale;
import com.katana.ui.BR;

import static com.katana.ui.support.Constants.INCREASE;

/**
 * Created by Akwasi Owusu on 1/8/18
 */

public class SaleItemViewModel extends BaseListItemViewModel {

    private String productName = "John";
    private String categoryName;
    private int quantity;
    private double price;
    private Sale sale;
    private int quantityRemaining;

    SaleItemViewModel(Sale sale) {
        super();

        Product product = sale.getProduct();
        assert product != null;

        this.productName = product.getProductName();
        this.categoryName = product.getCategoryName();
        this.quantity = sale.getQuantitySold();
        this.price = product.getPrice();
        this.sale = sale;
        this.quantityRemaining = product.getQuantity();
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
    public int getQuantityRemaining() {
        return quantityRemaining;
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

    public Sale getSale() {
        return sale;
    }

    public void changeQuantity(String changeType) {
        if (changeType.equals(INCREASE)) {
            if (quantity + 1 <= sale.getProduct().getQuantity())
                setQuantity(quantity + 1);
        } else {
            if (quantity > 0)
                setQuantity(quantity - 1);
        }

    }
}
