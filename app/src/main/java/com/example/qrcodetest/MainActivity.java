package com.example.qrcodetest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private TextView tv;
    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        tv = findViewById(R.id.textView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentIntegrator = new IntentIntegrator(MainActivity.this);
                // 鎖定直立掃描，即使手機橫放亦不會變橫向
                intentIntegrator.setOrientationLocked(false);
                // 是否開啟聲音（掃完後會 B 一聲），實測 HTC & 三星 note 9，預設皆為 true，掃完會 B
                intentIntegrator.setBeepEnabled(false);
                // 開啟掃描後跳到自定義的 activity，沒有這行會跳到 zxing 預設的掃描畫面
                intentIntegrator.setCaptureActivity(CaptureActivity.class);
                // 可選擇前置或後置攝影鏡頭，0 為後置，1 為前置，預設為後置
                intentIntegrator.setCameraId(0);

                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // result.getContents -> 取得之掃描結果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                tv.setText("沒有東西");
            } else {
                tv.setText(result.getContents());
                Log.i("TAG", result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
