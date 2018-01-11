package com.katana.ui.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.katana.ui.R;
import com.katana.ui.databinding.FragmentSaleBinding;
import com.katana.ui.viewmodels.SaleViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static com.katana.ui.support.Constants.BARCODES;
import static com.katana.ui.support.Constants.SCAN_REQUEST_CODE;

public class SaleFragment extends BaseFragment<FragmentSaleBinding, SaleViewModel> {

    private ScheduledExecutorService executorService;

    public SaleFragment() {
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
        return new SaleViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //launchBarcodeScanner();
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);

        Button scanButton = (Button)fragmentView.findViewById(R.id.scanBarcodeButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBarcodeScanner();
            }
        });

        return fragmentView;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SCAN_REQUEST_CODE:
                    viewModel.receiveDataFromView(BARCODES, data.getExtras().getSerializable(BARCODES));
            }
        }
    }
}
