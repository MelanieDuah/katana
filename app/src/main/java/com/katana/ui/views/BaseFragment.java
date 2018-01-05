package com.katana.ui.views;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katana.ui.BR;
import com.katana.ui.support.KatanaAction;
import com.katana.ui.viewmodels.BaseViewModel;


/**
 * Created by AOwusu on 12/25/2017.
 */

public abstract class BaseFragment<B extends ViewDataBinding, V extends BaseViewModel> extends Fragment {

    protected B binding;
    protected V viewModel;


    protected abstract int getLayoutReSource();
    protected abstract V getViewModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = getViewModel();
        binding = DataBindingUtil.inflate(inflater,getLayoutReSource(),container,false);
        binding.setVariable(BR.viewmodel, viewModel);
        viewModel.setActivityAction(new KatanaAction() {
            @Override
            public void Invoke(String param, Bundle bundle) {
                OnActionInvoked(param, bundle);
            }

            @Override
            public <T> T Invoke(String param) {
                return getData(param);
            }
        });
        viewModel.Initialize();
        return binding.getRoot();
    }

    protected void OnActionInvoked(String x, Bundle bundle) {
    }

    protected <T> T getData(String param){
        return null;
    }
}
