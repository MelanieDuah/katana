package com.katana.ui.views.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.katana.ui.R;
import com.katana.ui.databinding.FragmentInventoryBinding;
import com.katana.ui.support.KatanaDatePicker;
import com.katana.ui.viewmodels.InventoryViewModel;

import java.util.Calendar;
import java.util.Date;


public class InventoryFragment extends BaseFragment<FragmentInventoryBinding, InventoryViewModel> {

    private Date startDate;
    private Date endDate;

    public InventoryFragment() {
    }

    public static InventoryFragment newInstance() {
        return new InventoryFragment();
    }

    @Override
    protected int getLayoutReSource() {
        return R.layout.fragment_inventory;
    }

    @Override
    protected InventoryViewModel getViewModel() {
        return new InventoryViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        assert view != null;

        Button startDateButton = view.findViewById(R.id.startDate);
        Button endDateButton = view.findViewById(R.id.endDate);

        Button.OnClickListener clickListener = (v) -> getDatePicker((Button) v).show(getFragmentManager(), v.toString());

        startDateButton.setOnClickListener(clickListener);
        endDateButton.setOnClickListener(clickListener);
        return view;
    }

    private KatanaDatePicker getDatePicker(Button pickerButton) {
        return KatanaDatePicker.newInstance(getContext(), (date) -> onDateSet(pickerButton, date));
    }

    private void onDateSet(Button pickerButton, Date date) {
        int buttonId = pickerButton.getId();
        boolean isStartDate = buttonId == R.id.startDate;
        if (isStartDate) {
            startDate = date;
            viewModel.setSelectedStartDate(date);
            if (endDate == null || endDate.compareTo(date) <= 0) {
                endDate = date;
                viewModel.setSelectedEndDate(date);
            }
            viewModel.onDatesChanged();
        } else {
            if (startDate == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = calendar.getTime();
            }
            if (startDate.compareTo(date) <= 0) {
                endDate = date;
                viewModel.setSelectedEndDate(date);
                viewModel.onDatesChanged();
            }
        }
    }
}
