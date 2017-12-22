package com.katana.infrastructure.dependencymanagement;

import com.katana.infrastructure.dependencymanagement.business.BusinessModule;
import com.katana.infrastructure.dependencymanagement.dataaccess.DataAccessModule;
import com.katana.infrastructure.dependencymanagement.ui.ActivityBuilderModule;
import com.katana.infrastructure.dependencymanagement.ui.ViewModelBuilderModule;
import com.katana.ui.App;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by AOwusu on 11/13/2017.
 */

@Component(modules = {
        AppModule.class,
        AndroidSupportInjectionModule.class,
        AndroidInjectionModule.class,
        ActivityBuilderModule.class,
        ViewModelBuilderModule.class,
        BusinessModule.class,
        DataAccessModule.class
})
public interface KatanaAppComponent extends AndroidInjector<App> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {

    }
}
