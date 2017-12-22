package com.katana.ui.views;

import com.katana.ui.R;
import com.katana.ui.databinding.ActivitySignInBinding;
import com.katana.ui.viewmodels.SignInViewModel;

public class SignInActivity extends BaseActivity<ActivitySignInBinding, SignInViewModel> {

    @Override
    int getLayoutReSource() {
        return R.layout.activity_sign_in;
    }
}
