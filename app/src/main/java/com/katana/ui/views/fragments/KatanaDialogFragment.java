package com.katana.ui.views.fragments;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.katana.ui.BR;
import com.katana.ui.support.KatanaAlertDialog;
import com.katana.ui.viewmodels.BaseViewModel;

import static com.katana.ui.support.Constants.BUTTON_METHODS;
import static com.katana.ui.support.Constants.BUTTON_MODES;
import static com.katana.ui.support.Constants.CUSTOM_DIALOG_VIEW;
import static com.katana.ui.support.Constants.CUSTOM_DIALOG_VIEWMODEL;
import static com.katana.ui.support.Constants.MESSAGE;
import static com.katana.ui.support.Constants.TITLE;

/**
 * Created by Akwasi Owusu on 1/10/18
 */

public class KatanaDialogFragment extends DialogFragment {

    private int width;
    private int height;

    public static KatanaDialogFragment getInstance(int title, int message, KatanaAlertDialog.ButtonModes buttonModes, KatanaAlertDialog.ButtonMethods buttonMethods) {
        KatanaDialogFragment dialogFragment = new KatanaDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(BUTTON_MODES, buttonModes);
        bundle.putSerializable(BUTTON_METHODS, buttonMethods);
        bundle.putInt(TITLE, title);
        if (message != 0)
            bundle.putInt(MESSAGE, message);

        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }

    public static KatanaDialogFragment getInstance(int title, KatanaAlertDialog.ButtonModes buttonModes, KatanaAlertDialog.ButtonMethods buttonMethods, int customViewId, BaseViewModel viewModel) {
        KatanaDialogFragment dialogFragment = KatanaDialogFragment.getInstance(title, 0, buttonModes, buttonMethods);

        Bundle bundle = dialogFragment.getArguments();
        assert bundle != null;
        bundle.putInt(CUSTOM_DIALOG_VIEW, customViewId);
        bundle.putSerializable(CUSTOM_DIALOG_VIEWMODEL, viewModel);

        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;
        KatanaAlertDialog.ButtonMethods buttonMethods = (KatanaAlertDialog.ButtonMethods) bundle.getSerializable(BUTTON_METHODS);
        KatanaAlertDialog.ButtonModes buttonModes = (KatanaAlertDialog.ButtonModes) bundle.getSerializable(BUTTON_MODES);

        KatanaAlertDialog alertDialog = new KatanaAlertDialog(getActivity(), buttonModes, buttonMethods);

        assert bundle.containsKey(TITLE);
        alertDialog.setTitle(bundle.getInt(TITLE));

        if (bundle.containsKey(MESSAGE)) {
            alertDialog.setMessage(bundle.getInt(MESSAGE));
        }

        if (bundle.containsKey(CUSTOM_DIALOG_VIEW) && bundle.containsKey(CUSTOM_DIALOG_VIEWMODEL)) {
            ViewDataBinding binding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), bundle.getInt(CUSTOM_DIALOG_VIEW), null, false);
            binding.setVariable(BR.viewmodel, bundle.getSerializable(CUSTOM_DIALOG_VIEWMODEL));
            alertDialog.setView(binding.getRoot());
        }

        return alertDialog.create();
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && width > 0 && height > 0) {
            getDialog().getWindow().setLayout(width, height);
        }
    }
}
