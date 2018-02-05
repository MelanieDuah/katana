package com.katana.entities;

import java.util.Date;

/**
 * The payment entity
 *
 * @author Akwasi Owusu
 */

public class Payment extends BaseEntity {

    private static final long serialVersionUID = 2576415329209120071L;

    private String paymentDate;

    private double amountReceived;

    private double balance;

    private double discount;

    private String customerId;
    private String ownerId;

    public Payment() {
        super();
    }

    public Payment(String customerId, double amountReceived, double discount,
                   double balance) {
        super();
        this.customerId = customerId;
        this.amountReceived = amountReceived;
        this.discount = discount;
        this.balance = balance;
        paymentDate = String.valueOf(new Date().getTime());
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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

    @Override
    public boolean equals(Object another) {
        boolean equal = false;

        if (this == another) {
            equal = true;
        } else if (another != null && another instanceof Payment) {

            Payment anotherPayment = (Payment) another;
            equal = anotherPayment.paymentDate != null && paymentDate != null
                    && anotherPayment.paymentDate.equals(paymentDate)
                    && anotherPayment.amountReceived == amountReceived;
        }
        return equal;
    }

    @Override
    public int hashCode() {
        return 31 * paymentDate.hashCode();
    }

}
