package com.katana.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.katana.business.ProductEntryController;
import com.katana.entities.Category;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

import java.util.List;

/**
 * Created by AOwusu on 12/27/2017.
 */

public class CategoryViewModel extends BaseViewModel {

    private ObservableArrayList<Category> categories;
    private String categoryName;
    private ProductEntryController productController;

    public CategoryViewModel() {
        categories = new ObservableArrayList<>();
        productController = KatanaFactory.getProductController();
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            productController.getAllCategories(new OperationCallBack<Category>(){
                @Override
                public void onCollectionOperationSuccessful(List<Category> results) {
                    super.onCollectionOperationSuccessful(results);
                    categories.addAll(results);
                }
            });
        } catch (KatanaBusinessException ex) {
            Log.e(CategoryViewModel.class.getSimpleName(), "Failed to retrieve categories", ex);
        }
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
    public ObservableArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ObservableArrayList<Category> categories) {
        this.categories = categories;
        notifyPropertyChanged(BR.categories);
    }

    public void onAddNewCategoryRequested() {
        try {
            OperationResult result = productController.addCategory(categoryName, new OperationCallBack<Category>(){
                @Override
                public void onOperationSuccessful(Category category) {
                    super.onOperationSuccessful(category);
                    if(category != null){
                        categories.add(category);
                    }
                }
            });
            setCategoryName("");
        } catch (KatanaBusinessException ex) {
            Log.e(CategoryViewModel.class.getSimpleName(), "Failed to add category", ex);
        }
    }
}
