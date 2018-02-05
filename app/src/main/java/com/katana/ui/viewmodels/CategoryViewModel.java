package com.katana.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.katana.business.ProductEntryController;
import com.katana.entities.Category;
import com.katana.entities.Product;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.ui.R;

import java.util.List;

import static com.katana.ui.support.Constants.REQUEST_ADD_CATEGORY;

/**
 * Created by AOwusu on 12/27/2017
 */

public class CategoryViewModel extends BaseViewModel {

    private ObservableArrayList<CategoryItemViewModel> categories;
    private String categoryName;
    private ProductEntryController productController;

    public CategoryViewModel() {
        categories = new ObservableArrayList<>();
        productController = KatanaFactory.getProductController();
    }

    @Override
    public void initialize(boolean isFromSavedInstance) {
        try {
            productController.getAllCategories(new OperationCallBack<Category>() {
                @Override
                public void onCollectionOperationSuccessful(List<Category> results) {
                    try {
                        for (Category category : results) {
                            productController.findAllProductsInCategory(category.getId(), new OperationCallBack<Product>() {
                                @Override
                                public void onCollectionOperationSuccessful(List<Product> results) {
                                    createAndAddNewCategoryViewModel(category, results);
                                }
                            });
                        }
                    } catch (KatanaBusinessException e) {
                        Log.e("CategoryViewModel", "Failed to retrieve products in category", e);
                    }
                    //categories.addAll(results);
                }
            });
        } catch (KatanaBusinessException ex) {
            Log.e("CategoryViewModel", "Failed to retrieve categories", ex);
        }
    }

    private void createAndAddNewCategoryViewModel(Category category, List<Product> products) {
        int totalQuantity = 0;
        double total = 0D;

        for (Product product : products) {
            int quantity = product.getQuantity();
            totalQuantity += quantity;
            total += (quantity * product.getPrice());
        }

        CategoryItemViewModel categoryItemViewModel = new CategoryItemViewModel(category.getCategoryName(), totalQuantity, total);
        categories.add(categoryItemViewModel);
    }

    @Bindable
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        notifyPropertyChanged(BR.categoryName);
    }

    @Bindable
    public ObservableArrayList<CategoryItemViewModel> getCategories() {
        return categories;
    }

    public void setCategories(ObservableArrayList<CategoryItemViewModel> categories) {
        this.categories = categories;
    }

    public void onAddNewCategoryRequested() {
        getViewActionRequest().Invoke(REQUEST_ADD_CATEGORY, null);
    }

    @Override
    public int getEmptyImageId() {
        return R.drawable.ic_package;
    }

    public void addNewCategory() {
        try {
            productController.addCategory(categoryName, new OperationCallBack<Category>() {
                @Override
                public void onOperationSuccessful(Category category) {
                    super.onOperationSuccessful(category);
                    if (category != null) {
                        categories.add(new CategoryItemViewModel(category.getCategoryName(), 0, 0D));
                    }
                }
            });
            setCategoryName("");
        } catch (KatanaBusinessException ex) {
            Log.e("CategoryViewModel", "Failed to add category", ex);
        }
    }
}
