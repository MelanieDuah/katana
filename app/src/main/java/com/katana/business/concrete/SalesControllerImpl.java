package com.katana.business.concrete;

import com.katana.business.CustomersController;
import com.katana.business.ProductEntryController;
import com.katana.business.SalesController;
import com.katana.dataaccess.DataAccess;
import com.katana.entities.Customer;
import com.katana.entities.Payment;
import com.katana.entities.Product;
import com.katana.entities.Sale;
import com.katana.entities.SalePayment;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.exceptions.KatanaDataException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TimeZone;

public class SalesControllerImpl implements SalesController {

    private final ProductEntryController productController;
    private final DataAccess dataAccess;
    private final Queue<String> notFoundProducts;
    private List<Sale> salesFromScannedItems;
    private int operationCount = 0;
    private boolean isSaving = false;
    private SimpleDateFormat dateFormatter;
    private CustomersController customersController;


    public SalesControllerImpl() {
        productController = KatanaFactory.getProductController();
        dataAccess = KatanaFactory.getDataAccess();
        salesFromScannedItems = new ArrayList<>();
        notFoundProducts = new LinkedList<>();
        dateFormatter = new SimpleDateFormat(LocalConstants.DATEFORMAT);
        customersController = KatanaFactory.getCustomersController();
    }

    @Override
    public OperationResult generateSaleItems(Map<String, Integer> barcodeData, OperationCallBack<Sale> callBack)
            throws KatanaBusinessException {
        OperationResult result;
        operationCount = 0;
        Set<String> barcodeSet = barcodeData.keySet();
        int totalItems = barcodeSet.size();
        try {
            for (String barcode : barcodeSet) {

                String barcodeNoChecksum = getBarcodeNoChecksum(barcode);
                int quantity = barcodeData.get(barcode);
                Sale sale = getExistingSale(barcodeNoChecksum);
                if (sale != null) {
                    sale.setQuantitySold(sale.getQuantitySold() + quantity);
                    updateProductQuantityOfSale(sale, quantity);
                } else if (!notFoundProducts.contains(barcodeNoChecksum)) {
                    addNewSale(barcodeNoChecksum, quantity, totalItems, callBack);
                }
            }
            result = OperationResult.SUCCESSFUL;
        } catch (KatanaBusinessException e) {
            throw e;
        }
        return result;
    }

    private void addNewSale(final String barcode, final int count, final int total,
                            final OperationCallBack<Sale> callBack) throws KatanaBusinessException {


        productController.findProductByBarcode(barcode, new OperationCallBack<Product>() {

            @Override
            public void onOperationSuccessful(Product result) {
                if (result == null) {
                    addBarcodeToNotFoundList(barcode);
                }
                operationCount++;
                addProductToSale(result, count);
                if (callBack != null && operationCount == total) {
                    callBack.onCollectionOperationSuccessful(salesFromScannedItems);
                }
            }
        });

    }

    private void addProductToSale(Product product, int count) {
        if (product != null && product.getQuantity() > 0) {
            Sale sale = new Sale();
            sale.setProduct(product);
            sale.setSaleDate(Calendar.getInstance(TimeZone.getDefault()).getTime());
            sale.setQuantitySold(count);
            updateProductQuantityOfSale(sale, count);
            //sale.setOwnerId(session.getUser().getObjectId());
            salesFromScannedItems.add(sale);
        }
    }

    private void updateProductQuantityOfSale(Sale sale, int justAddedSaleQuantity) {
        Product product = sale.getProduct();
        if (product != null) {
            int productQuantity = product.getQuantity();
            int saleQuantity = sale.getQuantitySold();

            if (saleQuantity <= productQuantity) {
                product.setQuantity(productQuantity - saleQuantity);
            } else {
                sale.setQuantitySold(saleQuantity - justAddedSaleQuantity + productQuantity);
                product.setQuantity(0);
            }
        }
    }

    private Sale getExistingSale(String barcode) {
        Sale sale = null;
        for (Sale existingSale : salesFromScannedItems)
            if (existingSale.getProduct().getBarcode().equals(barcode)) {
                sale = existingSale;
                break;
            }
        return sale;
    }

    @Override
    public OperationResult saveSales(final List<Sale> sales, final Customer customer, double amountReceived, double discount,
                                     final double balance) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        try {
            isSaving = true;

            // TODO: Figure out a way to do this transactionally
            Payment payment = new Payment(customer, amountReceived, discount, balance);

            // payment.setOwnerId(session.getUser().getObjectId());

            result = dataAccess.addDataItem(payment, new OperationCallBack<Payment>() {
                @Override
                public void onOperationSuccessful(Payment payment) {
                    for (Sale sale : sales) {
                        if (sale.getCustomer() == null && customer != null) {
                            sale.setCustomer(customer);
                        }

                        if (balance >= 0) {
                            sale.setPaidFor(true);
                        }

                        SalePayment salePayment = new SalePayment(sale, payment);
                        try {
                            dataAccess.addDataItem(salePayment, null);
                        } catch (KatanaDataException e) {
                            onOperationFailed(e);
                        }
                    }
                    sales.clear();
                    isSaving = false;
                }
            });

        } catch (KatanaDataException e) {
            throw new KatanaBusinessException(e.getMessage(), e);
        }

        return result;
    }

    public String getBarcodeNoChecksum(String barcode) {
        return barcode.substring(0, barcode.length() - 1);
    }

    @Override
    public void findSalesByCustomer(Customer customer, OperationCallBack<SalePayment> operationCallBack)
            throws KatanaBusinessException {


    }

    @Override
    public OperationResult recordPayment(Customer customer, List<Sale> sales, double amountReceived, double discount,
                                         double balance, Map<String, Double> balancesPerSalesDate) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;

        customer.setAmountOwed(customer.getAmountOwed() - amountReceived);
        customersController.updateCustomer(customer);

        for (Entry<String, Double> entry : balancesPerSalesDate.entrySet()) {
            double balancePerDate = entry.getValue();
            String date = entry.getKey();
            List<Sale> currentSales = new ArrayList<>();
            for (Sale sale : sales) {
                if (dateFormatter.format(sale.getSaleDate()).equals(date)) {
                    currentSales.add(sale);
                }
            }

            if (amountReceived >= balancePerDate) {
                amountReceived -= balancePerDate;
                result = saveSales(currentSales, customer, balancePerDate, 0, 0);
            } else if (amountReceived > 0) {
                result = saveSales(currentSales, customer, amountReceived, 0, -(balancePerDate - amountReceived));
                break; //there is no point moving on in this case;
            }
        }
        return result;
    }

    private void addBarcodeToNotFoundList(String barcode) {
        if (notFoundProducts.size() >= 20) {
            notFoundProducts.remove();
        }
        notFoundProducts.add(barcode);
    }

    @Override
    public OperationResult getSalesBetween(Date fromDate, Date toDate, final OperationCallBack<Sale> operationCallBack)
            throws KatanaBusinessException {
        List<Sale> sales = new ArrayList<>();

        return OperationResult.SUCCESSFUL;
    }

    @Override
    public void clear() {
        if (salesFromScannedItems != null && !salesFromScannedItems.isEmpty() && !isSaving) {
            salesFromScannedItems.clear();
        }
    }

    public void removeFromTempList(int saleIndex) {
        if (salesFromScannedItems != null && saleIndex < salesFromScannedItems.size()) {
            salesFromScannedItems.remove(saleIndex);
        }
    }

    private class LocalConstants {
        public static final String CUSTOMEROBJECTID = "Sale.Customer.ObjectId";
        public static final String DATEFORMAT = "MMM dd, yyyy";
    }

}
