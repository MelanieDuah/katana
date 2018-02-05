package com.katana.ui.views.fragments;


import com.katana.ui.R;
import com.katana.ui.databinding.FragmentPaymentsBinding;
import com.katana.ui.viewmodels.PaymentsViewModel;

public class PaymentsFragment extends BaseFragment<FragmentPaymentsBinding, PaymentsViewModel> {


    public PaymentsFragment() {
        super();
    }


    @Override
    protected int getLayoutReSource() {
        return R.layout.fragment_payments;
    }

    @Override
    protected PaymentsViewModel getViewModel() {
        return viewModel == null ? new PaymentsViewModel() : viewModel;
    }

    public static PaymentsFragment newInstance() {
        return new PaymentsFragment();
    }
}
