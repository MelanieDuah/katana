package com.katana.entities;

public class SalePayment extends BaseEntity {

    private static final long serialVersionUID = -2532537384332475209L;

    private static final String SALE_ID = "SaleId";
    private static final String PAYMENT_ID = "PaymentId";

    private Sale sale;

    private Payment payment;

    public SalePayment() {
        super();
    }

    public SalePayment(Sale sale, Payment payment) {
        super();
        this.sale = sale;
        this.payment = payment;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

}
