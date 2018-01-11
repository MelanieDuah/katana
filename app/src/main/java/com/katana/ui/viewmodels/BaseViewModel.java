package com.katana.ui.viewmodels;

import android.databinding.BaseObservable;

import com.katana.ui.support.KatanaAction;

public abstract class BaseViewModel extends BaseObservable {
  private KatanaAction activityAction;

    public BaseViewModel() {
    }

    public KatanaAction getActivityAction() {
        return activityAction;
    }

    public void setActivityAction(KatanaAction activityAction) {
        this.activityAction = activityAction;
    }

    protected void getContext(){}
    public void initialize(){}
    public <T> void receiveDataFromView(String key, T data){}
}
