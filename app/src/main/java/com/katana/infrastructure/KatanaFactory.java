package com.katana.infrastructure;

import com.katana.business.CustomersController;
import com.katana.business.ProductEntryController;
import com.katana.business.SalesController;
import com.katana.business.UserController;
import com.katana.business.concrete.FirebaseUserController;
import com.katana.business.concrete.ProductEntryControllerImpl;
import com.katana.business.concrete.SalesControllerImpl;
import com.katana.dataaccess.DataAccess;
import com.katana.dataaccess.concrete.FirebaseDataAccess;

/**
 * Created by AOwusu on 12/27/2017.
 */

public class KatanaFactory {
    public static UserController getUserController(){
        return new FirebaseUserController();
    }

    public static DataAccess getDataAccess(){
        return new FirebaseDataAccess();
    }

    public static ProductEntryController getProductController(){
        return new ProductEntryControllerImpl();
    }

    public static CustomersController getCustomersController() {
        return null;
    }

    public static SalesController getSalesController(){
        return new SalesControllerImpl();
    }
}
