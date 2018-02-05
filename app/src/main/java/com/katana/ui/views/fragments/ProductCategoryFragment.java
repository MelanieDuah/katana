package com.katana.ui.views.fragments;

import android.os.Bundle;

import com.katana.ui.R;
import com.katana.ui.databinding.FragmentProductCategoryBinding;
import com.katana.ui.support.KatanaAlertDialog;
import com.katana.ui.viewmodels.CategoryViewModel;
import com.katana.ui.views.OnFragmentInteractionListener;

import static com.katana.ui.support.Constants.REQUEST_ADD_CATEGORY;

public class ProductCategoryFragment extends BaseFragment<FragmentProductCategoryBinding, CategoryViewModel> {

    private OnFragmentInteractionListener mListener;

    public ProductCategoryFragment() {
    }

    @Override
    protected int getLayoutReSource() {
        return R.layout.fragment_product_category;
    }

    @Override
    protected CategoryViewModel getViewModel() {
        return viewModel == null ? new CategoryViewModel() : viewModel;
    }

    public static ProductCategoryFragment newInstance() {
        return new ProductCategoryFragment();
    }

    @Override
    protected void OnViewActionInvoked(String param, Bundle bundle) {
        if (param.equals(REQUEST_ADD_CATEGORY)) {
            KatanaDialogFragment dialogFragment = KatanaDialogFragment.getInstance(R.string.category,
                    KatanaAlertDialog.ButtonModes.OK_CANCEL, new KatanaAlertDialog.ButtonMethods() {
                        @Override
                        public void okButtonOperation() {
                            viewModel.addNewCategory();
                        }
                    }, R.layout.layout_add_category, viewModel);


            dialogFragment.setSize(1200, 800);
            dialogFragment.show(getFragmentManager(), REQUEST_ADD_CATEGORY);
        }
    }
}

