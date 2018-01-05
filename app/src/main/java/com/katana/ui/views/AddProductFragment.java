package com.katana.ui.views;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katana.ui.R;
import com.katana.ui.databinding.FragmentAddProductBinding;
import com.katana.ui.viewmodels.AddProductViewModel;
import com.katana.ui.viewmodels.BaseViewModel;

import static com.katana.ui.support.Constants.*;

public class AddProductFragment extends BaseFragment<FragmentAddProductBinding, AddProductViewModel> {

    private OnFragmentInteractionListener mListener;

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
    protected <T> T getData(String param) {
        T requestedData = null;
        switch (param) {
            case GET_CONTEXT:
                requestedData = (T) getContext();
                break;
        }
        return requestedData;
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
