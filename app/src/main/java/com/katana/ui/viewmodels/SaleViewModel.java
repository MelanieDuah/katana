package com.katana.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.util.Log;

import com.katana.business.SalesController;
import com.katana.entities.Customer;
import com.katana.entities.Sale;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.ui.BR;
import com.katana.ui.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.katana.ui.support.Constants.ALERT_CREDIT;
import static com.katana.ui.support.Constants.BARCODES;
import static com.katana.ui.support.Constants.BARCODE_SCAN;
import static com.katana.ui.support.Constants.CUSTOMER;
import static com.katana.ui.support.Constants.HIDE_MAIN_PROGRESS;
import static com.katana.ui.support.Constants.NO_CUSTOMER_DESIRED;

/**
 * Created by Akwasi Owusu on 1/7/18
 */

public class SaleViewModel extends BaseViewModel {

    private SalesController salesController;
    private ObservableArrayList<SaleItemViewModel> saleItems;
    private List<Sale> sales;
    private PropertyChangeCallBack propertyChangeCallBack;
    private double total;
    private double amountReceived;
    private double discount;
    private double balance;
    private boolean isResetting = false;

    public SaleViewModel() {
        salesController = KatanaFactory.getSalesController();
        propertyChangeCallBack = new PropertyChangeCallBack();
        saleItems = new ObservableArrayList<>();
        sales = new ArrayList<>();
    }

    @Bindable
    public ObservableArrayList<SaleItemViewModel> getSaleItems() {
        return saleItems;
    }

    @Bindable
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
        notifyPropertyChanged(BR.balance);
    }

    @Bindable
    public double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(double amountReceived) {
        if (this.amountReceived != amountReceived) {
            this.amountReceived = amountReceived;

            if (isResetting)
                notifyPropertyChanged(BR.amountReceived);

            notifyPropertyChanged(BR.balance);
        }
    }

    @Bindable
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        if (this.discount != discount) {
            this.discount = discount;
            if (isResetting)
                notifyPropertyChanged(BR.discount);

            notifyPropertyChanged(BR.balance);
        }
    }

    @Bindable
    public double getBalance() {
        balance = amountReceived - (total - discount);
        return balance;
    }

    @Override
    public int getEmptyImageId() {
        return R.drawable.ic_empty_bag;
    }

    @Override
    public int getEmptyString() {
        return R.string.emptyBagMessage;
    }

    @Override
    public <T> void receiveDataFromView(String key, T data) {
        switch (key) {
            case BARCODES:
                recordSalesFromBarcodes((Map<String, Integer>) data);
                break;
            case NO_CUSTOMER_DESIRED:
                saveCurrentSales(null);
                break;
            case CUSTOMER:
                Bundle bundle = (Bundle) data;
                if (bundle != null) {
                    Customer customer = (Customer) bundle.getSerializable(CUSTOMER);
                    assert customer != null;
                    saveCurrentSales(customer.getId());
                }
                break;
        }
    }

    private void recordSalesFromBarcodes(Map<String, Integer> barcodeData) {
        try {
            setLoadingData(!barcodeData.isEmpty());
            salesController.generateSaleItems(barcodeData, new OperationCallBack<Sale>() {
                @Override
                public void onCollectionOperationSuccessful(List<Sale> results) {
                    if (results != null) {
                        sales.addAll(results);
                        saleItems.addAll(getSaleItemsFromSaleEntities(results));
                        calculateTotal();
                    }
                    setLoadingData(false);
                }
            });
        } catch (KatanaBusinessException e) {
            Log.e("SaleViewModel", "Error loading saleItems", e);
        }
    }

    private List<SaleItemViewModel> getSaleItemsFromSaleEntities(List<Sale> sales) {
        List<SaleItemViewModel> saleViewModels = new ArrayList<>();

        for (Sale sale : sales) {
            SaleItemViewModel saleItemViewModel = findExistingSaleItem(sale);
            if (saleItemViewModel == null) {
                saleItemViewModel = new SaleItemViewModel(sale);
                saleItemViewModel.addOnPropertyChangedCallback(propertyChangeCallBack);
                saleViewModels.add(saleItemViewModel);
            } else {
                saleItemViewModel.setQuantity(sale.getQuantitySold());
            }
        }
        return saleViewModels;
    }

    private void calculateTotal() {
        int total = 0;
        for (SaleItemViewModel saleViewModel : saleItems) {
            total += saleViewModel.getTotal();
        }
        setTotal(total);
    }

    private SaleItemViewModel findExistingSaleItem(Sale sale) {
        SaleItemViewModel existingSaleItem = null;

        for (SaleItemViewModel saleItem : saleItems) {
            if (saleItem.getSale().equals(sale)) {
                existingSaleItem = saleItem;
                break;
            }
        }

        return existingSaleItem;
    }

    private class PropertyChangeCallBack extends Observable.OnPropertyChangedCallback {

        @Override
        public void onPropertyChanged(Observable observable, int propertyId) {
            if (propertyId == BR.total) {
                if (observable instanceof SaleItemViewModel) {
                    calculateTotal();
                }
            }
        }
    }

    public void requestSaveSale() {
        if (amountReceived < total) {
            this.getViewActionRequest().Invoke(ALERT_CREDIT, null);
        } else {
            getViewActionRequest().Invoke(HIDE_MAIN_PROGRESS, null);
            saveCurrentSales(null);
        }
    }

    public void requestBarcodeScanning() {
        this.getViewActionRequest().Invoke(BARCODE_SCAN, null);
    }

    private void saveCurrentSales(String customerId) {
        try {
            salesController.saveSales(sales, customerId, amountReceived, discount, balance, new OperationCallBack<Sale>() {
                @Override
                public void onCollectionOperationSuccessful(List<Sale> results) {
                    getViewActionRequest().Invoke(HIDE_MAIN_PROGRESS, null);
                    resetValues();
                }
            });
        } catch (KatanaBusinessException e) {
            Log.e("SaleViewModel", "Error saving sales", e);
        }
    }

    public void resetValues() {
        isResetting = true;
        saleItems.clear();
        setTotal(0D);
        setDiscount(0D);
        setAmountReceived(0D);
        setLoadingData(false);
        isResetting = false;
    }
}
