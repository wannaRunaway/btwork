package com.btkj.chongdianbao.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityForgetPasswordBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.MD5;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.TimerCountUtils;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by xuedi on 2019/8/6
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ActivityForgetPasswordBinding binding;
    private TimerCountUtils timeCount;
    private long time;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        binding.imgBack.setOnClickListener(this);
        binding.tvSmsReceive.setOnClickListener(this);
        binding.tvBack.setOnClickListener(this);
        binding.imgRegister.setOnClickListener(this);
        timeCount = new TimerCountUtils(binding.tvSmsReceive,60000, 1000);
        if(getIntent()!=null&&!TextUtils.isEmpty(getIntent().getStringExtra("tel"))){
            binding.etPhone.setText(getIntent().getStringExtra("tel"));
            binding.etCode.requestFocus();
        }
    }

    @Override
    protected void onDestroy() {
        if(timeCount!=null){
            timeCount.cancelTime();
            timeCount=null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
            case R.id.tv_back:
                finish();
                break;
            case R.id.img_register:
                String myphones = binding.etPhone.getText().toString();
                if (!Utils.isPhone(myphones, getApplicationContext(), ForgetPasswordActivity.this)) {
                    return;
                }
                String code = binding.etCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, "?????????????????????");
                    return;
                }
                String passwordNew = binding.etPasswordNew.getText().toString();
                String passwordConfirm = binding.etPasswordConfirm.getText().toString();
                if (TextUtils.isEmpty(passwordNew) || TextUtils.isEmpty(passwordConfirm)) {
                    Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, "???????????????");
                    return;
                }
                if (passwordNew.length() < 6) {
                    Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, "??????????????????6???");
                    return;
                }
                if (!passwordNew.equals(passwordConfirm)) {
                    Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, "???????????????????????????");
                    return;
                }
                if (teshu(passwordNew)) {
                    Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, "????????????????????????");
                    return;
                }
                changePassword(myphones, code, passwordNew);
                break;
            case R.id.tv_sms_receive:
                if (System.currentTimeMillis() - time < 5000){
                    time = System.currentTimeMillis();
                    Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, "????????????");
                    return;
                }
                String myphone = binding.etPhone.getText().toString();
                if (!Utils.isPhone(myphone, getApplicationContext(), ForgetPasswordActivity.this)) {
                    return;
                }
                getSmsCode(myphone);
                break;
            default:
                break;
        }
    }

    private void changePassword(String myphones, String code, String passwordNew) {
        /**
         * type  [int]	???	??????????????? 1?????? 2???????????? 3??????
         * phone   [string]	???	????????????
         * code   [string]	???	?????????
         * pwd  [string]		type=2?????????
         * invitePhone  [string]		type=1???????????????
         */
        String url = API.BASE_URL + API.LOGIN_VERI_CODE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(2));
        map.put("phone", myphones);
        map.put("code", code);
        map.put("pwd", MD5.encode(passwordNew));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, ForgetPasswordActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, "??????????????????????????????");
                finish();
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
//                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (baseObject.isSuccess()) {
//                    Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, "??????????????????????????????");
//                    finish();
//                }else {
//                    Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, baseObject.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, API.net_error);
//                Utils.log(url, requestParams, "");
//            }
//        });
    }

    /**
     * type
     * [short]	???	??????????????? 1?????? 2???????????? 3??????
     * phone
     * [string]	???	??????
     *
     * @param myphone
     */
    private void getSmsCode(String myphone) {
        String url = API.BASE_URL + API.SMSCODE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(2));
        map.put("phone", myphone);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, ForgetPasswordActivity.this, getApplicationContext(), false, new Myparams() {
            @Override
            public void message(String json) {
                if(timeCount!=null){
                    timeCount.start();
                }
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (!baseObject.isSuccess()) {
//                    if (baseObject.getMsg() != null) {
//                        Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, baseObject.getMsg());
//                    }
//                } else {
//                    timeCount.start();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), ForgetPasswordActivity.this, API.net_error);
//            }
//        });
    }

    public boolean teshu(String string) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~???@#???%??????&*????????????+|{}????????????????????????????????????]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
