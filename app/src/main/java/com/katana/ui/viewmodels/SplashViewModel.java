package com.katana.ui.viewmodels;

import android.content.Context;

import com.katana.business.UserController;
import com.katana.infrastructure.KatanaFactory;

import static com.katana.ui.support.Constants.MAIN_ACTIVITY_REQUEST;
import static com.katana.ui.support.Constants.SIGN_IN_REQUEST;

/**
 * Created by AOwusu on 12/24/2017.
 */

public class SplashViewModel extends BaseViewModel {
    private UserController userController;

    public SplashViewModel(Context context) {
        this.userController = KatanaFactory.getUserController();
    }

    @Override
    public void initialize() {
        super.initialize();
        if(userController.getCurrentUser() == null)
            getActivityAction().Invoke(SIGN_IN_REQUEST, null);
        else
            getActivityAction().Invoke(MAIN_ACTIVITY_REQUEST, null);
    }
}
