package com.katana.ui.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.katana.ui.R;
import com.katana.ui.support.Utils;

import java.io.IOException;
import java.util.HashMap;

import static com.katana.ui.support.Constants.BARCODES;

public class ScanBarcodeActivity extends AppCompatActivity {
    private SurfaceView cameraPreview;
    private MediaPlayer mediaPlayer;
    private HashMap<String, Integer> scannedBarcodes;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private TextView previewTextView;
    private View redLine;
    private TranslateAnimation redLineAnimation;
    private boolean isResultAlreadySet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_barcode);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeFields();

        initializeMediaPlayer();
        initializeBarcodeScanner();
        loadScannerPreview();
        initializeDoneButton();
    }

    private void initializeFields() {
        scannedBarcodes = new HashMap<>();
        previewTextView = (TextView) findViewById(R.id.barcodeDigitsTextView);
        redLine = findViewById(R.id.redlineView);

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.EAN_13)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .setRequestedFps(15.0f)
                .build();


    }

    private void initializeDoneButton() {

        ImageButton doneButton = (ImageButton) findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                prepareBarcodesForSending();
                finish();
            }
        });
    }

    private void addBarcodeToScannedCollection(String barcode) {
        if (scannedBarcodes.containsKey(barcode)) {
            int count = scannedBarcodes.get(barcode);
            scannedBarcodes.put(barcode, ++count);
        } else {
            scannedBarcodes.put(barcode, 1);
        }
    }


    private void updatePreviewText(String barcode) {
        String barcodeAndCount = String.format("%s %s %s", barcode, "x", String.valueOf(scannedBarcodes.get(barcode)));
        previewTextView.post(new Runnable() {
            @Override
            public void run() {
                previewTextView.setText(barcodeAndCount);
            }
        });
    }

    private void playBeep() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void initializeMediaPlayer() {
        AsyncTask.execute(() -> {
            mediaPlayer = MediaPlayer.create(ScanBarcodeActivity.this,
                    R.raw.barcodebeep);
        });
    }

    private void initializeBarcodeScanner() {
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    try {
                        ScanBarcodeActivity.this.playBeep();

                        Barcode barcode = barcodes.valueAt(0);
                        if (barcode != null && isValidKatanaBarcode(barcode.displayValue)) {
                            addBarcodeToScannedCollection(barcode.displayValue);
                            updatePreviewText(barcode.displayValue);
                        }

                        Thread.sleep(2000);

                    } catch (Exception ex) {
                        previewTextView.post(() -> updatePreviewText("Error!"));
                    }
                }
            }
        });
    }

    private void loadScannerPreview() {
        cameraPreview = findViewById(R.id.camera_view);
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraPreview.getHolder());
                        animateRedLine();
                    }

                } catch (IOException ie) {
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraPreview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cameraSource != null)
                            cameraSource.stop();

                        stopRedLineAnimation();
                    }
                }, 1000);
            }
        });
    }

    private void animateRedLine() {
        if (redLineAnimation == null) {
            redLineAnimation = new TranslateAnimation(
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                    TranslateAnimation.RELATIVE_TO_PARENT, 1.0f);

            redLineAnimation.setDuration(2500);
            redLineAnimation.setRepeatCount(-1);
            redLineAnimation.setRepeatMode(Animation.REVERSE);
            redLineAnimation.setInterpolator(new LinearInterpolator());
        }

        redLine.post(new Runnable() {
            @Override
            public void run() {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) redLine.getLayoutParams();
                layoutParams.gravity = Gravity.NO_GRAVITY;
                redLine.setAnimation(redLineAnimation);
                if (!redLineAnimation.hasEnded())
                    redLineAnimation.start();
                redLine.setBackgroundColor(getResources().getColor(R.color.scannerRed));
            }
        });
    }

    private boolean isValidKatanaBarcode(String barcode) {

        boolean isValid = false;
        String barcodePrefix = Utils.getBarcodePrefix();
        if (barcode.substring(0, barcodePrefix.length()).equals(
                barcodePrefix)) {

            String firstTwelveDigits = barcode.substring(0, barcode.length() - 1);
            int checksum_digit = Utils.getCheckSumDigit(firstTwelveDigits);

            int lastDigit = Integer.parseInt(barcode.substring(barcode.length() - 1));
            isValid = lastDigit == checksum_digit;
        }
        return isValid;
    }

    private void stopRedLineAnimation() {
        redLine.post(new Runnable() {
            @Override
            public void run() {
                if (redLineAnimation != null) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) redLine.getLayoutParams();
                    layoutParams.gravity = Gravity.CENTER_VERTICAL;
                    redLine.clearAnimation();
                    redLine.setBackgroundColor(getResources().getColor(R.color.scannerGreen));
                }
            }
        });
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        prepareBarcodesForSending();
    }

    private void prepareBarcodesForSending() {
        if (!isResultAlreadySet) {
            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(BARCODES, scannedBarcodes);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            isResultAlreadySet = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (barcodeDetector != null)
            barcodeDetector.release();

        if (cameraSource != null)
            cameraSource.release();

        releaseMediaPlayer();
    }
}
