package com.kulun.kulunenergy.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityQrcodeDownloadBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.BaseDialog;
import com.kulun.kulunenergy.utils.SharePref;

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
//            binding.imageview.setImageBitmap(QRCodeUtil.createQRCode(apkpath, 300, 300));
            BaseDialog.createQRCodeWithLogo(binding.imageview, this, apkpath, 300);
        }
    }
}
