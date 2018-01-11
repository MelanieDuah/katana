package com.katana.infrastructure.support;

import android.util.Log;

import java.util.List;

public class OperationCallBack<T> {

    public void onCollectionOperationSuccessful(List<T> results) {
    }

    public void onOperationSuccessful(T result) {
    }

    public void onOperationFailed(Throwable e) {

        Log.e("OperationCallBack", e.getMessage(),e);
    }
}
