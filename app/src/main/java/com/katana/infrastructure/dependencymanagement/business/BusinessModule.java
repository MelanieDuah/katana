package com.katana.infrastructure.dependencymanagement.business;

import com.katana.business.UserController;
import com.katana.business.concrete.UserControllerImpl;
import com.katana.datacess.DataAccess;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

@Module
public class BusinessModule {

    @Provides
    @Inject
    public UserController provideUserController(DataAccess dataAccess){
        return new UserControllerImpl(dataAccess);
    }
}
