package com.katana.dataaccess;

import com.katana.entities.BaseEntity;
import com.katana.infrastructure.exceptions.KatanaDataException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

import java.util.List;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

public interface DataAccess {
    <T extends BaseEntity> OperationResult addDataItem(T dataItem, OperationCallBack<T> operationCallBack) throws KatanaDataException;
    <T extends BaseEntity> OperationResult findAllItems(Class<T> itemClass, OperationCallBack<T> operationCallBack) throws KatanaDataException;
}
