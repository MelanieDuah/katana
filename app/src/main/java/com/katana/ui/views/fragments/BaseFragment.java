package com.katana.ui.views.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katana.infrastructure.support.Asserter;
import com.katana.infrastructure.support.GetterFunc;
import com.katana.ui.BR;
import com.katana.ui.R;
import com.katana.ui.viewmodels.BaseViewModel;
import com.katana.ui.views.OnFragmentInteractionListener;

import static com.katana.ui.support.Constants.FRAGMENT_REQUEST_CODE;
import static com.katana.ui.support.Constants.HIDE_MAIN_PROGRESS;
import static com.katana.ui.support.Constants.SHOW_MAIN_PROGRESS;


/**
 * Created by AOwusu on 12/25/2017
 */

public abstract class BaseFragment<B extends ViewDataBinding, V extends BaseViewModel> extends Fragment {

    protected B binding;
    protected V viewModel;
    protected OnFragmentInteractionListener fragmentInteractionListener;

    protected abstract int getLayoutReSource();

    protected abstract V getViewModel();

    BaseFragment() {
        super();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = getViewModel();
        binding = DataBindingUtil.inflate(inflater, getLayoutReSource(), container, false);
        binding.setVariable(BR.viewmodel, viewModel);

        viewModel.setViewActionRequest(this::OnViewActionInvoked);

        viewModel.initialize(savedInstanceState != null);
        return binding.getRoot();
    }

    protected void OnViewActionInvoked(String param, Bundle bundle) {
        switch (param) {
            case SHOW_MAIN_PROGRESS:
                fragmentInteractionListener.onFragmentInteraction(SHOW_MAIN_PROGRESS, null);
                break;
            case HIDE_MAIN_PROGRESS:
                fragmentInteractionListener.onFragmentInteraction(HIDE_MAIN_PROGRESS, null);
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            fragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }

    protected void startRequestedFragment(GetterFunc<Fragment> fragmentToStart) {
        this.startRequestedFragment(fragmentToStart, null);
    }

    protected void startRequestedFragment(GetterFunc<Fragment> fragmentToStart, Bundle bundle) {
        Asserter.doAssert(fragmentToStart != null, "fragment to start is null in BaseFragment -> startRequestedFragment");
        Fragment fragment = fragmentToStart.get();
        if (bundle != null)
            fragment.setArguments(bundle);

        fragment.setTargetFragment(this, FRAGMENT_REQUEST_CODE);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
