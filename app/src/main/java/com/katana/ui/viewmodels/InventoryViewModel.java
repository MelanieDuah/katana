package com.katana.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;

import com.katana.business.ProductEntryController;
import com.katana.entities.Category;
import com.katana.entities.InventoryItem;
import com.katana.entities.Product;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.ui.BR;

import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Akwasi Owusu on 1/16/18
 */

public class InventoryViewModel extends BaseViewModel {
    private Date selectedStartDate;
    private Date selectedEndDate;
    private int selectedCategoryIndex;
    private ObservableArrayList<Category> categories;
    private ObservableArrayList<InventoryItemViewModel> inventoryItems;
    private ProductEntryController productController;

    public InventoryViewModel() {
        super();

        categories = new ObservableArrayList<>();
        categories.add(new Category("Category"));

        inventoryItems = new ObservableArrayList<>();

        Calendar calendar = Calendar.getInstance();

        selectedEndDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        selectedStartDate = calendar.getTime();

        productController = KatanaFactory.getProductController();
    }

    @Bindable
    public String getStartDate() {
        return DateFormat.format("dd-MM-yyyy", selectedStartDate).toString();
    }

    @Bindable
    public String getEndDate() {
        return DateFormat.format("dd-MM-yyyy", selectedEndDate).toString();
    }

    @Bindable
    public ObservableArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ObservableArrayList<Category> categories) {
        this.categories = categories;
    }

    @Bindable
    public int getSelectedCategoryIndex() {
        return selectedCategoryIndex;
    }

    public void setSelectedCategoryIndex(int selectedCategoryIndex) {
        this.selectedCategoryIndex = selectedCategoryIndex;
    }

    @Bindable
    public ObservableArrayList<InventoryItemViewModel> getInventoryItems() {
        return inventoryItems;
    }

    public void setSelectedStartDate(Date date) {
        selectedStartDate = date;
        notifyPropertyChanged(BR.startDate);
    }

    public void setSelectedEndDate(Date date) {
        selectedEndDate = date;
        notifyPropertyChanged(BR.endDate);
    }

    public void onCategorySelected() {
        loadRequestedItems();
    }

    private void loadRequestedItems() {
        AsyncTask.execute(() -> {
            try {
                if (selectedCategoryIndex > 0) {
                    setLoadingData(true);
                    productController.findProductsInCategoryBetweenDateRange(categories.get(selectedCategoryIndex).getId(), selectedStartDate, selectedEndDate, new OperationCallBack<AbstractMap.SimpleEntry<InventoryItem, Product>>() {
                        @Override
                        public void onCollectionOperationSuccessful(List<AbstractMap.SimpleEntry<InventoryItem, Product>> results) {
                            inventoryItems.clear();
                            for (AbstractMap.SimpleEntry<InventoryItem, Product> entry : results) {
                                inventoryItems.add(new InventoryItemViewModel(entry.getKey(), entry.getValue()));
                            }

                            setLoadingData(false);
                        }
                    });
                }
            } catch (KatanaBusinessException e) {
                Log.e("InventoryViewModel", "Error retrieving products", e);
            }
        });
    }

    public void onDatesChanged() {
        loadRequestedItems();
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
                } catch (KatanaBusinessException e) {
                    Log.e("AddProductViewModel", "Error retrieving product categories", e);
                }
            });
        }
    }
}
