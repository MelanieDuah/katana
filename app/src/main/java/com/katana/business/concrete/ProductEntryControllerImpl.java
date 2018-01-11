package com.katana.business.concrete;

import com.katana.business.ProductEntryController;
import com.katana.dataaccess.DataAccess;
import com.katana.entities.Category;
import com.katana.entities.CostEntry;
import com.katana.entities.CostItem;
import com.katana.entities.Product;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.exceptions.KatanaDataException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

import java.util.List;

/**
 * Created by AOwusu on 12/28/2017.
 */

public class ProductEntryControllerImpl implements ProductEntryController {

    private DataAccess dataAccess;

    public ProductEntryControllerImpl() {
        this.dataAccess = KatanaFactory.getDataAccess();
    }

    @Override
    public OperationResult addCategory(String categoryName, OperationCallBack<Category> operationCallBack) throws KatanaBusinessException {

        OperationResult result = OperationResult.FAILED;
        try {
            Category category = new Category(categoryName);
            dataAccess.addDataItem(category, operationCallBack);
            result = OperationResult.SUCCESSFUL;
        } catch (KatanaDataException ex) {
            throw new KatanaBusinessException("failed to add new product category", ex);
        }
        return result;
    }

    @Override
    public Category findCategory(int id, OperationCallBack<Category> operationCallBack) throws KatanaBusinessException {
        return null;
    }

    @Override
    public Category findCategory(String categoryName, OperationCallBack<Category> operationCallBack) throws KatanaBusinessException {
        return null;
    }

    @Override
    public OperationResult getAllCategories(OperationCallBack<Category> operationCallBack) throws KatanaBusinessException {
        try {
            return dataAccess.findAllItems(Category.class, operationCallBack);
        } catch (KatanaDataException ex) {
            throw new KatanaBusinessException("Could not retrieve product categories", ex);
        }
    }

    @Override
    public OperationResult addProduct(String productName, int quantity, double price, Category category, String barcode, OperationCallBack<Product> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;

        try {
            Product product = new Product(productName, quantity, price, category, barcode);
            dataAccess.addDataItem(product, operationCallBack);
            result = OperationResult.SUCCESSFUL;
        } catch (KatanaDataException ex) {

        }
        return result;
    }

    @Override
    public OperationResult removeProduct(int productId) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        return result;
    }

    @Override
    public OperationResult findAllProducts(OperationCallBack<Product> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        return result;
    }

    @Override
    public OperationResult findProduct(int productId, OperationCallBack<Product> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        return result;
    }

    @Override
    public OperationResult findProductByBarcode(String barcode, OperationCallBack<Product> operationCallBack) throws KatanaBusinessException {
        OperationResult result;
        try {
            result = dataAccess.findItem(Product.class, "barcode", barcode, new OperationCallBack<Product>() {
                @Override
                public void onOperationSuccessful(Product product) {
                    try {
                        if (product == null)
                            operationCallBack.onOperationSuccessful(null);
                        else
                            dataAccess.findItemByKey(Category.class, product.getCategoryId(), new OperationCallBack<Category>() {
                                @Override
                                public void onOperationSuccessful(Category result) {
                                    product.setCategoryName(result.getCategoryName());
                                    operationCallBack.onOperationSuccessful(product);
                                }
                            });
                    } catch (KatanaDataException e) {
                        onOperationFailed(e);
                    }
                }
            });
        } catch (KatanaDataException ex) {
            throw new KatanaBusinessException("Error attempting to find product by barcode", ex);
        }
        return result;
    }

    @Override
    public OperationResult findProduct(String productName, OperationCallBack<Product> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        return result;
    }

    @Override
    public OperationResult updateProduct(Product product) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        return result;
    }

    @Override
    public OperationResult getLastInsertedProductId(OperationCallBack<Integer> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        return result;
    }

    @Override
    public OperationResult saveCostEntries(List<CostEntry> costEntries) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        return result;
    }

    @Override
    public OperationResult getAllCostEntries(OperationCallBack<CostEntry> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        return result;
    }

    @Override
    public OperationResult getAllCostItems(OperationCallBack<CostItem> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        return result;
    }
}
