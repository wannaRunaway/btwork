package com.btkj.millingmachine.selftcult;

import android.databinding.DataBindingUtil;
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
public class SelfcultFinishActivity extends BaseActivity implements TimeFinishInterface,SelfcultFinishInterface{
    private TimeCount timeCount;
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
        binding.layoutBack.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishPage();
            }
        });
        SoundPlayUtils.play(6);
        if (!isMain) {
            uploadkucun();
            handler.postDelayed(runnable, 1000);
        }
    }


    //?????????????????? java.lang.IllegalArgumentException: Synchronous ResponseHandler used in AsyncHttpClient. You should create your response handler in a looper thread or use SyncHttpClient instead.
    private void UploadIsQumi() {
        /**
         * consumeFetchedRice ??????weight???int?????????g????????????serialNo???String
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
    //??????????????????
    private void uploadkucun() {
        /**
         * deviceId[int]	???	??????id
         * weight[int]	???	????????????????????????????????????
         * serialNo[string]	???	?????????????????????????????????????????????????????????
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
                Utils.log(url+"??????????????????");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(60000, 1000, binding.includeHeader.tvTime, this);
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

    //????????????????????????
    @Override
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null){
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.includeHeader.imgTopIcon);
            }
            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.layoutBottom.tvPhone.setText("???????????????"+config.getServiceCall());
        }
    }

    @Override
    public void selffinsh(long result) {
        Utils.log("????????????"+result);
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
}
