package com.katana.business;

import com.katana.entities.Customer;
import com.katana.entities.Sale;
import com.katana.entities.SalePayment;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Provides methods for recording sales
 *
 * @author Akwasi Owusu
 */
public interface SalesController {

    OperationResult generateSaleItems(Map<String, Integer> barcodeData,
                                 OperationCallBack<Sale> salesCallBack)
            throws KatanaBusinessException;

    void findSalesByCustomer(Customer customer,
                             OperationCallBack<SalePayment> operationCallBack)
            throws KatanaBusinessException;

    OperationResult recordPayment(Customer customer, List<Sale> sale,
                                  double amount, double discount, double balance, Map<String, Double> balancesPerSaleDate)
            throws KatanaBusinessException;

    OperationResult getSalesBetween(Date fromDate, Date toDate,
                               OperationCallBack<Sale> operationCallBack)
            throws KatanaBusinessException;

    OperationResult saveSales(List<Sale> sales, String customerId, double amountReceived,
                              double discount, double balance, OperationCallBack<Sale> operationCallBack) throws KatanaBusinessException;


    void clear();
}
