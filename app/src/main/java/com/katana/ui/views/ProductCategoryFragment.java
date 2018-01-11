package com.katana.ui.views;

import android.content.Context;
import android.os.Bundle;

import com.katana.ui.R;
import com.katana.ui.databinding.FragmentProductCategoryBinding;
import com.katana.ui.viewmodels.CategoryViewModel;

public class ProductCategoryFragment extends BaseFragment<FragmentProductCategoryBinding, CategoryViewModel> {

    private OnFragmentInteractionListener mListener;

    public ProductCategoryFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutReSource() {
        return R.layout.fragment_product_category;
    }

    @Override
    protected CategoryViewModel getViewModel() {
        return new CategoryViewModel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

