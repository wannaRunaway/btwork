package com.btkj.chongdianbao.mine;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityZarBarBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.utils.API;

import cn.bingoogolapple.qrcode.core.QRCodeView;

public class ZarBarActivity extends BaseActivity implements QRCodeView.Delegate{
    private ActivityZarBarBinding binding;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding= DataBindingUtil.setContentView(this, R.layout.activity_zar_bar);
        binding.zbarview.setDelegate(this);
        binding.header.title.setText("扫一扫");
    }
    @Override
    protected void onStart() {
        super.onStart();
        binding.zbarview.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        binding.zbarview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }
    @Override
    protected void onStop() {
        binding.zbarview.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        binding.zbarview.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Intent intent = new Intent();
        intent.putExtra(API.code, result);
        setResult(2, intent);
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        String tipText = binding.zbarview.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                binding.zbarview.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                binding.zbarview.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
    }
}
