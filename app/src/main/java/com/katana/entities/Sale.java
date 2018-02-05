package com.katana.entities;

import com.google.firebase.database.Exclude;

/**
 * The Sale entity
 *
 * @author Akwasi Owusu
 */
public class Sale extends BaseEntity {

    private static final long serialVersionUID = -8612909833588822894L;

    private String saleDate;

    private Product product;

    private int quantitySold;

    private String customerId;

    private boolean paidFor = false;
    private String ownerId;

    public Sale() {
        super();
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    @Exclude
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public boolean isPaidFor() {
        return paidFor;
    }

    public void setPaidFor(boolean paidFor) {
        this.paidFor = paidFor;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getProductId() {
        String productId = null;
        if (product != null)
            productId = product.getId();

        return productId;
    }

    @Override
    public boolean equals(Object another) {
        boolean equals = false;
        if (this == another) {
            equals = true;
        } else if (another != null && another instanceof Sale) {
            Sale anotherSale = (Sale) another;
            boolean checkFieldsNonNull = anotherSale.saleDate != null && saleDate != null
                    && anotherSale.product != null && product != null;
            equals = checkFieldsNonNull && anotherSale.saleDate.equals(saleDate)
                    && anotherSale.product.getBarcode().equals(product.getBarcode());
        }
        return equals;
    }

    @Override
    public int hashCode() {
        int hash = product != null && product.getBarcode() != null ? product.getBarcode().hashCode() : 1;
        hash *= saleDate != null ? saleDate.hashCode() : 2;
        return 31 * hash;
    }
}
