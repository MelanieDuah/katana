package com.katana.ui.support;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.katana.ui.BR;
import com.katana.ui.viewmodels.BaseViewModel;

import java.util.List;

/**
 * Created by Akwasi Owusu on 1/8/18.
 */

public class BindableListAdapter<T extends BaseViewModel, B extends ViewDataBinding> extends ArrayAdapter<T> {
    private Context context;
    private int layout;

    public BindableListAdapter(@NonNull Context context, int layout, @NonNull List<T> items) {
        super(context, layout, items);
        this.context = context;
        this.layout = layout;
        ((ObservableArrayList)items).addOnListChangedCallback(new RefreshListChangeListener(this));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        B binding = DataBindingUtil.inflate(inflater,layout,parent,false);
        T item = getItem(position);
        binding.setVariable(BR.viewmodel, item);

        return binding.getRoot();
    }
}
