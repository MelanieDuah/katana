package com.katana.ui.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.katana.ui.support.KatanaAlertDialog;

import static com.katana.ui.support.Constants.BUTTON_METHODS;
import static com.katana.ui.support.Constants.BUTTON_MODES;

/**
 * Created by Akwasi Owusu on 1/10/18.
 */

public class KatanaDialogFragment extends DialogFragment {

    private KatanaAlertDialog.ButtonModes buttonModes;
    private KatanaAlertDialog.ButtonMethods buttonMethods;

    public static KatanaDialogFragment getInstance(KatanaAlertDialog.ButtonModes buttonModes, KatanaAlertDialog.ButtonMethods buttonMethods){
        KatanaDialogFragment dialogFragment = new KatanaDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(BUTTON_MODES, buttonModes);
        bundle.putSerializable(BUTTON_METHODS, buttonMethods);

        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        buttonMethods = (KatanaAlertDialog.ButtonMethods)bundle.getSerializable(BUTTON_METHODS);
        buttonModes = (KatanaAlertDialog.ButtonModes)bundle.getSerializable(BUTTON_MODES);
        
        KatanaAlertDialog alertDialog = new KatanaAlertDialog(getActivity(),buttonModes, buttonMethods);
        return alertDialog.create();
    }
}
