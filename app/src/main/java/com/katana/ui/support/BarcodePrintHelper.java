package com.katana.ui.support;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

    private static final int DENSITY = 0;
    private Context context;
    private Map<String, String> printerInfo;
    private LWPrint printer;
    private boolean isPrinterFound = false;
    private ProgressDialog progressDialog = null;
    private ScheduledExecutorService executorService;
    private int printPhaseMessage;
    private PrinterDiscoverer printerDiscoverer;
    private int numberOfBarcodes;
    private boolean isInitialized;
    private String barcode;

    public BarcodePrintHelper(Context context, boolean bluetoothEnabledRefused) {
        this.isInitialized = !bluetoothEnabledRefused;

        if (!bluetoothEnabledRefused) {
            this.context = context;
            initializePrinter();
        }
    }

    private void initializePrinter() {

        printerDiscoverer = new PrinterDiscoverer(context, new OperationCallBack<Map<String, String>>() {

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
        printSettings.put(LWPrintParameterKey.HalfCut, false);
        printSettings.put(LWPrintParameterKey.TapeCut, LWPrintTapeCut.EachLabel);
        printSettings.put(LWPrintParameterKey.PrintSpeed, true);
        printSettings.put(LWPrintParameterKey.Density, DENSITY);

        return printSettings;
    }

    private void performPrint(String barcode) {
        AsyncTask.execute(() -> {
            HashMap<String, Object> printSettings = getPrintSettings();
            printer.setPrinterInformation(printerInfo);
            Map<String, Integer> lwStatus = printer.fetchPrinterStatus();
            printSettings.put(LWPrintParameterKey.TapeWidth, printer.getTapeWidthFromStatus(lwStatus));
            printer.doPrint(new BarcodeDataProvider(context.getAssets(), barcode, numberOfBarcodes), printSettings);
        });
    }

    public void printBarcode(String barcode, int quantity) {
        if (isInitialized) {
            numberOfBarcodes = quantity;

            if (printerDiscoverer.isBluetoothAvailable() && isPrinterFound) {
                performPrint(barcode);
            } else {
                this.barcode = barcode;
                printerDiscoverer.discoverBarcodePrinter();
            }
        }
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

        PrintCallBack() {
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
}
