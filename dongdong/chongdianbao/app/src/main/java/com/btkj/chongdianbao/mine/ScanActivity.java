package com.btkj.chongdianbao.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityScanBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Utils;

public class ScanActivity extends BaseActivity implements View.OnClickListener, QRCodeReaderView.OnQRCodeReadListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private ActivityScanBinding binding;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan);
        binding.header.left.setOnClickListener(this);
        binding.header.title.setText("扫一扫");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        } else {
            requestCameraPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (binding.qrdecoderview != null) {
            binding.qrdecoderview.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (binding.qrdecoderview != null) {
            binding.qrdecoderview.stopCamera();
        }
    }

    @Override
    protected void onDestroy() {
        if (binding.qrdecoderview != null) {
            binding.qrdecoderview.stopCamera();
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        initQRCodeReaderView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (binding.qrdecoderview != null) {
            binding.qrdecoderview.startCamera();
        }
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        //两种解析方式  站点和司机
        Utils.log(null, null, text);
        Intent intent = new Intent();
        intent.putExtra(API.code, text);
        setResult(2, intent);
        finish();
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                MY_PERMISSION_REQUEST_CAMERA);
    }

    private void initQRCodeReaderView() {
        binding.qrdecoderview.setAutofocusInterval(2000L);
        binding.qrdecoderview.setOnQRCodeReadListener(this);
        binding.qrdecoderview.setBackCamera();
        binding.qrdecoderview.startCamera();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            default:
                break;
        }
    }
}
