package com.katana.ui.viewmodels;

import android.databinding.Bindable;

import com.katana.entities.Customer;
import com.katana.ui.R;

import java.text.DecimalFormat;

/**
 * Created by Akwasi Owusu on 1/21/18
 */

public class CustomerItemViewModel extends BaseListItemViewModel {

    private Customer customer;

    CustomerItemViewModel(Customer customer) {
        this.customer = customer;
    }

    @Bindable
    public String getName() {
        return customer.getName();
    }

    @Bindable
    public String getPhone() {
        return customer.getPhone();
    }

    @Bindable
    public String getAmountDue() {
        return new DecimalFormat("0.00").format(customer.getAmountOwed());
    }

    @Override
    public int getLeftSwipeBackground() {
        return R.drawable.orange_ripple;
    }

    @Override
    public int getLeftSwipeIcon() {
        return R.drawable.ic_delete;
    }

    @Override
    public int getLeftSwipeLabel() {
        return R.string.delete;
    }

    @Override
    public int getRightSwipeBackground() {
        return R.drawable.blue_ripple;
    }

    @Override
    public int getRightSwipeIcon() {
        return R.drawable.ic_wallet_white;
    }

    @Override
    public int getRightSwipeLabel() {
        return R.string.melanie_payment;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return customer.getName();
    }
}
