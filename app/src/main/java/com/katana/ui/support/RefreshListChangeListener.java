package com.katana.ui.support;

import android.databinding.ObservableList;

/**
 * Created by Akwasi Owusu on 1/10/18.
 */

public class RefreshListChangeListener extends ObservableList.OnListChangedCallback {

    private DataSetChangeNotificationReceiver datasetNotificationReceiver;

    public RefreshListChangeListener(DataSetChangeNotificationReceiver datasetNotificationReceiver) {
        super();
        this.datasetNotificationReceiver = datasetNotificationReceiver;
    }

    @Override
    public void onChanged(ObservableList observableList) {
        datasetNotificationReceiver.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList observableList, int i, int i1) {
        datasetNotificationReceiver.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeInserted(ObservableList observableList, int i, int i1) {
        datasetNotificationReceiver.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeMoved(ObservableList observableList, int i, int i1, int i2) {
        datasetNotificationReceiver.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeRemoved(ObservableList observableList, int i, int i1) {
        datasetNotificationReceiver.notifyDataSetChanged();
    }

    public interface DataSetChangeNotificationReceiver {
        void notifyDataSetChanged();
    }
}
