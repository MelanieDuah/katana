package com.katana.ui.viewmodels;

import android.databinding.Bindable;
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

import static com.katana.ui.support.Constants.ALERT_CREDIT;
import static com.katana.ui.support.Constants.CUSTOMER;
import static com.katana.ui.support.Constants.HIDE_MAIN_PROGRESS;

/**
 * Created by Akwasi Owusu on 1/21/18
 */

public class PaymentsViewModel extends BaseViewModel {

    private SalesController salesController;
    private ObservableArrayList<SaleItemViewModel> saleItems;
    private List<Sale> sales;
    private double total;
    private double amountReceived;
    private double discount;
    private double balance;
    private boolean isResetting = false;

    public PaymentsViewModel() {
        salesController = KatanaFactory.getSalesController();
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
    public <T> void receiveDataFromView(String key, T data) {
        switch (key) {
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

    private List<SaleItemViewModel> getSaleItemsFromSaleEntities(List<Sale> sales) {
        List<SaleItemViewModel> saleViewModels = new ArrayList<>();

        for (Sale sale : sales) {
            SaleItemViewModel saleItemViewModel = new SaleItemViewModel(sale);
            saleViewModels.add(saleItemViewModel);
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

    public void requestSaveSale() {
        if (amountReceived < total) {
            this.getViewActionRequest().Invoke(ALERT_CREDIT, null);
        } else {
            getViewActionRequest().Invoke(HIDE_MAIN_PROGRESS, null);
            saveCurrentSales(null);
        }
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

    private void resetValues() {
        isResetting = true;
        saleItems.clear();
        setTotal(0D);
        setAmountReceived(0D);
        setLoadingData(false);
        isResetting = false;
    }
}
