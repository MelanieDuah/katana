package com.katana.ui.support;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class KatanaDatePicker extends DialogFragment {

    private Context context;
    private DateReceiver dateReceiver;

    public KatanaDatePicker() {
        super();
    }

    public static KatanaDatePicker newInstance(Context context, DateReceiver dateReceiver) {
        KatanaDatePicker picker = new KatanaDatePicker();

        picker.setContext(context);
        picker.setDateReceiver(dateReceiver);

        return picker;
    }

    public void setDateReceiver(DateReceiver dateReceiver) {
        this.dateReceiver = dateReceiver;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            final Calendar c = Calendar.getInstance(TimeZone.getDefault());
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(context, this::onDateSet, year, month,
                    day);
        }
        return super.onCreateDialog(savedInstanceState);
    }

    private void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        calendar.set(year, month, day);
        Date newDate = calendar.getTime();

        assert dateReceiver != null;
        dateReceiver.receive(newDate);
    }

    public interface DateReceiver {
        void receive(Date date);
    }
}
