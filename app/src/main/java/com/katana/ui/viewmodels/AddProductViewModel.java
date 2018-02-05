package com.katana.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.katana.business.ProductEntryController;
import com.katana.entities.Category;
import com.katana.entities.Product;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.Asserter;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.ui.support.Utils;

import java.util.List;

import static com.katana.ui.support.Constants.BARCODE;
import static com.katana.ui.support.Constants.EMPTY;
import static com.katana.ui.support.Constants.PRINT_REQUEST;
import static com.katana.ui.support.Constants.QUANTITY;
import static com.katana.ui.support.Constants.REQUEST_OCR;

/**
 * Created by AOwusu on 12/25/2017
 */

public class AddProductViewModel extends BaseViewModel {
    private String productName;
    private double price;
    private int quantity;
    private int selectedCategoryIndex;
    private ObservableArrayList<Category> categories;
    private ProductEntryController productController;
    private boolean isResetting = false;
    private Product lastInsertedProduct;

    public AddProductViewModel() {
        super();
        categories = new ObservableArrayList<>();
        categories.add(new Category("Category"));

        productController = KatanaFactory.getProductController();
    }

    @Bindable
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (!productName.equals(this.productName)) {
            this.productName = productName;
            notifyPropertyChanged(BR.productName);
        }
    }

    @Bindable
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        if (isResetting)
            notifyPropertyChanged(BR.price);
    }

    @Bindable
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (isResetting)
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
    public void initialize(boolean isFromSavedInstance) {
        if (!isFromSavedInstance) {
            AsyncTask.execute(() -> {

                try {
                    productController.getAllCategories(new OperationCallBack<Category>() {
                        @Override
                        public void onCollectionOperationSuccessful(List<Category> results) {
                            super.onCollectionOperationSuccessful(results);
                            categories.addAll(results);
                        }
                    });

                    productController.getLastInsertedProduct(new OperationCallBack<Product>() {

                        @Override
                        public void onOperationSuccessful(Product result) {
                            lastInsertedProduct = result;
                        }
                    });
                } catch (KatanaBusinessException e) {
                    Log.e("AddProductViewModel", "Error retrieving product categories", e);
                }
            });
        }
    }

    public void onAddProductRequested() {
        Category category = categories.get(selectedCategoryIndex);

        int lastproductId = getLastInsertedProductId();

        String currentBarcode = generateBarcodeString(lastproductId);

        try {
            productController.addProduct(productName, quantity, price, category,
                    currentBarcode, new OperationCallBack<Product>() {
                        @Override
                        public void onOperationSuccessful(Product result) {
                            lastInsertedProduct = result;

                            printBarcode(currentBarcode);
                            clearFields();
                        }
                    });
        } catch (KatanaBusinessException e) {
            Log.e("AddProductViewModel", "Could not add product", e);
        }
    }

    private int getLastInsertedProductId() {
        int lastproductId = 0;
        if (lastInsertedProduct != null) {
            String barcode = lastInsertedProduct.getBarcode();
            Asserter.doAssert(!(barcode == null && barcode.equals("")), "barcode is null or empty");
            lastproductId = Integer.parseInt(barcode.substring(barcode.length() - 1));
        }

        return lastproductId;
    }

    private void clearFields() {
        isResetting = true;
        setProductName(EMPTY);
        setPrice(0);
        setQuantity(0);
        isResetting = false;
    }

    private String generateBarcodeString(int lastItemId) {
        lastItemId++;
        String format = "%06d";
        return Utils.getBarcodePrefix() + String.format(format, lastItemId);
    }

    private void printBarcode(String barcode) {
        Bundle bundle = new Bundle();
        bundle.putString(BARCODE, barcode);
        bundle.putInt(QUANTITY, quantity);
        getViewActionRequest().Invoke(PRINT_REQUEST, bundle);
    }

    public void requestOCR() {
        getViewActionRequest().Invoke(REQUEST_OCR, null);
    }
}
