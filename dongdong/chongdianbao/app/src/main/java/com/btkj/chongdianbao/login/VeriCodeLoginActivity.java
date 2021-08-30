package com.btkj.chongdianbao.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityVeriCodeBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.main.MainActivity;
import com.btkj.chongdianbao.model.User;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.TimerCountUtils;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

public class VeriCodeLoginActivity extends BaseActivity implements View.OnClickListener {
    private ActivityVeriCodeBinding binding;
    private TimerCountUtils timeCount;
    private long time;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_login:
                String phone = binding.etPhone.getText().toString();
                String code = binding.etCode.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    Utils.snackbar(getApplicationContext(), VeriCodeLoginActivity.this, "手机号不能为空");
                    return;
                }
                if (phone.length() < 11){
                    Utils.snackbar(getApplicationContext(), VeriCodeLoginActivity.this, "手机号长度不正确");
                    return;
                }
                if (TextUtils.isEmpty(code)){
                    Utils.snackbar(getApplicationContext(), VeriCodeLoginActivity.this, "验证码不能为空");
                    return;
                }
                login(phone, code);
                break;
            case R.id.img_register:
                Intent intent = new Intent(VeriCodeLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sms_receive:
                if (System.currentTimeMillis() - time < 5000){
                    time = System.currentTimeMillis();
                    Utils.snackbar(getApplicationContext(), VeriCodeLoginActivity.this, "点击过快");
                    return;
                }
                String myphone = binding.etPhone.getText().toString();
                if (!Utils.isPhone(myphone, getApplicationContext(), VeriCodeLoginActivity.this)){
                    return;
                }
                getSmsCode(myphone);
                break;
            case R.id.tv_login_code:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * type
     * [short]	是	验证码类型 1注册 2更新密码 3登录
     * phone
     * [string]	是	电话
     * @param myphone
     */
    private void getSmsCode(String myphone) {
        String url = API.BASE_URL + API.SMSCODE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(3));
        map.put("phone", myphone);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, VeriCodeLoginActivity.this, getApplicationContext(), false, new Myparams() {
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
//                if (!baseObject.isSuccess()){
//                    if (baseObject.getMsg() != null){
//                        Utils.snackbar(getApplicationContext(), VeriCodeLoginActivity.this, baseObject.getMsg());
//                    }
//                }else {
//                    timeCount.start();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), VeriCodeLoginActivity.this, API.net_error);
//            }
//        });
    }

    /**
     type    [int]	是	验证码类型 1注册 2更新密码 3登录
     phone   [string]	是	电话号码
     code    [string]	是	验证码
     pwd  [string]		type=2新密码
     invitePhone  [string]		type=1邀请人电话
     */
    private void login(String phone, String code) {
        String url = API.BASE_URL + API.LOGIN_VERI_CODE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(3));
        map.put("phone", phone);
        map.put("code", code);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, VeriCodeLoginActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
//                BaseRequest.getInfo(VeriCodeLoginActivity.this, getApplicationContext());
//                SharePref.put(VeriCodeLoginActivity.this,API.islogin,true);
                User.getInstance().setIsneedlogin(false);
                // TODO: 2019/10/12 0012  从服务器返回的数据设置是否需要身份认证
                Intent intent = new Intent(VeriCodeLoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_veri_code);
        binding.imgClose.setOnClickListener(this);
        binding.tvSmsReceive.setOnClickListener(this);
        binding.imgLogin.setOnClickListener(this);
        binding.imgRegister.setOnClickListener(this);
        binding.tvLoginCode.setOnClickListener(this);
        binding.etPhone.requestFocus();
        timeCount = new TimerCountUtils(binding.tvSmsReceive,60000, 1000);
    }

    @Override
    protected void onDestroy() {
        if(timeCount!=null){
            timeCount.cancelTime();
            timeCount=null;
        }
        super.onDestroy();
    }
}
