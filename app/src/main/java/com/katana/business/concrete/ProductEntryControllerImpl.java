package com.katana.business.concrete;

import com.katana.business.ProductEntryController;
import com.katana.dataaccess.DataAccess;
import com.katana.entities.Category;
import com.katana.entities.CostEntry;
import com.katana.entities.CostItem;
import com.katana.entities.InventoryItem;
import com.katana.entities.Product;
import com.katana.infrastructure.KatanaFactory;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.exceptions.KatanaDataException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AOwusu on 12/28/2017
 */

public class ProductEntryControllerImpl implements ProductEntryController {

    private DataAccess dataAccess;

    public ProductEntryControllerImpl() {
        this.dataAccess = KatanaFactory.getDataAccess();
    }

    @Override
    public OperationResult addCategory(String categoryName, OperationCallBack<Category> operationCallBack) throws KatanaBusinessException {

        OperationResult result;
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
    public OperationResult getAllCategories(OperationCallBack<Category> operationCallBack) throws KatanaBusinessException {
        try {
            return dataAccess.findAllItems(Category.class, operationCallBack);
        } catch (KatanaDataException ex) {
            throw new KatanaBusinessException("Could not retrieve product categories", ex);
        }
    }

    @Override
    public OperationResult findProductsInCategoryBetweenDateRange(String categoryId, Date startDate, Date endDate, OperationCallBack<AbstractMap.SimpleEntry<InventoryItem, Product>> operationCallBack) throws KatanaBusinessException {
        try {
            //First retrieve inventory items and retrieve the product per inventoryitem product id
            return dataAccess.findItemsInRangeOrderedByField(InventoryItem.class, "inventoryDate", String.valueOf(startDate.getTime()), String.valueOf(endDate.getTime()), new OperationCallBack<InventoryItem>() {
                @Override
                public void onCollectionOperationSuccessful(List<InventoryItem> results) {
                    List<AbstractMap.SimpleEntry<InventoryItem, Product>> inventoryProducts = new ArrayList<>();
                    if (results != null && results.size() > 0) {
                        InventoryItem inventoryItem;
                        int resultSize = results.size();
                        for (int i = 0; i < resultSize; i++) {
                            inventoryItem = results.get(i);
                            try {
                                int finalI = i;
                                InventoryItem finalInventoryItem = inventoryItem;
                                dataAccess.findItemByKey(Product.class, inventoryItem.getProductId(), new OperationCallBack<Product>() {
                                    @Override
                                    public void onOperationSuccessful(Product result) {
                                        if (result != null && result.getCategoryId().equals(categoryId))
                                            inventoryProducts.add(new AbstractMap.SimpleEntry<InventoryItem, Product>(finalInventoryItem, result));
                                        if (finalI + 1 >= resultSize)
                                            operationCallBack.onCollectionOperationSuccessful(inventoryProducts);
                                    }
                                });
                            } catch (KatanaDataException e) {
                                operationCallBack.onOperationFailed(e);
                            }
                        }
                    } else {
                        operationCallBack.onCollectionOperationSuccessful(inventoryProducts);
                    }
                }
            });
        } catch (KatanaDataException e) {
            throw new KatanaBusinessException("Error retrieving inventory item", e);
        }
    }

    @Override
    public OperationResult addProduct(String productName, int quantity, double price, Category category, String barcode, OperationCallBack<Product> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;
        try {
            Product product = new Product(productName, quantity, price, category, barcode);
            dataAccess.addDataItem(product, new OperationCallBack<Product>() {
                @Override
                public void onOperationSuccessful(Product result) {
                    operationCallBack.onOperationSuccessful(result);
                    InventoryItem inventoryItem = new InventoryItem(result.getId(), quantity);
                    try {
                        dataAccess.addDataItem(inventoryItem, null);
                    } catch (KatanaDataException e) {
                    }
                }
            });
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
    public OperationResult findAllProductsInCategory(String categoryId, OperationCallBack<Product> operationCallBack) throws KatanaBusinessException {
        OperationResult result;
        try {
            result = dataAccess.findAllItemsByField(Product.class, "categoryId", categoryId, operationCallBack);
        } catch (KatanaDataException ex) {
            throw new KatanaBusinessException("Error attempting to find product by barcode", ex);
        }
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
    public OperationResult getLastInsertedProduct(OperationCallBack<Product> operationCallBack) throws KatanaBusinessException {
        OperationResult result = OperationResult.FAILED;

        try {
            result = dataAccess.findLastInsertedItemFor(Product.class, operationCallBack);
        } catch (KatanaDataException e) {
            if (operationCallBack != null)
                operationCallBack.onOperationFailed(e);
        }
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
