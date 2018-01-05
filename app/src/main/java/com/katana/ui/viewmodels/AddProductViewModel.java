package com.katana.ui.viewmodels;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.os.Bundle;

import com.android.databinding.library.baseAdapters.BR;
import com.katana.business.ProductEntryController;
import com.katana.entities.Category;
import com.katana.entities.Product;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.ui.R;
import com.katana.ui.support.BarcodePrintHelper;
import com.katana.ui.support.Utils;

import static com.katana.ui.support.Constants.*;

import java.util.List;
import java.util.Map;

/**
 * Created by AOwusu on 12/25/2017.
 */

public class AddProductViewModel extends BaseViewModel {
    private String productName;
    private double price;
    private int quantity;
    private int selectedCategoryIndex;
    private ObservableArrayList<Category> categories;
    private ProductEntryController productController;
    private BarcodePrintHelper barcodePrintHelper;

    public AddProductViewModel() {
        categories = new ObservableArrayList<>();
        categories.add(new Category("Category"));

        productController = KatanaFactory.getProductController();
    }

    @Bindable
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
        notifyPropertyChanged(BR.productName);
    }

    @Bindable
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantity);
    }

    @Bindable
    public ObservableArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ObservableArrayList<Category> categories) {
        this.categories = categories;
        notifyPropertyChanged(BR.categories);
    }

    @Bindable
    public int getSelectedCategoryIndex() {
        return selectedCategoryIndex;
    }

    public void setSelectedCategoryIndex(int selectedCategoryIndex) {
        this.selectedCategoryIndex = selectedCategoryIndex;
    }

    @Override
    public void Initialize() {
        super.Initialize();
        try {
            productController.getAllCategories(new OperationCallBack<Category>() {
                @Override
                public void onCollectionOperationSuccessful(List<Category> results) {
                    super.onCollectionOperationSuccessful(results);
                    categories.addAll(results);
                }
            });
        } catch (KatanaBusinessException ex) {
        }
    }

    public void onAddProductRequested() {
        Category category = categories.get(selectedCategoryIndex);
        int lastproductId = 1;
        String currentBarcode = generateBarcodeString(lastproductId);

        try {
            productController.addProduct(productName, quantity, price, category,
                    currentBarcode, new OperationCallBack<Product>() {
                        @Override
                        public void onOperationSuccessful(Product result) {
                            printBarcode(currentBarcode);
                            clearFields();
                        }
                    });
        } catch (KatanaBusinessException e) {
        }
    }

    private void clearFields(){
      setProductName(EMPTY);
      setPrice(0);
      setQuantity(0);
    }

    private String generateBarcodeString(int lastItemId) {
        lastItemId++;
        String format = "%06d";
        return Utils.getBarcodePrefix() + String.format(format, lastItemId);
    }

    private void printBarcode(String barcode) {

        barcodePrintHelper = new BarcodePrintHelper(getActivityAction().Invoke(GET_CONTEXT), false);
        if (barcodePrintHelper != null) {
            barcodePrintHelper.printBarcode(barcode, quantity, null);
        }
    }
}
