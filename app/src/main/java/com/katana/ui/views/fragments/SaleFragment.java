package com.katana.ui.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import com.katana.ui.R;
import com.katana.ui.databinding.FragmentSaleBinding;
import com.katana.ui.support.KatanaAlertDialog;
import com.katana.ui.viewmodels.SaleViewModel;
import com.katana.ui.views.activities.ScanBarcodeActivity;

import static android.app.Activity.RESULT_OK;
import static com.katana.ui.support.Constants.ALERT_CREDIT;
import static com.katana.ui.support.Constants.BARCODES;
import static com.katana.ui.support.Constants.BARCODE_SCAN;
import static com.katana.ui.support.Constants.NO_CUSTOMER_DESIRED;
import static com.katana.ui.support.Constants.SCAN_REQUEST_CODE;

public class SaleFragment extends BaseFragment<FragmentSaleBinding, SaleViewModel> {

    public SaleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutReSource() {
        return R.layout.fragment_sale;
    }

    @Override
    protected SaleViewModel getViewModel() {
        return viewModel == null ? new SaleViewModel() : viewModel;
    }

    public static SaleFragment newInstance() {
        return new SaleFragment();
    }

    public void launchBarcodeScanner() {
        Intent intent = new Intent(getContext(), ScanBarcodeActivity.class);
        startActivityForResult(intent, SCAN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SCAN_REQUEST_CODE:
                    viewModel.receiveDataFromView(BARCODES, data.getExtras().getSerializable(BARCODES));
            }
        }
    }

    @Override
    protected void OnViewActionInvoked(String param, Bundle bundle) {
        switch (param) {
            case ALERT_CREDIT:
                KatanaDialogFragment dialog = makeCreditAlertDialog();
                dialog.show(getFragmentManager(), ALERT_CREDIT);
                break;
            case BARCODE_SCAN:
                launchBarcodeScanner();
                break;
            default:
                super.OnViewActionInvoked(param, bundle);
        }
    }

    KatanaDialogFragment makeCreditAlertDialog() {
        return KatanaDialogFragment.getInstance(R.string.creditSaleAlertTitle, R.string.creditSaleAlertMessage, KatanaAlertDialog.ButtonModes.YES_NO_CANCEL, new KatanaAlertDialog.ButtonMethods() {

            @Override
            public void yesButtonOperation() {
                startRequestedFragment(CustomersFragment::newInstance);
            }

            @Override
            public void noButtonOperation() {
                viewModel.receiveDataFromView(NO_CUSTOMER_DESIRED, null);
            }
        });
    }
}
