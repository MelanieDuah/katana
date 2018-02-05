package com.katana.entities;

public class SalePayment extends BaseEntity {

    private static final long serialVersionUID = -2532537384332475209L;

    private String saleId;

    private String paymentId;

    public SalePayment() {
        super();
    }

    public SalePayment(String saleId, String paymentId) {
        this.saleId = saleId;
        this.paymentId = paymentId;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
