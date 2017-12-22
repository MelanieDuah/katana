package com.katana.infrastructure.dependencymanagement.dataaccess;

import com.katana.datacess.DataAccess;
import com.katana.datacess.concrete.DataAccessImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

@Module
public class DataAccessModule {

    @Provides
    public DataAccess provideDataAccess(){
        return new DataAccessImpl();
    }
}
