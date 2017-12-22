package com.katana.ui.views;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.katana.ui.BR;
import com.katana.ui.viewmodels.BaseViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by AOwusu on 11/13/2017.
 */

public abstract class BaseActivity<B extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {

    protected B binding;
    @Inject
    protected V viewModel;


    abstract int getLayoutReSource();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutReSource());
        binding.setVariable(BR.viewmodel, viewModel);
    }
}
