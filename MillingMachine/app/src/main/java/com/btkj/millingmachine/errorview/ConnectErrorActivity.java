package com.btkj.millingmachine.errorview;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityConnectErrorBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MyApplication;
import com.btkj.millingmachine.model.BaseObject;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * created by xuedi on 2019/5/5
 */
public class ConnectErrorActivity extends BaseActivity implements GuzhangInterface {
    private ActivityConnectErrorBinding binding;
    private GuzhangInterface guzhangInterface;
    private String code;

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_connect_error);
        guzhangInterface = this;
        code = getIntent().getStringExtra("code");
        MyApplication.getInstance().sendGuzhangWuliCmd(API.chaxunguzhang, guzhangInterface);
        uploadData(code);
        hideTimeBack();
    }

    private void hideTimeBack() {
        binding.center.daojishi.setVisibility(View.INVISIBLE);
        binding.center.time.setVisibility(View.INVISIBLE);
        binding.center.back.setVisibility(View.INVISIBLE);
    }

    private void uploadData(String code) {
        String url = API.BASE_URL + API.ERROR;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(SharePref.get(ConnectErrorActivity.this, "id", 0)));
        map.put("errorId", code);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(json);
                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (!baseObject.isSuccess()) {
//                    Utils.toast(ConnectErrorActivity.this, baseObject.getError());
//                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
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
    public void message(String message) {
        String receiver = protocalReceive(message);
        if (receiver.equals("0")){
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String url = API.BASE_URL + API.ERROR;
                RequestParams requestParams = new RequestParams();
                Map<String, String> map = new HashMap<>();
                map.put("deviceId", String.valueOf(SharePref.get(ConnectErrorActivity.this, "id", 0)));
                map.put("errorId", receiver);
                requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
                AsyncHttpClient httpClient = new AsyncHttpClient();
                PersistentCookieStore cookieStore = new PersistentCookieStore(ConnectErrorActivity.this);
                httpClient.setCookieStore(cookieStore);
                httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String json = new String(responseBody);
                        Utils.log(json);
                        BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                        if (!baseObject.isSuccess()) {
//                            Utils.toast(ConnectErrorActivity.this, baseObject.getError());
//                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });
            }
        });
    }

    /**
     * app协议文档：
     * 7E A0 01 30 EF Main data 主板返回：7E A0 01 55 8A
     * 7E A1 01 99 47 Main data 主板返回：7E A1 01 55 8B
     * 7E A2 01 AA 77 Main data 主板返回：7E A2 01 55 88 7E A7 02 00 01 DA
     * 7E A7 01 AA 72 Main data 主板返回：7E A7 02 00 01 DA  7E A7 02 01 01 DB
     * 7E A8 01 AA 7D Main data 主板返回：7E A8 06 00 00 00 00 00 00 D0
     * 7E AA 01 10 C5 Main data 主板返回：7E AA 01 55 80
     * 日志没有看到A3、A5、A6、A9
     */
    public String protocalReceive(String stringList) {
        String order = stringList.substring(6, 8);
        ArrayList<String> malfunctionList = new ArrayList<>();
        if (!order.equals("00")) {
            switch (stringList.substring(6, 8)) {
                case "01":
                case "02":
                case "03":
                case "04":
                    return "3";
            }
        }
        if (stringList.substring(8, 10).equals("01") || stringList.substring(10, 12).equals("01")) {
            return "2";
        }
        if (!stringList.substring(12, 14).equals("00")) {
            switch (stringList.substring(12, 14)) {
                case "01":
                case "02":
                case "03":
                    return "5";
            }
        }
        String ss = "";
        for (int i = 0; i < malfunctionList.size(); i++) {
            ss = ss + malfunctionList.get(i);
        }
        Utils.log(ss);
        return "0";
    }
}
