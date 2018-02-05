package com.katana.business;

import com.katana.entities.Customer;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

/**
 * Provides methods for recording and retrieval of customer information
 *
 * @author Akwasi Owusu
 */

public interface CustomersController {

    OperationResult getAllCustomers(
            OperationCallBack<Customer> operationCallBack)
            throws KatanaBusinessException;

    OperationResult updateCustomer(Customer customer, OperationCallBack<Customer> operationCallBack)
            throws KatanaBusinessException;

    OperationResult addCustomer(String name, String phone, OperationCallBack<Customer> operationCallBack)
            throws KatanaBusinessException;

    OperationResult findCustomer(int customerId,
                                 OperationCallBack<Customer> operationCallBack)
            throws KatanaBusinessException;
}
