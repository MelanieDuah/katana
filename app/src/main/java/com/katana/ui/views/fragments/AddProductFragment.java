package com.katana.ui.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import com.katana.ui.R;
import com.katana.ui.databinding.FragmentAddProductBinding;
import com.katana.ui.support.BarcodePrintHelper;
import com.katana.ui.viewmodels.AddProductViewModel;
import com.katana.ui.views.activities.ScanOCRActivity;

import static android.app.Activity.RESULT_OK;
import static com.katana.ui.support.Constants.BARCODE;
import static com.katana.ui.support.Constants.OCR_RESULT;
import static com.katana.ui.support.Constants.PRINT_REQUEST;
import static com.katana.ui.support.Constants.QUANTITY;
import static com.katana.ui.support.Constants.REQUEST_OCR;
import static com.katana.ui.support.Constants.SCAN_OCR_REQUEST_CODE;

public class AddProductFragment extends BaseFragment<FragmentAddProductBinding, AddProductViewModel> {

    private BarcodePrintHelper barcodePrintHelper;

    public AddProductFragment() {
    }

    public static AddProductFragment newInstance() {
        return new AddProductFragment();
    }

    @Override
    protected int getLayoutReSource() {
        return R.layout.fragment_add_product;
    }

    @Override
    protected AddProductViewModel getViewModel() {
        return viewModel == null ? new AddProductViewModel() : viewModel;
    }

    @Override
    protected void OnViewActionInvoked(String key, Bundle bundle) {
        switch (key) {
            case PRINT_REQUEST:
                if (barcodePrintHelper == null) {
                    barcodePrintHelper = new BarcodePrintHelper(getContext(), false);
                }
                String barcode = bundle.getString(BARCODE);
                int quantity = bundle.getInt(QUANTITY);
                barcodePrintHelper.printBarcode(barcode, quantity);
                break;
            case REQUEST_OCR:
                launchOCRScanner();
                break;
            default:
                super.OnViewActionInvoked(key, bundle);

        }
    }

    private void launchOCRScanner() {
        Intent intent = new Intent(getContext(), ScanOCRActivity.class);
        startActivityForResult(intent, SCAN_OCR_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SCAN_OCR_REQUEST_CODE:
                    viewModel.setProductName(data.getExtras().getString(OCR_RESULT));
                    break;
            }
        }
    }
}
