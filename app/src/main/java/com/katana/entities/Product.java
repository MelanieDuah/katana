package com.katana.entities;

import com.google.firebase.database.Exclude;

/**
 * The Product entity
 *
 * @author Akwasi Owusu
 */

public class Product extends BaseEntity {

    private static final long serialVersionUID = -4874210643819140613L;

    private String productName;

    private int quantity;

    private double price;

    private String barcode;

    private String imagePath;

    private String categoryId;

    private String categoryName;

    public Product() {
        super();
    }

    public Product(String productName, int quantity, double price,
                   Category category, String barcode) {
        super();
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.categoryId = category.getId();
        this.barcode = barcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Exclude
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object another) {
        boolean equal = false;

        if (this == another) {
            equal = true;
        } else if (another != null && another instanceof Product) {
            Product anotherProduct = (Product) another;
            equal = anotherProduct.barcode.equals(barcode);
        }

        return equal;
    }

    @Override
    public int hashCode() {
        return 31 * (barcode != null ? barcode.hashCode() : super.hashCode());
    }

}
