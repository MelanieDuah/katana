package com.katana.ui.support;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.SparseIntArray;

import com.epson.lwprint.sdk.LWPrint;
import com.epson.lwprint.sdk.LWPrintCallback;
import com.epson.lwprint.sdk.LWPrintParameterKey;
import com.epson.lwprint.sdk.LWPrintPrintingPhase;
import com.epson.lwprint.sdk.LWPrintTapeCut;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.ui.R;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public class BarcodePrintHelper {

    private static final int CUT_EACH_TAPE = 0;
    private static final int DENSITY = -2;
    private Context context;
    private Map<String, String> printerInfo;
    private LWPrint printer;
    private boolean isPrinterFound = false;
    private ProgressDialog progressDialog = null;
    private Handler handler;
    private ScheduledExecutorService executorService;
    private int printPhaseMessage;
    private MelaniePrinterDiscoverer printerDiscoverer;
    private int numberOfBarcodes;
    private boolean isInitialized;
    private String barcode;

    public BarcodePrintHelper(Context context, boolean bluetoothEnabledRefused) {
        this.isInitialized = !bluetoothEnabledRefused;

        if (!bluetoothEnabledRefused) {
            this.context = context;
            initializePrinter();
            handler = new Handler(context.getMainLooper());
        }
    }

    private void initializePrinter() {

        printerDiscoverer = new MelaniePrinterDiscoverer(context, new OperationCallBack<Map<String, String>>() {

            @Override
            public void onOperationSuccessful(Map<String, String> result) {
                printerInfo = result;
                isPrinterFound = true;
                performPrint(barcode);
            }

        }, PrinterType.Barcode);

        printerDiscoverer.discoverBarcodePrinter();
        printer = new LWPrint(context);
        printer.setCallback(new PrintCallBack());
    }

    private HashMap<String, Object> getPrintSettings() {

        HashMap<String, Object> printSettings = new HashMap<>();

        printSettings.put(LWPrintParameterKey.Copies, 1);
        printSettings.put(LWPrintParameterKey.HalfCut, true);
        printSettings.put(LWPrintParameterKey.TapeCut, LWPrintTapeCut.AfterJob);
        printSettings.put(LWPrintParameterKey.PrintSpeed, true);
        printSettings.put(LWPrintParameterKey.Density, DENSITY);
        printSettings.put(LWPrintParameterKey.TapeWidth, 6);

        return printSettings;
    }

    private void performPrint(String barcode) {
        new MelaniePrintAsyncTask().execute(printer, printerInfo, getPrintSettings(), context.getAssets(), barcode);
    }

    public void printBarcode(String barcode, int quantity, Map<String, String> printerInfo) {
        if (isInitialized) {
            numberOfBarcodes = quantity;

            if (printerInfo != null) {
                this.printerInfo = printerInfo;
            }
            if (printerDiscoverer.isBluetoothAvailable() && isPrinterFound) {
                performPrint(barcode);
            } else {
                this.barcode = barcode;
                printerDiscoverer.discoverBarcodePrinter();
            }
        }
    }

    public void PerformBarcodePrint() {
        performPrint(barcode);
    }

    public void setIsPrinterFound(boolean isPrinterFound) {
        this.isPrinterFound = isPrinterFound;
    }

    public void clearResources() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }

        if (printerDiscoverer != null) {
            printerDiscoverer.clearResources();
            printerDiscoverer = null;
        }

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private class PrintCallBack implements LWPrintCallback {

        private final SparseIntArray phaseMessage;

        public PrintCallBack() {
            phaseMessage = new SparseIntArray(3) {
                {
                    put(LWPrintPrintingPhase.Prepare, R.string.printPreparing);
                    put(LWPrintPrintingPhase.Processing, R.string.printing);
                    put(LWPrintPrintingPhase.Complete, R.string.printingComplete);
                    put(LWPrintPrintingPhase.WaitingForPrint, R.string.waitingForPrint);
                }
            };
        }

        @Override
        public void onAbortPrintOperation(LWPrint arg0, int arg1, int arg2) {
            // Do nothing for now

        }

        @Override
        public void onAbortTapeFeedOperation(LWPrint arg0, int arg1, int arg2) {
            // Do nothing for now

        }

        @Override
        public void onChangePrintOperationPhase(LWPrint print, int phase) {
            final int p = phase;

            if (phase == LWPrintPrintingPhase.Complete) {

            }
        }

        @Override
        public void onChangeTapeFeedOperationPhase(LWPrint arg0, int arg1) {
            // Do nothing for now
        }

        @Override
        public void onSuspendPrintOperation(LWPrint arg0, int arg1, int arg2) {
            // Do nothing for now
        }

    }

    private class MelaniePrintAsyncTask extends AsyncTask<Object, Void, Void> {

        private static final int PRINTER = 0;
        private static final int PRINTER_INFO = 1;
        private static final int PRINT_SETTINGS = 2;
        private static final int ASSET_MANAGER = 3;
        private static final int BARCODE = 4;

        @Override
        @SuppressWarnings("unchecked")
        protected Void doInBackground(Object... params) {
            LWPrint printer = (LWPrint) params[PRINTER];

            Map<String, String> printerInfo = (Map<String, String>) params[PRINTER_INFO];
            HashMap<String, Object> printSettings = (HashMap<String, Object>) params[PRINT_SETTINGS];
            printer.setPrinterInformation(printerInfo);

            Map<String, Integer> lwStatus = printer.fetchPrinterStatus();
            printSettings.put(LWPrintParameterKey.TapeWidth, printer.getTapeWidthFromStatus(lwStatus));

            AssetManager assetManager = (AssetManager) params[ASSET_MANAGER];
            String barcode = (String) params[BARCODE];

            printer.doPrint(new MelanieBarcodeDataProvider(assetManager, barcode, numberOfBarcodes), printSettings);
            return null;
        }
    }
}
