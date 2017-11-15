package com.katana.infrastructure.ui;

import com.katana.ui.views.SignInActivity;
import com.katana.ui.views.SignUpActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by AOwusu on 11/14/2017.
 */

@Module
public abstract class ActivityBuilderModule {
    @ContributesAndroidInjector
    abstract SignUpActivity signUpActivity();
    abstract SignInActivity signInActivity();
}
