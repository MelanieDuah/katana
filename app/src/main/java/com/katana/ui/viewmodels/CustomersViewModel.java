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
import com.katana.infrastructure.support.Asserter;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.ui.R;

import java.util.List;

import static com.katana.ui.support.Constants.ADD_CUSTOMER;
import static com.katana.ui.support.Constants.CUSTOMER;
import static com.katana.ui.support.Constants.CUSTOMER_ID;
import static com.katana.ui.support.Constants.RECORD_PAYMENT;

/**
 * Created by Akwasi Owusu on 1/21/18
 */

public class CustomersViewModel extends BaseViewModel {
    private ObservableArrayList<CustomerItemViewModel> customers;
    private CustomersController customersController;

    public CustomersViewModel() {
        customers = new ObservableArrayList<>();
        customersController = KatanaFactory.getCustomersController();
    }

    @Bindable
    public ObservableArrayList<CustomerItemViewModel> getCustomers() {
        return customers;
    }

    @Override
    public int getEmptyImageId() {
        return R.drawable.ic_buyer;
    }

    @Override
    public int getEmptyString() {
        return R.string.nocustomers;
    }

    public void onCustomerSelected(Object index) {
        if (index instanceof Integer) {
            int selectedIndex = (int) index;
            Customer customer = customers.get(selectedIndex).getCustomer();
            Bundle data = new Bundle();
            if (customer != null) {
                data.putSerializable(CUSTOMER, customer);
            }
            getViewActionRequest().Invoke(CUSTOMER, data);
        }
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
                                CustomerItemViewModel customerItemViewModel = new CustomerItemViewModel(customer);
                                customerItemViewModel.setSwipeRightAction(CustomersViewModel.this::OnDeleteRequested);
                                customerItemViewModel.setSwipeLeftAction(CustomersViewModel.this::OnPaymentRequested);
                                customers.add(customerItemViewModel);
                            }
                        }
                    });
                } catch (KatanaBusinessException e) {
                    Log.e("CustomersViewModel", "Error loading customers", e);
                }
            });
        }
    }

    public void requestAddNewCustomer() {
        getViewActionRequest().Invoke(ADD_CUSTOMER, null);
    }

    @Override
    public <T> void receiveDataFromView(String key, T data) {
        if (key.equals(CUSTOMER)) {
            Bundle bundle = (Bundle) data;
            if (bundle != null) {
                Customer customer = (Customer) bundle.getSerializable(CUSTOMER);
                assert customer != null;

                CustomerItemViewModel customerItemViewModel = new CustomerItemViewModel(customer);
                customerItemViewModel.setSwipeRightAction(this::OnDeleteRequested);
                customerItemViewModel.setSwipeLeftAction(this::OnPaymentRequested);

                customers.add(new CustomerItemViewModel(customer));
            }
        }
    }

    private void OnDeleteRequested(Object selected) {

    }

    private void OnPaymentRequested(Object selected) {
        Asserter.doAssert(selected instanceof Integer, "selected parameter not integer CustomersViewModel -> OnPaymentRequestd");
        Bundle bundle = new Bundle();
        bundle.putInt(CUSTOMER_ID, (int) selected);
        getViewActionRequest().Invoke(RECORD_PAYMENT, bundle);
    }
}
