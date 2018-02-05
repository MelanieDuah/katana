package com.katana.ui.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.katana.ui.R;
import com.katana.ui.databinding.FragmentAddCustomerBinding;
import com.katana.ui.viewmodels.AddCustomerViewModel;

import static com.katana.ui.support.Constants.CUSTOMER;


public class AddCustomerFragment extends BaseFragment<FragmentAddCustomerBinding, AddCustomerViewModel> {

    public AddCustomerFragment() {
        // Required empty public constructor
    }

    public static AddCustomerFragment newInstance() {
        return new AddCustomerFragment();
    }

    @Override
    protected int getLayoutReSource() {
        return R.layout.fragment_add_customer;
    }

    @Override
    protected AddCustomerViewModel getViewModel() {
        return viewModel == null ? new AddCustomerViewModel() : viewModel;
    }

    @Override
    protected void OnViewActionInvoked(String param, Bundle bundle) {
        switch (param) {
            case CUSTOMER:
                handleCallerFragmentRequest(param, bundle);
                break;
            default:
                super.OnViewActionInvoked(param, bundle);
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
