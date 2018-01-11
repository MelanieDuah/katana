package com.katana.business;

import com.katana.entities.Customer;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

import java.util.List;

/**
 * Provides methods for recording and retrieval of customer information
 *
 * @author Akwasi Owusu
 */

public interface CustomersController {

    List<Customer> getAllCustomers(
            OperationCallBack<Customer> operationCallBack)
            throws KatanaBusinessException;

    OperationResult updateCustomer(Customer customer)
            throws KatanaBusinessException;

    OperationResult addCustomer(Customer customer)
            throws KatanaBusinessException;

    Customer findCustomer(int customerId,
                          OperationCallBack<Customer> operationCallBack)
            throws KatanaBusinessException;

    void addOrUpdateCustomerLocalOnly(Customer customer)
            throws KatanaBusinessException;

    void getLastInsertedCustomerId(OperationCallBack<Integer> operationCallBack) throws KatanaBusinessException;
}
