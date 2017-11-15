package com.katana.infrastructure.ui;

import com.katana.ui.viewmodels.SignUpViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by AOwusu on 11/13/2017.
 */

@Module
public class ViewModelBuilderModule {

    @Provides
    public SignUpViewModel provideSignUpViewModel(){
        return new SignUpViewModel();
    }
}
