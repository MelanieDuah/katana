package com.katana.ui.views;

import android.os.Bundle;

import com.katana.ui.R;
import com.katana.ui.databinding.FragmentAddProductBinding;
import com.katana.ui.support.BarcodePrintHelper;
import com.katana.ui.viewmodels.AddProductViewModel;

import static com.katana.ui.support.Constants.BARCODE;
import static com.katana.ui.support.Constants.PRINT_REQUEST;
import static com.katana.ui.support.Constants.QUANTITY;

public class AddProductFragment extends BaseFragment<FragmentAddProductBinding, AddProductViewModel> {

    private BarcodePrintHelper barcodePrintHelper;

    public AddProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutReSource() {
        return R.layout.fragment_add_product;
    }

    @Override
    protected AddProductViewModel getViewModel() {
        return new AddProductViewModel();
    }

    @Override
    protected void OnActionInvoked(String key, Bundle bundle) {
        switch (key) {
            case PRINT_REQUEST:
                if (barcodePrintHelper == null) {
                    barcodePrintHelper = new BarcodePrintHelper(getContext(), false);
                }
                String barcode = bundle.getString(BARCODE);
                int quantity = bundle.getInt(QUANTITY);
                barcodePrintHelper.printBarcode(barcode, quantity, null);
                break;
        }
    }
}
