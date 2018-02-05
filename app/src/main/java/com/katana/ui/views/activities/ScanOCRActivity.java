package com.katana.ui.views.activities;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.katana.ui.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.katana.ui.support.Constants.OCR_RESULT;

public class ScanOCRActivity extends AppCompatActivity {
    private TextRecognizer textRecognizer;
    private CameraSource cameraSource;
    private View redLine;
    private SurfaceView cameraPreview;
    private TranslateAnimation redLineAnimation;
    private List<String> detectedStrings;
    private ArrayAdapter<String> detectedListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_ocr);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeFields();
        initializeOCRScanner();
        loadScannerPreview();
    }

    private void initializeFields() {
        redLine = findViewById(R.id.redlineView);
        detectedStrings = new ArrayList<>();
        detectedListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, detectedStrings);
        ListView listView = findViewById(R.id.detectedTextList);
        listView.setAdapter(detectedListAdapter);
        listView.setOnItemClickListener((a, v, pos, l) -> sendSelectedStringToRequester(detectedListAdapter.getItem(pos)));

        ImageButton clearButton = findViewById(R.id.button_clear);
        clearButton.setOnClickListener((x) -> {
            detectedStrings.clear();
            detectedListAdapter.notifyDataSetChanged();
        });

        textRecognizer = new TextRecognizer.Builder(this).build();

        cameraSource = new CameraSource
                .Builder(this, textRecognizer)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .setRequestedFps(15.0f)
                .build();
    }

    private void sendSelectedStringToRequester(String selectedString) {
        setResult(RESULT_OK, getIntent().putExtra(OCR_RESULT, selectedString));
        finish();
    }

    private void initializeOCRScanner() {
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                SparseArray<TextBlock> items = detections.getDetectedItems();
                for (int i = 0; i < items.size(); ++i) {
                    TextBlock item = items.valueAt(i);
                    if (item != null && item.getValue() != null) {
                        String detectedString = item.getValue();
                        String capFirstLetter = detectedString.substring(0, 1).toUpperCase();
                        String stringCapitalized = capFirstLetter + detectedString.substring(1);

                        if (!detectedStrings.contains(stringCapitalized))
                            detectedStrings.add(stringCapitalized);
                    }
                }
                runOnUiThread(() -> detectedListAdapter.notifyDataSetChanged());

            }
        });
    }

    private void loadScannerPreview() {
        cameraPreview = findViewById(R.id.camera_view);
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanOCRActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraPreview.getHolder());
                        animateRedLine();
                    }

                } catch (IOException ie) {
                    Log.e("OCRCameraPreview", "Error in surfaceCreated", ie);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraPreview.post(new Runnable() {
                    @Override
                    public void run() {
                        cameraSource.stop();
                        stopRedLineAnimation();
                    }
                });
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
                redLine.setAnimation(redLineAnimation);
                if (!redLineAnimation.hasEnded())
                    redLineAnimation.start();
                redLine.setBackgroundColor(getResources().getColor(R.color.scannerRed));
            }
        });
    }

    private void stopRedLineAnimation() {
        redLine.post(new Runnable() {
            @Override
            public void run() {
                if (redLineAnimation != null) {
                    redLine.clearAnimation();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (textRecognizer != null)
            textRecognizer.release();
    }
}
