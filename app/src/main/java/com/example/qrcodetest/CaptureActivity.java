package com.example.qrcodetest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.Random;

public class CaptureActivity extends AppCompatActivity {
    private CaptureManager capture;   // 管理擷取畫面
    private DecoratedBarcodeView barcodeScannerView;    // 裝飾整個掃描畫面
    private Button switchLightBtn;    // 控制燈光

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        ligntControl();

        // 設定擷取管理者
        capture = new CaptureManager(this, barcodeScannerView);
        // 設定拒絕危險權限提示訊息
        capture.setShowMissingCameraPermissionDialog(true, "You need Camera Permission to scan QR Code");
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();    // 開始解析
    }

    // 判斷是否有支援燈光
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void ligntControl() {
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        switchLightBtn = findViewById(R.id.switch_flashlight);

        // 補光燈是否開啟，設定開/關狀態 btn 上的字
        barcodeScannerView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                switchLightBtn.setText("Turn Off");
            }

            @Override
            public void onTorchOff() {
                switchLightBtn.setText("Turn On");
            }
        });


        if (!hasFlash()) {   // hasFlash 判斷是否有支援燈光，有的話補光按鈕才出現
            switchLightBtn.setVisibility(View.GONE);
        }

        switchLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Turn On".equals(switchLightBtn.getText())) {
                    barcodeScannerView.setTorchOn();    // 開啟燈光
                } else {
                    barcodeScannerView.setTorchOff();   // 關閉燈光
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
