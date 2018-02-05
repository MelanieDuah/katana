package com.katana.ui.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.katana.ui.R;
import com.katana.ui.databinding.FragmentCustomersBinding;
import com.katana.ui.viewmodels.CustomersViewModel;

import static com.katana.ui.support.Constants.ADD_CUSTOMER;
import static com.katana.ui.support.Constants.CUSTOMER;
import static com.katana.ui.support.Constants.RECORD_PAYMENT;

public class CustomersFragment extends BaseFragment<FragmentCustomersBinding, CustomersViewModel> {


    public CustomersFragment() {
        super();
    }


    @Override
    protected int getLayoutReSource() {
        return R.layout.fragment_customers;
    }

    @Override
    protected CustomersViewModel getViewModel() {
        return viewModel == null ? new CustomersViewModel() : viewModel;
    }

    public static CustomersFragment newInstance() {
        return new CustomersFragment();
    }


    @Override
    protected void OnViewActionInvoked(String param, Bundle bundle) {
        switch (param) {
            case ADD_CUSTOMER:
                startRequestedFragment(AddCustomerFragment::newInstance);
                break;
            case RECORD_PAYMENT:
                startRequestedFragment(PaymentsFragment::newInstance, bundle);
            case CUSTOMER:
                handleCallerFragmentRequest(param, bundle);
                break;
        }
    }

    private void handleCallerFragmentRequest(String param, Bundle bundle) {
        Fragment callerFragment = getTargetFragment();
        if (callerFragment != null) {
            submitCustomerDataToRequester(callerFragment, param, bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contentFrame, callerFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void submitCustomerDataToRequester(Fragment callerFragment, String param, Bundle bundle) {
        if (callerFragment != null && callerFragment instanceof BaseFragment) {
            ((BaseFragment) callerFragment).getViewModel().receiveDataFromView(param, bundle);
        }
    }
}
