package com.katana.business.concrete;

import com.katana.business.UserController;
import com.katana.datacess.DataAccess;
import com.katana.entities.User;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.exceptions.KatanaDataException;

import javax.inject.Inject;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

public class UserControllerImpl implements UserController {

    private DataAccess dataAccess;

    @Inject
    public UserControllerImpl(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public void saveUser(User user) throws KatanaBusinessException {
        try {
            dataAccess.addDataItem(user);
        } catch (KatanaDataException ex) {
            throw new KatanaBusinessException("save user failed", ex);
        }

    }
}
