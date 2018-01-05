package com.katana.ui.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.katana.ui.R;
import com.katana.ui.databinding.ActivitySplashBinding;
import com.katana.ui.viewmodels.SplashViewModel;

import java.util.Arrays;
import java.util.List;

import static com.katana.ui.support.Constants.MAIN_ACTIVITY_REQUEST;
import static com.katana.ui.support.Constants.SIGN_IN_REQUEST;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected int getLayoutReSource() {
        return R.layout.activity_splash;
    }

    @Override
    protected SplashViewModel getViewModel() {
        return new SplashViewModel(this);
    }

    @Override
    protected void OnActionInvoked(String param, Bundle bundle) {
        switch (param) {
            case SIGN_IN_REQUEST:
                requestSignIn();
                break;
            case MAIN_ACTIVITY_REQUEST:
                requestMainActivity();
                break;
        }
    }

    private void requestMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void requestSignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
        );
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LaunchScreenTheme)
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.drawable.mel_logo)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                requestMainActivity();
            } else {
                if(response.getErrorCode() == 20)
                    Toast.makeText(this, "Something shitty happened!", Toast.LENGTH_LONG);
            }
        }
    }
}
