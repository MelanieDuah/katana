package com.katana.ui.support;

import android.databinding.ObservableList;
import android.widget.ArrayAdapter;

/**
 * Created by Akwasi Owusu on 1/10/18.
 */

public class RefreshListChangeListener extends ObservableList.OnListChangedCallback {

    private ArrayAdapter arrayAdapter;

    public RefreshListChangeListener(ArrayAdapter arrayAdapter) {
        super();
        this.arrayAdapter = arrayAdapter;
    }

    @Override
    public void onChanged(ObservableList observableList) {
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList observableList, int i, int i1) {
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeInserted(ObservableList observableList, int i, int i1) {
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeMoved(ObservableList observableList, int i, int i1, int i2) {
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeRemoved(ObservableList observableList, int i, int i1) {
        arrayAdapter.notifyDataSetChanged();
    }
}
