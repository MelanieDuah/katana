package com.katana.business.concrete;

import com.katana.business.CustomersController;
import com.katana.business.ProductEntryController;
import com.katana.business.SalesController;
import com.katana.business.UserController;
import com.katana.dataaccess.DataAccess;
import com.katana.entities.BaseEntity;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

public class SalesControllerImpl implements SalesController {

    private final ProductEntryController productController;
    private final DataAccess dataAccess;
    private final Queue<String> notFoundProducts;
    private List<Sale> salesFromScannedItems; // all sales scanned since user began
    private List<Sale> currentSaleItems; // sales from the current set of barcodes given. A subset of salesFromScannedItems
    private int operationCount = 0;
    private boolean isSaving = false;
    private SimpleDateFormat dateFormatter;
    private CustomersController customersController;
    private UserController userController;

    public SalesControllerImpl() {
        productController = KatanaFactory.getProductController();
        dataAccess = KatanaFactory.getDataAccess();
        salesFromScannedItems = new ArrayList<>();
        notFoundProducts = new LinkedList<>();
        currentSaleItems = new ArrayList<>();
        dateFormatter = new SimpleDateFormat(LocalConstants.DATEFORMAT, Locale.getDefault());
        customersController = KatanaFactory.getCustomersController();
        userController = KatanaFactory.getUserController();
    }

    @Override
    public OperationResult generateSaleItems(Map<String, Integer> barcodeData, OperationCallBack<Sale> operationCallBack)
            throws KatanaBusinessException {
        OperationResult result;
        operationCount = 0;
        boolean saleItemsCreated = false;
        Set<String> barcodeSet = barcodeData.keySet();
        int totalItems = barcodeSet.size();

        for (String barcode : barcodeSet) {

            String barcodeNoChecksum = getBarcodeNoChecksum(barcode);
            int quantity = barcodeData.get(barcode);
            Sale sale = getExistingSale(barcodeNoChecksum);
            if (sale != null) {
                sale.setQuantitySold(sale.getQuantitySold() + quantity);
                updateProductQuantityOfSale(sale, quantity);
                saleItemsCreated |= true;
                if (++operationCount == totalItems)
                    operationCallBack.onCollectionOperationSuccessful(currentSaleItems);
            } else if (!notFoundProducts.contains(barcodeNoChecksum)) {
                addNewSale(barcodeNoChecksum, quantity, totalItems, operationCallBack);
                saleItemsCreated |= true;
            }
        }
        if (!saleItemsCreated) {
            operationCallBack.onCollectionOperationSuccessful(null);
            result = OperationResult.FAILED;
        } else
            result = OperationResult.SUCCESSFUL;

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
                    salesFromScannedItems.addAll(currentSaleItems);
                    callBack.onCollectionOperationSuccessful(currentSaleItems);
                    currentSaleItems.clear();
                }
            }
        });

    }

    private void addProductToSale(Product product, int count) {
        if (product != null && product.getQuantity() > 0) {
            Sale sale = new Sale();
            sale.setProduct(product);
            sale.setSaleDate(String.valueOf(new Date().getTime()));
            sale.setQuantitySold(count);
            currentSaleItems.add(sale);
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

        currentSaleItems.add(sale);
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
    public OperationResult saveSales(final List<Sale> sales, final String customerId, double amountReceived, double discount,
                                     final double balance, OperationCallBack<Sale> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        try {
            isSaving = true;
            List<BaseEntity> entities = new ArrayList<>();

            String userId = userController.getCurrentUser().getId();

            Payment payment = new Payment(customerId, amountReceived, discount, balance);
            payment.setOwnerId(userId);

            String paymentId = dataAccess.createAndReturnNewIdFor(Payment.class, payment);
            payment.setId(paymentId);

            entities.add(payment);

            for (Sale sale : sales) {
                if (sale.getCustomerId() == null && customerId != null)
                    sale.setCustomerId(customerId);

                if (balance >= 0)
                    sale.setPaidFor(true);

                sale.setOwnerId(userId);

                String saleId = dataAccess.createAndReturnNewIdFor(Sale.class, sale);
                sale.setId(saleId);
                SalePayment salePayment = new SalePayment(saleId, paymentId);
                String salePaymentId = dataAccess.createAndReturnNewIdFor(SalePayment.class, salePayment);
                salePayment.setId(salePaymentId);
                updateProductQuantityOfSale(sale, sale.getQuantitySold());
                entities.add(sale.getProduct());
                entities.add(salePayment);
            }

            entities.addAll(sales);
            dataAccess.addDataItems(new OperationCallBack<BaseEntity>() {
                @Override
                public void onCollectionOperationSuccessful(List<BaseEntity> results) {
                    operationCallBack.onCollectionOperationSuccessful(sales);
                }
            }, entities.toArray(new BaseEntity[entities.size()]));

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
        customersController.updateCustomer(customer, null);

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
//                result = saveSales(currentSales, customer.getId(), balancePerDate, 0, 0);
            } else if (amountReceived > 0) {
                // result = saveSales(currentSales, customer.getId(), amountReceived, 0, -(balancePerDate - amountReceived));
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
