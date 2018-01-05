package com.katana.ui.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.katana.ui.R;
import com.katana.ui.support.BarcodePrintHelper;
import com.katana.ui.support.CameraPreview;
import com.katana.ui.support.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScanBarcodeActivity extends AppCompatActivity implements  BarcodeGraphicTracker{
    private Camera camera;
    private CameraPreview cameraPreview;
    private Handler handler;
    private List<String> scannedBarcodes;
    private MediaPlayer mediaPlayer;
    private boolean isCameraReleased;
    private HashMap<String, Integer> scannedCount;
    PreviewCallback previewCallBack = new PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {

            int result = scanAndReturnBarcodeResult(data, camera);

            if (result != 0) {
                resetCameraPreviewCallBack();
                addScannerResultsToScannedBarcodes();
            }
        }

        private int scanAndReturnBarcodeResult(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            int result = 0;

            return result;
        }

        private void playBeep() {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }

        private void addScannerResultsToScannedBarcodes() {

              //  String barcode = sym.getData();
//                if (sym.getType() == Symbol.EAN13
//                        && isValidMelanieBarcode(barcode)) {
//                    scannedBarcodes.add(barcode);
//                    keepScannedCountForBarcode(barcode);
//                    updatePreviewText(barcode);
                    playBeep();
        }

        private void keepScannedCountForBarcode(String barcode) {
            if (scannedCount.containsKey(barcode)) {
                int count = scannedCount.get(barcode);
                scannedCount.put(barcode, ++count);
            } else {
                scannedCount.put(barcode, 1);
            }
        }

        private void updatePreviewText(String barcode) {
            TextView textView = (TextView) findViewById(R.id.barcodeDigitsTextView);
            String barcodeAndCount = barcode + " x" + String.valueOf(scannedCount.get(barcode));
            textView.setText(barcodeAndCount);
        }

        private boolean isValidMelanieBarcode(String barcode) {

            boolean isValid = true;
//            String barcodePrefix = Utils.getBarcodePrefix();
//            if (barcode.substring(0, barcodePrefix.length()).equals(
//                    barcodePrefix)) {
//
//                String firstTwelveDigits = barcode.substring(0, barcode.length() - 1);
//                int checksum_digit = Utils.getCheckSumDigit(firstTwelveDigits);
//
//                int lastDigit = Integer.parseInt(barcode.substring(barcode.length() - 1));
//                isValid = lastDigit == checksum_digit;
//            }
            return isValid;
        }

    };
    private Runnable doAutoFocus = new Runnable() {
        @Override
        public void run() {
            if (camera != null) {
                camera.autoFocus(autoFocusCallBack);
            }
        }
    };
    AutoFocusCallback autoFocusCallBack = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            handler.postDelayed(doAutoFocus, 1000);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scan_barcode);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeFields();

        initializeMediaPlayer();
        initializeCamera();
        if (camera != null) {
            initializeBarcodeScanner();
            loadScannerPreview();
            configurePreviewFrame();
        }
    }

    private void initializeFields() {
        handler = new Handler();
        scannedBarcodes = new ArrayList<String>();
        isCameraReleased = false;
        scannedCount = new HashMap<>();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initializeMediaPlayer() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                mediaPlayer = MediaPlayer.create(ScanBarcodeActivity.this,
                        R.raw.barcodebeep);
                return null;
            }
        };
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            task.execute();
        } else {
            task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, (Void[]) null);
        }
    }

    private void initializeCamera() {
        camera = getCameraInstance();
        if (camera != null && camera.getParameters().getSupportedFlashModes() != null) {
            camera.getParameters().setFlashMode(Parameters.FLASH_MODE_AUTO);
        }
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.e("","");
        }
        return c;
    }

    private void initializeBarcodeScanner() {
    }

    private void loadScannerPreview() {
        cameraPreview = new CameraPreview(this, camera, previewCallBack,
                autoFocusCallBack);
        FrameLayout previewFrame = (FrameLayout) findViewById(R.id.scannerMainView);
        previewFrame.addView(cameraPreview);
    }

    private void configurePreviewFrame() {
        FrameLayout previewFrame = (FrameLayout) findViewById(R.id.scannerMainView);
        previewFrame.addView(redLineView());
        previewFrame.addView(doneButton());
        findViewById(R.id.scannedBarcodeDisplay).bringToFront();
    }

    private View redLineView() {
        View view = new View(this);
        view.setBackgroundColor(Color.RED);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 3);
        params.gravity = Gravity.CENTER_VERTICAL;
        view.setLayoutParams(params);
        view.bringToFront();
        return view;
    }

    private Button doneButton() {

        Button button = new Button(this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;
        button.setLayoutParams(params);
        button.setText(R.string.doneText);
        setButtonOnclickListner(button);

        return button;
    }

    private void setButtonOnclickListner(Button button) {
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putStringArrayListExtra(LocalConstants.BARCODES,
                        new ArrayList<String>(scannedBarcodes));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void resetCameraPreviewCallBack() {
        cameraPreview.removePreviewCallBack();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isCameraReleased) {
                    cameraPreview.resetPreviewCallBack();
                }
            }
        }, 900);
    }

    private void releaseCamera() {
        if (camera != null) {
            SurfaceHolder previewHolder = cameraPreview.getHolder();
            if (previewHolder != null) {
                previewHolder.removeCallback(cameraPreview);
            }
            camera.autoFocus(null);
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
            isCameraReleased = true;
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        releaseCamera();
        releaseMediaPlayer();
    }

    private class LocalConstants {
        public static final String BARCODES = "barcodes";
    }
}
