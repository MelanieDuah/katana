package com.katana.ui.viewmodels;

import android.databinding.Bindable;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

public class SignInViewModel extends BaseViewModel {

    private String email;
    private String password;

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void onSignInRequested(){

    }
}
