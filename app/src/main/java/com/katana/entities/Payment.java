package com.katana.entities;

import java.util.Date;

/**
 * The payment entity
 *
 * @author Akwasi Owusu
 */

public class Payment extends BaseEntity {

    private static final long serialVersionUID = 2576415329209120071L;

    private Date paymentDate;

    private double amountReceived;

    private double balance;

    private double discount;

    private Customer customer;

    public Payment() {
        super();
    }

    public Payment(Customer customer, double amountReceived, double discount,
                   double balance) {
        super();
        this.customer = customer;
        this.amountReceived = amountReceived;
        this.discount = discount;
        this.balance = balance;
        paymentDate = new Date();
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
