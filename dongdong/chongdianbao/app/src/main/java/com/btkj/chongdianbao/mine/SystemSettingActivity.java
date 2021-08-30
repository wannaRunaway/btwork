package com.btkj.chongdianbao.mine;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivitySystemSettingBinding;
import com.btkj.chongdianbao.login.PasswordLoginActivity;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.model.ServiceListModel;
import com.btkj.chongdianbao.model.User;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.GsonUtils;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.SharePref;
import com.loopj.android.http.RequestParams;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.HashMap;
import java.util.Map;

public class SystemSettingActivity extends BaseActivity {
    private ActivitySystemSettingBinding binding;
    //private String apkpath;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_system_setting);
        binding.header.title.setText("设置");
        //apkpath = getIntent().getStringExtra(API.apppath);
        binding.header.left.setOnClickListener(view -> finish());
        binding.rePersonl.setOnClickListener(view -> {
            Intent intent = new Intent(SystemSettingActivity.this, PersonalActivity.class);
            startActivity(intent);
        });
        binding.reChangePassword.setOnClickListener(view -> {
            Intent intent = new Intent(SystemSettingActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        });
        binding.reConnectService.setOnClickListener(view -> {
            loadData();
        });
        binding.tvLoginOut.setOnClickListener(view -> {
            loginOut();
        });
        binding.reDownload.setOnClickListener(view -> {
            Intent intent = new Intent(SystemSettingActivity.this, QrcodeDownloadActivity.class);
            //intent.putExtra(API.apppath, apkpath);
            startActivity(intent);
        });
        /*binding.rePrivacy.setOnClickListener(v -> {
            startActivity(new Intent(SystemSettingActivity.this, PrivacyActivity.class));
        });
        binding.reXieyi.setOnClickListener(v -> {
           startActivity(new Intent(SystemSettingActivity.this, UseProtocolActivity.class));
        });*/
    }

    private EasyPopup easyPopup;
    private void loadData() {
        String url = API.BASE_URL + API.SERVICELIST;
        RequestParams requestParams = new RequestParams();
        Map<String,String> map = new HashMap<>();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, SystemSettingActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                ServiceListModel model = GsonUtils.getInstance().fromJson(json, ServiceListModel.class);
                showEasyPopup(model);
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                ServiceListModel model = GsonUtils.getInstance().fromJson(json, ServiceListModel.class);
//                showEasyPopup(model);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), SystemSettingActivity.this, API.net_error);
//                Utils.log(url, requestParams, null);
//            }
//        });
    }

    private void showEasyPopup(ServiceListModel model) {
        easyPopup = new EasyPopup(this)
                .setContentView(R.layout.layout_easy_popup)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                .createPopup();
        easyPopup.showAtAnchorView(binding.reConnectService, VerticalGravity.BELOW, HorizontalGravity.ALIGN_RIGHT, 0, 0);
        TextView tv_phone = easyPopup.getView(R.id.tv_phone);
        TextView tv_time = easyPopup.getView(R.id.tv_time);
        TextView tv_call = easyPopup.getView(R.id.tv_call);
        tv_phone.setText("客服电话\n"+model.getContent().get(0).getValue());
        tv_time.setText(model.getContent().get(1).getValue()+"\n"+model.getContent().get(2).getValue());
        tv_call.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + model.getContent().get(0).getValue());
            intent.setData(data);
            startActivity(intent);
        });
    }

    private void loginOut() {
        String url = API.BASE_URL + API.LOGINOUT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, SystemSettingActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                SharePref.put(SystemSettingActivity.this, API.bindId, 0);
                SharePref.put(SystemSettingActivity.this, API.carId, 0);
                SharePref.put(SystemSettingActivity.this, API.plateNo, "");
                SharePref.put(SystemSettingActivity.this, API.carType, 0);
                SharePref.put(SystemSettingActivity.this, API.batterycount, 0);
                User.getInstance().setYuyue(false);
                User.getInstance().setDelay(false);
                User.getInstance().setIsneedlogin(true);
                User.getInstance().setIsneedcheckIdcard(false);
//                    User.getInstance().setStation(null);
                Intent intent = new Intent(SystemSettingActivity.this, PasswordLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.setCookieStore(cookieStore);
//        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                BaseObject object = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (object.isSuccess()) {
//                    SharePref.put(SystemSettingActivity.this, "phone", "");
//                    SharePref.put(SystemSettingActivity.this, "pwd", "");
//                    SharePref.put(SystemSettingActivity.this, API.bindId, 0);
//                    SharePref.put(SystemSettingActivity.this, API.plateNo, "");
//                    SharePref.put(SystemSettingActivity.this, API.carType, 0);
//                    SharePref.put(SystemSettingActivity.this, API.batterycount, 0);
//                    User.getInstance().setYuyue(false);
//                    User.getInstance().setDelay(false);
//                    User.getInstance().setIslogin(false);
////                    User.getInstance().setStation(null);
//                    Intent intent = new Intent(SystemSettingActivity.this, PasswordLoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Utils.snackbar(getApplicationContext(),SystemSettingActivity.this, object.getMsg());
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(),SystemSettingActivity.this, API.net_error);
//                Utils.log(url, requestParams, null);
//            }
//        });
    }
}