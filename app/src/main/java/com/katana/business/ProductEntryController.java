package com.katana.business;

import com.katana.entities.Category;
import com.katana.entities.CostEntry;
import com.katana.entities.CostItem;
import com.katana.entities.InventoryItem;
import com.katana.entities.Product;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

import java.util.AbstractMap;
import java.util.Date;
import java.util.List;

/**
 * Provides methods for adding product categories and products
 * @author Akwasi Owusu
 */
public interface ProductEntryController {

    OperationResult addCategory(String categoryName, OperationCallBack<Category> operationCallBack) throws KatanaBusinessException;

    OperationResult getAllCategories(
            OperationCallBack<Category> operationCallBack)
            throws KatanaBusinessException;

    OperationResult findProductsInCategoryBetweenDateRange(String categoryId, Date startDate, Date endDate, OperationCallBack<AbstractMap.SimpleEntry<InventoryItem, Product>> operationCallBack) throws KatanaBusinessException;

    OperationResult addProduct(String productName, int quantity, double price,
                               Category category, String barcode, OperationCallBack<Product> operationCallBack) throws KatanaBusinessException;

    OperationResult removeProduct(int productId)
            throws KatanaBusinessException;

    OperationResult findAllProductsInCategory(String categoryId,
                                              OperationCallBack<Product> operationCallBack)
            throws KatanaBusinessException;

    OperationResult findProduct(int productId,
                        OperationCallBack<Product> operationCallBack)
            throws KatanaBusinessException;

    OperationResult findProductByBarcode(String barcodDigits,
                                 OperationCallBack<Product> operationCallBack)
            throws KatanaBusinessException;

    OperationResult findProduct(String productName,
                        OperationCallBack<Product> operationCallBack)
            throws KatanaBusinessException;

    OperationResult updateProduct(Product product) throws KatanaBusinessException;

    OperationResult getLastInsertedProduct(OperationCallBack<Product> operationCallBack) throws KatanaBusinessException;

    OperationResult saveCostEntries(List<CostEntry> costEntries) throws KatanaBusinessException;

    OperationResult getAllCostEntries(
            OperationCallBack<CostEntry> operationCallBack)
            throws KatanaBusinessException;

    OperationResult getAllCostItems(
            OperationCallBack<CostItem> operationCallBack)
            throws KatanaBusinessException;
}
