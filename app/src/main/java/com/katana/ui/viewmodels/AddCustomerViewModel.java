package com.katana.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.katana.business.CustomersController;
import com.katana.entities.Customer;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;

import java.util.List;

import static com.katana.ui.support.Constants.CUSTOMER;

/**
 * Created by Akwasi Owusu on 1/11/18
 */

public class AddCustomerViewModel extends BaseViewModel {

    private String name;
    private String phone;
    private CustomersController customersController;
    private ObservableArrayList<CustomerItemViewModel> customers;

    public AddCustomerViewModel() {
        customersController = KatanaFactory.getCustomersController();
        customers = new ObservableArrayList<>();
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Bindable
    public ObservableArrayList<CustomerItemViewModel> getCustomers() {
        return customers;
    }

    @Override
    public void initialize(boolean isFromSavedInstance) {
        if (!isFromSavedInstance) {
            AsyncTask.execute(() -> {
                try {
                    customersController.getAllCustomers(new OperationCallBack<Customer>() {
                        @Override
                        public void onCollectionOperationSuccessful(List<Customer> results) {
                            for (Customer customer : results) {
                                customers.add(new CustomerItemViewModel(customer));
                            }
                        }
                    });
                } catch (KatanaBusinessException e) {
                    Log.e("CustomersViewModel", "Error loading customers", e);
                }
            });
        }
    }

    public void saveCustomer() {
        try {
            customersController.addCustomer(name, phone, new OperationCallBack<Customer>() {
                @Override
                public void onOperationSuccessful(Customer result) {
                    Bundle data = new Bundle();
                    if (result != null) {
                        data.putSerializable(CUSTOMER, result);
                    }
                    getViewActionRequest().Invoke(CUSTOMER, data);
                }
            });
        } catch (KatanaBusinessException e) {
            Log.e("AddCustomerViewModel", "Error adding customer", e);
            getViewActionRequest().Invoke(CUSTOMER, null);
        }
    }
}
