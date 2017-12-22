package com.katana.ui.views;

import com.katana.ui.R;
import com.katana.ui.databinding.ActivitySignupBinding;
import com.katana.ui.viewmodels.SignUpViewModel;

public class SignUpActivity extends BaseActivity<ActivitySignupBinding, SignUpViewModel> {

    @Override
    int getLayoutReSource() {
        return R.layout.activity_signup;
    }
}
