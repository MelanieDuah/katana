package com.katana.ui.views;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.katana.ui.BR;
import com.katana.ui.R;
import com.katana.ui.viewmodels.BaseViewModel;

/**
 * Created by AOwusu on 11/13/2017.
 */

public class BaseActivity<B extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {

    protected B binding;
    protected V viewModel;

    protected final void initialzeLayoutAndBinding(int layoutResourceId, Bundle bundle) {
        binding = DataBindingUtil.setContentView(this,layoutResourceId);
        binding.setVariable(BR.viewmodel, viewModel);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
}
