package com.katana.ui.support;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.epson.lwprint.sdk.LWPrintDiscoverConnectionType;
import com.epson.lwprint.sdk.LWPrintDiscoverPrinter;
import com.epson.lwprint.sdk.LWPrintDiscoverPrinterCallback;
import com.katana.infrastructure.support.ActionFuncWithOneParam;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.ui.R;
import com.zj.btsdk.BluetoothService;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class PrinterDiscoverer {

    private final OperationCallBack<Map<String, String>> operationCallBack;
    private final PrinterType printerType;
    private final Context context;
    private final boolean bluetoothRefused = false;
    private LWPrintDiscoverPrinter printerDiscoverHelper;
    private final LWPrintDiscoverPrinterCallback discoverBarcodePrinterCallBack = new LWPrintDiscoverPrinterCallback() {
        @Override
        public void onFindPrinter(LWPrintDiscoverPrinter arg0, Map<String, String> printerInfo) {
            if (printerDiscoverHelper != null) {
                printerDiscoverHelper.stopDiscover();
            }

            operationCallBack.onOperationSuccessful(printerInfo);
        }

        @Override
        public void onRemovePrinter(LWPrintDiscoverPrinter arg0, Map<String, String> arg1) {
            Utils.makeToast(context, R.string.printerRemoved);
        }


    };
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothService receiptPrinterService;
    private ActionFuncWithOneParam afterPrinterPairingAction = null;

    private final BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (printerType.equals(PrinterType.Receipt)) {

                        if (device != null && device.getName() != null) {
                            String deviceName = device.getName();
                            if (deviceName.contains("Printer")) {
                                Map<String, String> deviceInfo = new HashMap<String, String>();
                                deviceInfo.put(device.getName(), device.getAddress());
                                operationCallBack.onOperationSuccessful(deviceInfo);
                                receiptPrinterService.cancelDiscovery();
                            }
                        }
                        break;
                    } else if (device.getName().contains("LW")) {

                    }
            }
        }
    };

    PrinterDiscoverer(Context context, OperationCallBack<Map<String, String>> operationCallBack,
                      PrinterType printerType) {

        this.operationCallBack = operationCallBack;
        this.context = context;
        this.printerType = printerType;
        setupBluetooth();
    }

    private void setupBluetooth() {
        if (canConnectBluetooth()) {
            enableBluetooth();
            context.registerReceiver(bluetoothBroadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }

    private boolean canConnectBluetooth() {
        boolean canConnect = true;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Utils.makeToast(context, R.string.bluetoothNotSupported);
            canConnect = false;
        }
        return canConnect;
    }

    private void enableBluetooth() {
        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        ((Activity) context).startActivityForResult(enableBluetoothIntent, Utils.Constants.BLUETOOTH_REQUEST_CODE);
    }

    void discoverBarcodePrinter() {
        if (!bluetoothRefused) {
            if (isBarcodePrinterPaired()) {
                performBarcodePrinterDiscovery();
            } else {
                //Ask user to manually pair for now
            }
        }
    }

    private void performBarcodePrinterDiscovery() {
        EnumSet<LWPrintDiscoverConnectionType> flag = EnumSet
                .of(LWPrintDiscoverConnectionType.ConnectionTypeBluetooth);
        printerDiscoverHelper = new LWPrintDiscoverPrinter(null, null, flag);
        printerDiscoverHelper.setCallback(discoverBarcodePrinterCallBack);
        printerDiscoverHelper.startDiscover(context);
    }

    private boolean isBarcodePrinterPaired() {
        boolean isPaired = false;
        if (bluetoothAdapter != null) {
            Collection<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().contains("LW") && device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    isPaired = true;
                    break;
                }
            }
        }
        return isPaired;
    }

    private void tryPairingBarcodePrinterThen() {
        if (bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();

//        afterPrinterPairingAction = afterPairingAction;
        bluetoothAdapter.startDiscovery();
    }

    boolean isBluetoothAvailable() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public void discoverReceiptPrinter() {
        if (!bluetoothRefused) {
            receiptPrinterService = new BluetoothService(context, new Handler());
            receiptPrinterService.startDiscovery();
            Utils.makeToast(context, R.string.printerdiscovering);
        }
    }

    void clearResources() {
        if (printerType.equals(PrinterType.Receipt) && receiptPrinterService != null) {
            receiptPrinterService.cancelDiscovery();

            receiptPrinterService.stop();
            receiptPrinterService = null;
        }

        if (printerType.equals(PrinterType.Barcode) && printerDiscoverHelper != null) {
            printerDiscoverHelper.stopDiscover();
            printerDiscoverHelper = null;
        }

        context.unregisterReceiver(bluetoothBroadcastReceiver);
    }
}
