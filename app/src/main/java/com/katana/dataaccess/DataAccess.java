package com.katana.dataaccess;

import com.katana.entities.BaseEntity;
import com.katana.infrastructure.exceptions.KatanaDataException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

/**
 * Created by Akwasi Owusu on 11/16/17
 */

public interface DataAccess {
    <T extends BaseEntity> OperationResult addDataItem(T dataItem, OperationCallBack<T> operationCallBack) throws KatanaDataException;

    <T extends BaseEntity> OperationResult addDataItems(OperationCallBack<T> operationCallBack, T... dataItems) throws KatanaDataException;
    <T extends BaseEntity> OperationResult findAllItems(Class<T> itemClass, OperationCallBack<T> operationCallBack) throws KatanaDataException;

    <T extends BaseEntity> OperationResult findAllItemsByField(Class<T> itemClass, String fieldName, String fieldValue, OperationCallBack<T> operationCallBack) throws KatanaDataException;
    <T extends BaseEntity> OperationResult findItem(Class<T> itemClass, String fieldName, String fieldValue, OperationCallBack<T> operationCallBack) throws KatanaDataException;
    <T extends BaseEntity> OperationResult findItemByKey(Class<T> itemClass, String key, OperationCallBack<T> operationCallBack) throws KatanaDataException;

    <T extends BaseEntity> String createAndReturnNewIdFor(Class<T> itemClass, T dataItem) throws KatanaDataException;

    <T extends BaseEntity> OperationResult findLastInsertedItemFor(Class<T> itemClass, OperationCallBack<T> operationCallBack) throws KatanaDataException;

    <T extends BaseEntity> OperationResult findItemsInRange(Class<T> itemClass, String startPoint, String endPoint, OperationCallBack<T> operationCallBack) throws KatanaDataException;

    <T extends BaseEntity> OperationResult findItemsInRangeOrderedByField(Class<T> itemClass, String fieldName, String startPoint, String endPoint, OperationCallBack<T> operationCallBack) throws KatanaDataException;
}
