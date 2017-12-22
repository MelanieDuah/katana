package com.katana.infrastructure.dependencymanagement.ui;

import com.katana.business.UserController;
import com.katana.ui.viewmodels.SignInViewModel;
import com.katana.ui.viewmodels.SignUpViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by AOwusu on 11/13/2017.
 */

@Module
public class ViewModelBuilderModule {

    @Provides
    public SignUpViewModel provideSignUpViewModel(UserController userController) {
        return new SignUpViewModel(userController);
    }

    @Provides
    public SignInViewModel provideSignInViewModel() {
        return new SignInViewModel();
    }
}
