package com.katana.ui.views;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.katana.ui.BR;
import com.katana.ui.viewmodels.BaseViewModel;


/**
 * Created by AOwusu on 11/13/2017.
 */

public abstract class BaseActivity<B extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {

    protected B binding;
    protected V viewModel;

    protected abstract int getLayoutReSource();
    protected abstract V getViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getViewModel();
        binding = DataBindingUtil.setContentView(this, getLayoutReSource());

        binding.setVariable(BR.viewmodel, viewModel);
        viewModel.setActivityAction(this::OnActivityActionInvoked);

        viewModel.initialize();
    }

    protected void OnActivityActionInvoked(String x, Bundle bundle) {
    }

    protected <T> T getData(String param){
        return null;
    }

}
