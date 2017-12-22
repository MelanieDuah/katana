package com.katana.infrastructure.dependencymanagement.ui;

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

    @ContributesAndroidInjector
    abstract SignInActivity signInActivity();
}
