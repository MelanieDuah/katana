package com.katana.business;

import com.katana.entities.Category;
import com.katana.entities.CostEntry;
import com.katana.entities.CostItem;
import com.katana.entities.Product;
import com.katana.infrastructure.exceptions.KatanaBusinessException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

import java.util.List;

/**
 * Provides methods for adding product categories and products
 * @author Akwasi Owusu
 */
public interface ProductEntryController {

    OperationResult addCategory(String categoryName, OperationCallBack<Category> operationCallBack) throws KatanaBusinessException;

    Category findCategory(int id,
                          OperationCallBack<Category> operationCallBack)
            throws KatanaBusinessException;

    Category findCategory(String categoryName,
                          OperationCallBack<Category> operationCallBack)
            throws KatanaBusinessException;

    OperationResult getAllCategories(
            OperationCallBack<Category> operationCallBack)
            throws KatanaBusinessException;

    OperationResult addProduct(String productName, int quantity, double price,
                               Category category, String barcode, OperationCallBack<Product> operationCallBack) throws KatanaBusinessException;

    OperationResult removeProduct(int productId)
            throws KatanaBusinessException;

    OperationResult findAllProducts(
            OperationCallBack<Product> operationCallBack)
            throws KatanaBusinessException;

    Product findProduct(int productId,
                        OperationCallBack<Product> operationCallBack)
            throws KatanaBusinessException;

    Product findProductByBarcode(String barcodDigits,
                                 OperationCallBack<Product> operationCallBack)
            throws KatanaBusinessException;

    Product findProduct(String productName,
                        OperationCallBack<Product> operationCallBack)
            throws KatanaBusinessException;

    OperationResult updateProduct(Product product) throws KatanaBusinessException;

    void getLastInsertedProductId(OperationCallBack<Integer> operationCallBack) throws KatanaBusinessException;

    OperationResult saveCostEntries(List<CostEntry> costEntries) throws KatanaBusinessException;

    List<CostEntry> getAllCostEntries(
            OperationCallBack<CostEntry> operationCallBack)
            throws KatanaBusinessException;

    List<CostItem> getAllCostItems(
            OperationCallBack<CostItem> operationCallBack)
            throws KatanaBusinessException;
}
