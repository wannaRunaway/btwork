package com.botann.charing.pad.activity.AppDownloadCode;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.botann.charging.pad.R;
import com.botann.charing.pad.activity.BaseActivity;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.utils.QRCodeUtil;

/**
 * Created by liushanguo on 2018/6/21.
 */

public class AppDownloadCodeActivity extends BaseActivity {


    private ImageView ivQRCode;
    private Button refreshBtn;

    @Override
    public void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setTitle("蓝色大道APP下载");
        loadDownloadUrl(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onrestart");
    }

    @Override
    public int viewLayout() {
        return R.layout.activity_app_download;
    }

    @Override
    public void initView() {
        ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
        refreshBtn = findButtonById(R.id.button);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDownloadUrl(true);
            }
        });
    }

    private void loadDownloadUrl(final boolean isRefresh) {
        SGHTTPManager.POST(API.URL_APP_DOWNLOAD_URL, new URLParams(), new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess) {
                    String url = fetchModel.getSting("apkPath");
                    if (url != null && !url.isEmpty()) {
                        Bitmap image = QRCodeUtil.qrCodeImageWithStr(url);
                        if (image != null) ivQRCode.setImageBitmap(image);
                        if (isRefresh) toast("刷新成功！");
                    } else {
                        toast( "二维码内容为空！");
                    }
                } else {
                    toast("二维码信息获取失败！");
                }

            }
        });
    }

}
