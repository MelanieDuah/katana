package com.katana.ui.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.katana.ui.BR;
import com.katana.ui.R;
import com.katana.ui.support.KatanaAction;

import java.io.Serializable;

public abstract class BaseViewModel extends BaseObservable implements Serializable {
    private KatanaAction viewActionRequest;
    private boolean loadingData;

    public BaseViewModel() {
        super();
    }

    KatanaAction getViewActionRequest() {
        return viewActionRequest;
    }

    public void setViewActionRequest(KatanaAction viewActionRequest) {
        this.viewActionRequest = viewActionRequest;
    }

    public void initialize(boolean isFromSavedInstance) {
    }

    @Bindable
    public boolean isLoadingData() {
        return loadingData;
    }

    @Bindable
    public int getEmptyImageId() {
        return R.drawable.ic_price_tag;
    }

    void setLoadingData(boolean loadingData) {
        this.loadingData = loadingData;
        notifyPropertyChanged(BR.loadingData);
    }

    public int getEmptyString() {
        return R.string.noItems;
    }

    public <T> void receiveDataFromView(String key, T data) {
    }
}
