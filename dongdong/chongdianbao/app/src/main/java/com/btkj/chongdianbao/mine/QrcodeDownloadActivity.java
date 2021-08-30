package com.btkj.chongdianbao.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityQrcodeDownloadBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.QRCodeUtil;
import com.btkj.chongdianbao.utils.SharePref;

/**
 * created by xuedi on 2019/9/11
 */
public class QrcodeDownloadActivity extends BaseActivity{
    private ActivityQrcodeDownloadBinding binding;
    private String apkpath;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_qrcode_download);
        apkpath = (String) SharePref.get(this,API.apkurl,"");
        binding.header.left.setOnClickListener(view -> finish());
        binding.header.title.setText("APP下载二维码");
        if(!TextUtils.isEmpty(apkpath)){
            binding.imageview.setImageBitmap(QRCodeUtil.createQRCode(apkpath, 300, 300));
        }

    }
}
