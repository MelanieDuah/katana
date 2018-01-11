package com.katana.ui.support;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.ObservableArrayList;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.katana.entities.Category;
import com.katana.ui.R;
import com.katana.ui.databinding.LayoutSaleListBinding;
import com.katana.ui.viewmodels.SaleItemViewModel;

import java.text.DecimalFormat;

/**
 * Created by AOwusu on 12/27/2017.
 */

public class KatanaBindingAdapters {
    @BindingAdapter("productcategories")
    public static void setProductCategories(ListView view, ObservableArrayList<Category> list) {
        SingleTextListAdapter<Category> adapter = new SingleTextListAdapter<Category>(view.getContext(), list);
        view.setAdapter(adapter);
    }

    @BindingAdapter("spinneritems")
    public static void setCategorySpinneritems(Spinner view, ObservableArrayList<Category> list) {
        SingleTextListAdapter<Category> adapter = new SingleTextListAdapter<Category>(view.getContext(), list);
        view.setAdapter(adapter);
    }

    @BindingAdapter({"doubleText"})
    public static void setDoubleValue(EditText editText, double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedDecimalValue = decimalFormat.format(value);
        editText.setText(formattedDecimalValue);
    }

    @BindingAdapter({"doubleText"})
    public static void setDoubleValue(TextView textView, double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedDecimalValue = decimalFormat.format(value);
        textView.setText(formattedDecimalValue);
    }

    @InverseBindingAdapter(attribute = "doubleText", event = "textValueChanged")
    public static double getDoubleValue(EditText editText) {
        double value = 0D;
        if (!editText.getText().toString().equals(""))
            value = Double.parseDouble(editText.getText().toString());

        return value;
    }

    @BindingAdapter({"intText"})
    public static void setIntValue(EditText editText, int value) {
        editText.setText(Integer.toString(value));
    }

    @BindingAdapter({"intText"})
    public static void setIntValue(TextView textView, int value) {
        textView.setText(Integer.toString(value));
    }

    @InverseBindingAdapter(attribute = "intText", event = "textValueChanged")
    public static int getIntegerValue(EditText editText) {
        int value = 0;
        if (!editText.getText().toString().equals(""))
            value = Integer.parseInt(editText.getText().toString());

        return value;
    }

    @BindingAdapter(value = "textValueChanged")
    public static void setListener(EditText editText, final InverseBindingListener listener) {
        if (listener != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    listener.onChange();
                }
            });
        }
    }

    @BindingAdapter("salesitems")
    public static void setSaleItemListAdapter(ListView listView, ObservableArrayList<SaleItemViewModel> saleItemViewModels) {
        BindableListAdapter<SaleItemViewModel, LayoutSaleListBinding> saleItemAdapter = new BindableListAdapter<>(listView.getContext(), R.layout.layout_sale_list, saleItemViewModels);
        LayoutInflater inflater = (LayoutInflater) listView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            View emptyview = inflater.inflate(R.layout.layout_empty_sale_list, (ViewGroup) listView.getParent(), false);
            ((ViewGroup) listView.getParent()).addView(emptyview);
            listView.setEmptyView(emptyview);
        }
        listView.setAdapter(saleItemAdapter);
    }
}
