package com.katana.datacess;

import com.katana.infrastructure.exceptions.KatanaDataException;
import com.katana.infrastructure.support.OperationResult;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

public interface DataAccess {
    <T> OperationResult addDataItem(T dataItem) throws KatanaDataException;
}
