package com.katana.ui.viewmodels;

import android.databinding.Bindable;

import com.katana.infrastructure.support.ActionFuncWithOneParam;
import com.katana.ui.R;

/**
 * Created by Akwasi Owusu on 1/27/18
 */

public abstract class BaseListItemViewModel extends BaseViewModel {

    private ActionFuncWithOneParam swipeLeftAction;
    private ActionFuncWithOneParam swipeRightAction;

    @Bindable
    public int getLeftSwipeIcon() {
        return 0;
    }

    @Bindable
    public int getRightSwipeIcon() {
        return 0;
    }

    @Bindable
    public int getLeftSwipeLabel() {
        return 0;
    }

    @Bindable
    public int getRightSwipeLabel() {
        return 0;
    }

    @Bindable
    public int getLeftSwipeBackground() {
        return R.color.scannerRed;
    }

    @Bindable
    public int getRightSwipeBackground() {
        return 0;
    }

    public ActionFuncWithOneParam getSwipeLeftAction() {
        return swipeLeftAction;
    }

    public void setSwipeLeftAction(ActionFuncWithOneParam swipeLeftAction) {
        this.swipeLeftAction = swipeLeftAction;
    }

    public ActionFuncWithOneParam getSwipeRightAction() {
        return swipeRightAction;
    }

    public void setSwipeRightAction(ActionFuncWithOneParam swipeRightAction) {
        this.swipeRightAction = swipeRightAction;
    }
}
