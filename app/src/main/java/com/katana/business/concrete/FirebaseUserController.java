package com.katana.business.concrete;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.katana.business.UserController;
import com.katana.entities.User;


/**
 * Created by Akwasi Owusu on 11/16/17.
 */

public class FirebaseUserController implements UserController {

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth authenticator;
    private User user;
    private static FirebaseUserController instance = new FirebaseUserController();

    private FirebaseUserController() {
        authenticator = FirebaseAuth.getInstance();
    }

    @Override
    public User getCurrentUser() {

        if (user == null) {
            FirebaseUser firebaseUser = authenticator.getCurrentUser();
            if (firebaseUser != null) {
                user = new User();
                user.setEmail(firebaseUser.getEmail());
                user.setName(firebaseUser.getDisplayName());
                user.setId(firebaseUser.getUid());
            }
        }
        return user;
    }

    public static FirebaseUserController getInstance() {
        return instance;
    }

}
