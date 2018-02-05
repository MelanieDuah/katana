package com.katana.ui.viewmodels;

import android.databinding.Bindable;

import com.katana.ui.BR;

/**
 * Created by AOwusu on 12/24/2017
 */

public class MainActivityViewModel extends BaseViewModel {

    private boolean showProgress = false;

    public MainActivityViewModel() {
    }

    @Bindable
    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        notifyPropertyChanged(BR.showProgress);
    }
}
