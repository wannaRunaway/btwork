package com.btkj.millingmachine.errorview;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityMarginLackBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MainActivity;
import com.btkj.millingmachine.homepage.MyApplication;
import com.btkj.millingmachine.model.BaseObject;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.selftcult.ConnectWuliHeartBeatInterface;
import com.btkj.millingmachine.selftcult.QuemiInterface;
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

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/5/5
 */
public class MarginLackActivity extends BaseActivity implements QuemiInterface, TimeFinishInterface {
    private ActivityMarginLackBinding binding;
    private Handler handler = new Handler();
    private QuemiInterface quemiInterface;
    private TimeCount timeCount;
    private boolean ismain;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MyApplication.getInstance().sendQuemiHeartBeat(API.chaxunmicang, quemiInterface);
            handler.postDelayed(runnable, 10000);
        }
    };
    private void hideTimeBack() {
        binding.center.daojishi.setVisibility(View.INVISIBLE);
        binding.center.time.setVisibility(View.INVISIBLE);
        binding.center.back.setVisibility(View.INVISIBLE);
    }
    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_margin_lack);
        ismain = getIntent().getBooleanExtra("ismain", false);
        timeCount = new TimeCount(60000, 1000, null, this);
        if (!ismain){
            MyApplication.getInstance().sendWuliCmd(API.nianmistop);
            handler.postDelayed(() -> {
                MyApplication.getInstance().sendWuliCmd(API.micangdengkai);
            }, 1000);
            timeCount.start();
        }
        MyApplication.getInstance().serialPortHandlerwuli.isquemi = true;
        quemiInterface = this;
        handler.postDelayed(runnable, 10000);
        String url = API.BASE_URL + API.ERROR;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(SharePref.get(this, API.id, 0)));
        map.put("errorId", "1");
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log("缺米返回" + json);
                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (!baseObject.isSuccess()){
//                    Utils.toast(MarginLackActivity.this, baseObject.getError());
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
        hideTimeBack();
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
    protected void onStop() {
        super.onStop();
        if (timeCount != null){
            timeCount.cancel();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        MyApplication.getInstance().serialPortHandlerwuli.isquemi = false;
        timeFinish();
    }

    @Override
    public void lack() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        finish();
        Utils.toMain(MarginLackActivity.this);
    }

    @Override
    public void timeFinish() {
        MyApplication.getInstance().sendWuliCmd(API.jieshujiaoyi);
        handler.postDelayed(() -> {
            MyApplication.getInstance().sendWuliCmd(API.micangdengguan);
        }, 1000);
    }
}
