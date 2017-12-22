package com.katana.ui.viewmodels;

import android.databinding.Bindable;
import android.util.Log;

import com.katana.business.UserController;
import com.katana.entities.User;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.ui.BR;

import javax.inject.Inject;

/**
 * Created by AOwusu on 11/13/2017.
 */

public class SignUpViewModel extends BaseViewModel{
    private String userName;
    private String email;
    private String phone;
    private String password;
    private UserController userController;

    @Inject
    public SignUpViewModel(UserController userController) {
        this.userController = userController;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public void onSignupRequested() {

        User user = new User();
        user.setEmail(email);
        user.setUserName(userName);
        user.setPassword(password);
        user.setPhone(phone);

        try {
            userController.saveUser(user);
        }
        catch (KatanaBusinessException ex){
            Log.e(ex.getMessage(), ex.getStackTrace()[0].getClassName(), ex);
        }
    }
}
