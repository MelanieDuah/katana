package com.katana.ui.views;

import android.os.Bundle;

import com.katana.ui.R;
import com.katana.ui.databinding.ActivitySignupBinding;
import com.katana.ui.viewmodels.SignUpViewModel;

public class SignUpActivity extends BaseActivity<ActivitySignupBinding, SignUpViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayoutAndBinding(R.layout.activity_signup, savedInstanceState);
    }
}
