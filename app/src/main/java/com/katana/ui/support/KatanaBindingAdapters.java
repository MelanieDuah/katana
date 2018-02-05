package com.katana.ui.support;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.ObservableArrayList;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.katana.entities.Category;
import com.katana.infrastructure.support.ActionFunc;
import com.katana.infrastructure.support.ActionFuncWithOneParam;
import com.katana.infrastructure.support.Asserter;
import com.katana.ui.R;
import com.katana.ui.viewmodels.BaseListItemViewModel;
import com.katana.ui.viewmodels.BaseViewModel;
import com.katana.ui.viewmodels.CategoryItemViewModel;
import com.katana.ui.viewmodels.CustomerItemViewModel;
import com.katana.ui.viewmodels.InventoryItemViewModel;
import com.katana.ui.viewmodels.SaleItemViewModel;

import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.Locale;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;

/**
 * Created by AOwusu on 12/27/2017
 */

public class KatanaBindingAdapters {

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter({"android:background"})
    public static void setBackgroundResource(Button button, int resource) {
        button.setBackgroundResource(resource);
    }

    @BindingAdapter({"android:drawableLeft"})
    public static void setDrawableLeftResource(Button button, int resource) {
        button.setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
    }

    @BindingAdapter({"android:drawableRight"})
    public static void setDrawableRightResource(Button button, int resource) {
        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, resource, 0);
    }

    @BindingAdapter({"android:text"})
    public static void setStringResource(TextView textView, int resource) {
        textView.setText(resource);
    }


    @BindingAdapter("spinneritems")
    public static void setCategorySpinneritems(Spinner view, ObservableArrayList<Category> list) {
        SingleTextListAdapter<Category> adapter = new SingleTextListAdapter<>(view.getContext(), list);
        view.setAdapter(adapter);
    }

    @BindingAdapter({"doubleText"})
    public static void setDoubleValue(EditText editText, double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedDecimalValue = value == 0D ? "" : decimalFormat.format(value);
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
        String text = editText.getText().toString();

        if (text.matches("^\\d+\\.?\\d{0,2}$"))
            value = Double.parseDouble(editText.getText().toString());

        return value;
    }

    @BindingAdapter({"intText"})
    public static void setIntValue(EditText editText, int value) {
        editText.setText(value > 0 ? Integer.toString(value) : "");
    }

    @BindingAdapter({"intText"})
    public static void setIntValue(TextView textView, int value) {
        textView.setText(String.format(Locale.getDefault(), "%d", value));
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

    @BindingAdapter("suggestions")
    public static void setCustomerAutoComplete(AutoCompleteTextView autoCompleteTextView, ObservableArrayList<CustomerItemViewModel> items) {
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(autoCompleteTextView.getContext(), R.layout.layout_single_textview, items));
    }

    @BindingAdapter("productcategories")
    public static void setProductCategories(RecyclerView recyclerView, ObservableArrayList<CategoryItemViewModel> items) {
        setupRecyclerView(recyclerView, items, R.layout.layout_category_list);
    }

    @BindingAdapter("salesitems")
    public static void setSaleItemListAdapter(RecyclerView recyclerView, ObservableArrayList<SaleItemViewModel> items) {
        setupRecyclerView(recyclerView, items, R.layout.layout_sale_list);
    }

    @BindingAdapter("inventoryitems")
    public static void setInventoryItemListAdapter(RecyclerView recyclerView, ObservableArrayList<InventoryItemViewModel> items) {
        setupRecyclerView(recyclerView, items, R.layout.layout_inventory_item);
    }

    @BindingAdapter("customeritems")
    public static void setCustomerListAdapter(RecyclerView recyclerView, ObservableArrayList<CustomerItemViewModel> items) {
        setupRecyclerView(recyclerView, items, R.layout.layout_customer_item);
    }

    @BindingAdapter("swipedirection")
    public static void setSwipeDirection(RecyclerView recyclerView, int direction) {
        ViewDataBinding binding = DataBindingUtil.findBinding(recyclerView);
        if (binding != null)
            binding.addOnRebindCallback(new OnRebindCallback() {
                @Override
                public void onBound(ViewDataBinding binding) {
                    BindableRecyclerViewAdapter adapter = (BindableRecyclerViewAdapter) recyclerView.getAdapter();
                    if (adapter != null)
                        adapter.setSwipeDir(direction);
                }
            });
    }

    private static <T extends BaseViewModel> void setupRecyclerView(RecyclerView recyclerView, ObservableArrayList<T> items, int layout) {
        BindableRecyclerViewAdapter recyclerViewAdapter = new BindableRecyclerViewAdapter<>(items, layout);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        setEmptyView(recyclerView, recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerViewAdapter);
        RecycleItemTouchCallback touchCallback = new RecycleItemTouchCallback();
        touchCallback.setSwipedAction(KatanaBindingAdapters::onRecyclerViewSwiped);
        new ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView);

    }

    private static <T> void onRecyclerViewSwiped(T param) {
        Asserter.doAssert(param instanceof AbstractMap.SimpleEntry, "onRecyclerViewSwiped -> param instance of AbstractMap.SimpleEntry");
        AbstractMap.SimpleEntry<Integer, RecyclerView.ViewHolder> keyValuePair = (AbstractMap.SimpleEntry) param;

        if (param != null) {
            int direction = keyValuePair.getKey();
            BindableRecyclerViewAdapter.KatanaRecyclerViewHolder viewHolder = (BindableRecyclerViewAdapter.KatanaRecyclerViewHolder) keyValuePair.getValue();
            BaseViewModel baseViewModel = viewHolder.getViewModel();

            if (baseViewModel instanceof BaseListItemViewModel) {
                BaseListItemViewModel listItemViewModel = (BaseListItemViewModel) baseViewModel;
                if (listItemViewModel != null) {
                    ActionFuncWithOneParam swipeAction = direction == LEFT ? listItemViewModel.getSwipeLeftAction() : listItemViewModel.getSwipeRightAction();
                    if (swipeAction != null)
                        swipeAction.invoke(viewHolder.getAdapterPosition());
                }
            }
        }
    }


    private static void setEmptyView(RecyclerView recyclerView, BindableRecyclerViewAdapter recyclerViewAdapter) {
        View emptyView = ((ViewGroup) recyclerView.getParent()).findViewById(R.id.emptylistview);

        recyclerViewAdapter.setAdapterObserverAction(() -> {
            if (emptyView != null) {
                int itemCount = recyclerViewAdapter.getItemCount();
                recyclerView.setVisibility(itemCount > 0 ? View.VISIBLE : View.GONE);
                emptyView.setVisibility(itemCount > 0 ? View.GONE : View.VISIBLE);
            }
        });
    }
