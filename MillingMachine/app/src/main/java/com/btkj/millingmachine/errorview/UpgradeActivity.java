package com.btkj.millingmachine.errorview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityUpgradeBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MainActivity;
import com.btkj.millingmachine.homepage.MyApplication;
import com.btkj.millingmachine.model.appupdate.UpdateData;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.serialutil.FileInfo;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/5/5
 */
public class UpgradeActivity extends BaseActivity implements TimeFinishInterface {
    private ActivityUpgradeBinding binding;
    private UpdateData updateData;
    private boolean isDevice;
    private TimeCount timeCount;

    //    private String url = "https://rice-app.oss-cn-hangzhou.aliyuncs.com/app-rice-2.0-2.apk?Expires=1564105467&OSSAccessKeyId=TMP.hVAteGBjVhWHYbcrd3T379D67DxTPS6fckz8eimCkYQ1LUVxu8KcvT1gGJ3YVwhLgNXmghBKsiFfn5DZps6EaVg6wSwC6Noh2xjm35E5hFRz5jayZEVNYdVdQsn8Dv.tmp&Signature=SRG6mMmR1VeFjl49kwacEyJstzE%3D";
//    private String url1 = "https://rice-app.oss-cn-hangzhou.aliyuncs.com/app-rice-2.0-2.apk?Expires=1564105951&OSSAccessKeyId=TMP.hVAteGBjVhWHYbcrd3T379D67DxTPS6fckz8eimCkYQ1LUVxu8KcvT1gGJ3YVwhLgNXmghBKsiFfn5DZps6EaVg6wSwC6Noh2xjm35E5hFRz5jayZEVNYdVdQsn8Dv.tmp&Signature=IZ9ulnvBrXEVB21Ilj1we3GGn%2F0%3D";
    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upgrade);
        updateData = (UpdateData) getIntent().getSerializableExtra("updatedata");
        isDevice = getIntent().getBooleanExtra("isdevice", false);
        //"https://hs-rice.oss-cn-hangzhou.aliyuncs.com/RiceMill.bin.df"
        UpdateManager manager = new UpdateManager(this, Integer.parseInt(updateData.getVersionCode()), updateData.getVersionName(),
                updateData.getApkPath(), isDevice);
//        updateData.getApkPath());
        manager.checkUpdate();
        //初始化一个定时倒计时任务
        if (isDevice) {
            initCountTimer();
        }
    }

    private void initCountTimer() {
        timeCount = new TimeCount(120000, 1000, binding.tv, this);
        timeCount.start();
    }

    //初始化头部和底部
    @Override
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null) {
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
//            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("服务电话：" + config.getServiceCall());
        }
    }

    @Override
    public void timeFinish() {
        uploadData(UpgradeActivity.this, 2);
        MyApplication.getInstance().serialPortHandlerwuli.isupdate = false;
    }

    private void uploadData(Context context, int updateRst) {
        /**
         *  deviceId int   设备id
         firmwareUpdate Short 状态，升级成功0，升级失败>1
         *  firmwareHversion String 非必填 硬件版本；firmwareSversion String 非必填 软件版本
         */
        String url = API.BASE_URL + API.deviceUpload;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(SharePref.get(context, API.id, 0)));
        map.put("firmwareUpdate", String.valueOf(updateRst));
        map.put("firmwareHversion", new String(FileInfo.getInstance().getHwVersion()));
        map.put("firmwareSversion", new String(FileInfo.getInstance().getSwVersion()));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(context);
        asyncHttpClient.setCookieStore(persistentCookieStore);
        Utils.log("上传:" + requestParams);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(json);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.log("网络异常");
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timeCount != null) {
            timeCount.cancel();
        }
        MyApplication.getInstance().serialPortHandlerwuli.isupdate = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
    }
}
