package com.katana.business;

import com.katana.entities.User;
import com.katana.infrastructure.exceptions.KatanaBusinessException;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

public interface UserController {
    void saveUser(User user) throws KatanaBusinessException;
}
