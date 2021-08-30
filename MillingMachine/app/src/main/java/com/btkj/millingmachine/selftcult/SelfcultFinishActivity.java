package com.btkj.millingmachine.selftcult;

import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivitySelfCultFinishBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MyApplication;
import com.btkj.millingmachine.model.BaseObject;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.FinishTimeFinishInterface;
import com.btkj.millingmachine.util.SoundPlayUtils;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/5/4
 */
public class SelfcultFinishActivity extends BaseActivity implements FinishTimeFinishInterface,SelfcultFinishInterface{
    private FinishTimeCount timeCount;
    private long weight;
    private ActivitySelfCultFinishBinding binding;
    private String serialNo;
    private Handler handler = new Handler();
    private SelfcultFinishInterface finishInterface;
    private boolean isMain;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MyApplication.getInstance().sendJiliangCmd(API.chengzhong, finishInterface);
            handler.postDelayed(runnable, 100);
        }
    };

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_self_cult_finish);
        finishInterface = this;
        weight = getIntent().getLongExtra("weight", 0);
        serialNo = getIntent().getStringExtra("serialNo");
        isMain = getIntent().getBooleanExtra("ismain", false);
        openLight();
        binding.center.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishPage();
            }
        });
        binding.center.back.setVisibility(View.GONE);
        SoundPlayUtils.play(6);
        if (!isMain) {
            uploadkucun();
            handler.postDelayed(runnable, 1000);
        }
    }


    //上传是否取米 java.lang.IllegalArgumentException: Synchronous ResponseHandler used in AsyncHttpClient. You should create your response handler in a looper thread or use SyncHttpClient instead.
    private void UploadIsQumi() {
        /**
         * consumeFetchedRice 取米weight，int，单位g；订单号serialNo，String
         */
        String url = API.BASE_URL + API.QUMIZHONGLIANG;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("weight", String.valueOf(weight));
        map.put("serialNo", serialNo);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        Utils.log(requestParams.toString());
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(json);
                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
                if (!baseObject.isSuccess()){
                    Utils.toast(SelfcultFinishActivity.this, baseObject.getError());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.log(url+API.internet_error);
            }
        });
    }
    //更新设备库存
    private void uploadkucun() {
        /**
         * deviceId[int]	是	设备id
         * weight[int]	是	本次实际碾米实际重量，克
         * serialNo[string]	是	消费订单号，用户更新用户实际获取米重量
         */
        String url = API.BASE_URL + API.NIANMIKUCUN_GENGXIN;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(SharePref.get(SelfcultFinishActivity.this, "id", 0)));
        map.put("weight", String.valueOf(weight));
        map.put("serialNo", serialNo);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(persistentCookieStore);
        Utils.log(url + "\n" + requestParams);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(json);
                BaseObject object = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (!object.isSuccess()){
//                    Utils.toast(SelfcultFinishActivity.this, object.getError());
//                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.log(url+"网络连接异常");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new FinishTimeCount(120000, 1000, binding.center.time, this);
        timeCount.start();
    }

    private void finishPage() {
        closeDoor();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLight();
            }
        }, 1000);
        Utils.toMain(SelfcultFinishActivity.this);
    }

    @Override
    public void timeFinish() {
        finishPage();
    }

    @Override
    public void visible() {
        binding.center.back.setVisibility(View.VISIBLE);
    }

    private void closeDoor() {
        MyApplication.getInstance().sendWuliCmd(API.jieshujiaoyi);
    }

    private void openLight() {
        MyApplication.getInstance().sendWuliCmd(API.micangdengkai);
    }

    private void closeLight() {
        MyApplication.getInstance().sendWuliCmd(API.micangdengguan);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeCount.cancel();
        clearHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
        clearHandler();
    }

    //初始化头部和底部
    @Override
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null){
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
//            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("服务电话："+config.getServiceCall());
        }
    }

    @Override
    public void selffinsh(long result) {
        Utils.log("取米重量"+result);
        if (result < 30){
            clearHandler();
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UploadIsQumi();
                }
            });
        }
    }

    private void clearHandler() {
        handler.removeCallbacksAndMessages(null);
    }

    public class FinishTimeCount extends CountDownTimer {
        private TextView textView;
        private FinishTimeFinishInterface finishInterface;

        public FinishTimeCount(long millisInFuture, long countDownInterval, TextView textView, FinishTimeFinishInterface finishInterface) {
            super(millisInFuture, countDownInterval);
            if (textView != null) {
                this.textView = textView;
            }
            this.finishInterface = finishInterface;
        }

        @Override
        public void onTick(long l) {
            if (textView != null) {
                if (l/1000 == 110){
                    finishInterface.visible();
                }
                textView.setText(l / 1000 + "s");
            }
        }

        @Override
        public void onFinish() {
            Utils.log("倒计时结束");
            finishInterface.timeFinish();
        }
    }
}
