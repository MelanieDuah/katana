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
import android.widget.Filter;

import com.katana.ui.BR;
import com.katana.ui.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akwasi Owusu on 1/8/18
 */

public class BindableListAdapter<T extends BaseViewModel, B extends ViewDataBinding> extends ArrayAdapter<T> implements RefreshListChangeListener.DataSetChangeNotificationReceiver {
    private Context context;
    private int layout;
    private List<T> originalItems = new ArrayList<>();
    private List<T> items;

    BindableListAdapter(@NonNull Context context, int layout, @NonNull List<T> items, boolean isSearchable) {
        super(context, layout, items);
        this.context = context;
        this.layout = layout;

        this.items = items;

        if (isSearchable)
            originalItems.addAll(items);

        ((ObservableArrayList) items).addOnListChangedCallback(new RefreshListChangeListener(this));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        B binding = convertView == null ? DataBindingUtil.inflate(inflater, layout, parent, false)
                : DataBindingUtil.findBinding(convertView);
        T item = getItem(position);
        binding.setVariable(BR.viewmodel, item);

        return binding.getRoot();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResult = new FilterResults();
                List<T> filteredItems = new ArrayList<>();

                if (constraint != null) {

                    for (T item : originalItems) {
                        String pattern = ".*" + constraint.toString().toLowerCase() + ".*";
                        if (item.toString().toLowerCase().matches(pattern)) {
                            filteredItems.add(item);
                        }
                    }
                    filterResult.values = filteredItems;
                } else {
                    filterResult.values = originalItems;
                }
                return filterResult;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                items.clear();
                if (filterResults.values != null) {
                    items.addAll((List<T>) filterResults.values);
                    notifyDataSetChanged();
                }
            }
        };
    }

}
