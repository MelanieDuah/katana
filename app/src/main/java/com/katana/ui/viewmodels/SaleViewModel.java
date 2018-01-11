package com.katana.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.katana.business.SalesController;
import com.katana.entities.Sale;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.ui.BR;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.katana.ui.support.Constants.BARCODES;

/**
 * Created by Akwasi Owusu on 1/7/18.
 */

public class SaleViewModel extends BaseViewModel {

    private SalesController salesController;
    private ObservableArrayList<SaleItemViewModel> saleItems;
    private PropertyChangeCallBack propertyChangeCallBack;
    private double total;

    public SaleViewModel() {
        salesController = KatanaFactory.getSalesController();
        propertyChangeCallBack = new PropertyChangeCallBack();
        saleItems = new ObservableArrayList<>();
    }

    @Bindable
    public ObservableArrayList<SaleItemViewModel> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(ObservableArrayList<SaleItemViewModel> saleItems) {
        this.saleItems = saleItems;
        notifyPropertyChanged(BR.saleItems);
    }

    @Bindable
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }

    @Override
    public <T> void receiveDataFromView(String key, T data) {
        switch (key) {
            case BARCODES:
                recordSalesFromBarcodes((Map<String, Integer>) data);
                break;
        }
    }

    private void recordSalesFromBarcodes(Map<String, Integer> barcodeData) {
        try {
            salesController.generateSaleItems(barcodeData, new OperationCallBack<Sale>() {
                @Override
                public void onCollectionOperationSuccessful(List<Sale> results) {
                    saleItems.addAll(getSaleItemsFromSaleEntities(results));
                    calculateTotal();
                }
            });
        } catch (KatanaBusinessException e) {
            Log.e("SaleViewModel", "Error loading saleItems", e);
        }
    }

    private List<SaleItemViewModel> getSaleItemsFromSaleEntities(List<Sale> sales) {
        List<SaleItemViewModel> saleViewModels = new ArrayList<>();

        for (Sale sale : sales) {
            SaleItemViewModel saleItemViewModel = new SaleItemViewModel(sale.getProduct().getProductName(), sale.getProduct().getCategoryName(), sale.getQuantitySold(), sale.getProduct().getPrice());
            saleItemViewModel.addOnPropertyChangedCallback(propertyChangeCallBack);
            saleViewModels.add(saleItemViewModel);
        }
        return saleViewModels;
    }

    private SaleItemViewModel getSaleItemViewModel() {
        SaleItemViewModel saleItemViewModel = new SaleItemViewModel();

        total += saleItemViewModel.getTotal();
        return saleItemViewModel;
    }

    private void calculateTotal(){
        int total = 0;
        for (SaleItemViewModel saleViewModel : saleItems) {
            total += saleViewModel.getTotal();
        }
        setTotal(total);
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
}
