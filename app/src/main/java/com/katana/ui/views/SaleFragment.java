package com.katana.ui.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katana.ui.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.katana.ui.support.Constants.*;

public class SaleFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ScheduledExecutorService executorService;

    public SaleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        launchBarcodeScanner();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sale, container, false);
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

    public void launchBarcodeScanner() {
        executorService = Executors.newScheduledThreadPool(2);
        startBarcodeScanning();
    }

    private void startBarcodeScanning() {
        if (!executorService.isShutdown()) {
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getContext(), ScanBarcodeActivity.class);
                    startActivityForResult(intent, SCAN_REQUEST_CODE);
                }
            }, 100, TimeUnit.MILLISECONDS);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
