package com.katana.ui.support;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.ObservableArrayList;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.katana.entities.Category;

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

        String currentValue = editText.getText().toString();

        try {

            if (Double.valueOf(currentValue) != value) {
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String val = decimalFormat.format(value);
                editText.setText(val);
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    @InverseBindingAdapter(attribute = "doubleText", event = "textValueChanged")
    public static double getDoubleValue(EditText editText) {
        return Double.parseDouble(editText.getText().toString());
    }

    @BindingAdapter({"intText"})
    public static void setIntValue(EditText editText, int value) {

        String currentValue = editText.getText().toString();

        try {

            if (Integer.valueOf(currentValue) != value) {

                editText.setText(Integer.toString(value));
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    @InverseBindingAdapter(attribute = "intText", event = "textValueChanged")
    public static int getIntegerValue(EditText editText) {
        int value = 0;
        if(!editText.getText().toString().equals(""))
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
}