//
//    @BindingAdapter("selectedIndex")
//    public static void setRcyclerSelectedIndex(RecyclerView recyclerView, int selectedIndex) {
//        BindableRecyclerViewAdapter adapter = (BindableRecyclerViewAdapter) recyclerView.getAdapter();
//        if (adapter != null && adapter.getSelectedIndex() != selectedIndex)
//            adapter.setSelectedIndex(selectedIndex);
//    }

    @BindingAdapter("onItemSelected")
    public static void setOnItemSelected(RecyclerView recyclerView, ActionFuncWithOneParam actionFunc) {
        ViewDataBinding binding = DataBindingUtil.findBinding(recyclerView);
        if (binding != null)
            binding.addOnRebindCallback(new OnRebindCallback() {
                @Override
                public void onBound(ViewDataBinding binding) {
                    BindableRecyclerViewAdapter adapter = (BindableRecyclerViewAdapter) recyclerView.getAdapter();
                    if (adapter != null && adapter.getItemCount() > 0)
                        adapter.addSelectionChangedAction(actionFunc);
                }
            });
    }

    @BindingAdapter("android:onClick")
    public static void setBoundButtonClicked(Button button, ActionFuncWithOneParam actionFunc) {
        button.setOnClickListener(view -> actionFunc.invoke(null));
    }

    @InverseBindingAdapter(attribute = "selectedIndex", event = "selectedIndexAttrChanged")
    public static int getSelectedItemPosition(RecyclerView recyclerView) {
        return ((BindableRecyclerViewAdapter) recyclerView.getAdapter()).getSelectedIndex();
    }

    @BindingAdapter(value = "selectedIndexAttrChanged")
    public static void setRecyclerSelectedIndexListener(RecyclerView recyclerView, final InverseBindingListener listener) {
        if (listener != null) {
            ViewDataBinding binding = DataBindingUtil.findBinding(recyclerView);
            if (binding != null)
                binding.addOnRebindCallback(new OnRebindCallback() {
                    @Override
                    public void onBound(ViewDataBinding binding) {
                        BindableRecyclerViewAdapter adapter = (BindableRecyclerViewAdapter) recyclerView.getAdapter();
                        if (adapter != null && adapter.getItemCount() > 0)
                            adapter.addSelectionChangedAction((ActionFunc) listener::onChange);
                    }
                });
        }
    }
}
