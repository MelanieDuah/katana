package com.katana.infrastructure;

import android.app.Application;

import com.katana.ui.App;
import com.katana.ui.views.SignUpActivity;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by AOwusu on 11/13/2017.
 */

@Component(modules = {AppModule.class,AndroidSupportInjectionModule.class, AndroidInjectionModule.class, ViewModelBuilderModule.class, ActivityBuilderModule.class})
public interface KatanaAppComponent extends AndroidInjector<App>{

     @Component.Builder
     abstract class Builder extends AndroidInjector.Builder<App>{
     }
}
