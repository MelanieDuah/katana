package com.katana.ui;

import com.katana.infrastructure.dependencymanagement.DaggerKatanaAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by AOwusu on 11/13/2017.
 */

public class App extends DaggerApplication {

    AndroidInjector<? extends DaggerApplication> appInjector;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

        if(appInjector == null)
            appInjector = DaggerKatanaAppComponent.builder().create(this);

        return appInjector;
    }
}
