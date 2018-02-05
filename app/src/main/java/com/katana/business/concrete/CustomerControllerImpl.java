package com.katana.business.concrete;

import com.katana.business.CustomersController;
import com.katana.dataaccess.DataAccess;
import com.katana.entities.Customer;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.exceptions.KatanaDataException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

/**
 * Created by Akwasi Owusu on 1/11/18
 */

public class CustomerControllerImpl implements CustomersController {

    private DataAccess dataAccess;

    public CustomerControllerImpl() {
        dataAccess = KatanaFactory.getDataAccess();
    }

    @Override
    public OperationResult getAllCustomers(OperationCallBack<Customer> operationCallBack) throws KatanaBusinessException {
        try {
            return dataAccess.findAllItems(Customer.class, operationCallBack);
        } catch (KatanaDataException e) {
            throw new KatanaBusinessException("Error retrieving customers", e);
        }
    }

    @Override
    public OperationResult updateCustomer(Customer customer, OperationCallBack<Customer> operationCallBack) throws KatanaBusinessException {
        return null;
    }

    @Override
    public OperationResult addCustomer(String name, String phone, OperationCallBack<Customer> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        try {
            Customer customer = new Customer(name, phone);
            result = dataAccess.addDataItem(customer, operationCallBack);
        } catch (KatanaDataException e) {
            throw new KatanaBusinessException("Error adding customer", e);
        }
        return result;
    }

    @Override
    public OperationResult findCustomer(int customerId, OperationCallBack<Customer> operationCallBack) throws KatanaBusinessException {
        return null;
    }
}
