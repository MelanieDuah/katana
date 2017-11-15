package com.katana.ui;

import com.katana.infrastructure.DaggerKatanaAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by AOwusu on 11/13/2017.
 */

public class App extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerKatanaAppComponent.builder().create(this);
    }
}
