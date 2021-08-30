package com.kulun.kulunenergy.login;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityVeriCodeBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.requestparams.Response;
import com.kulun.kulunenergy.utils.TimerCountUtils;
import com.kulun.kulunenergy.utils.Utils;

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
                    Utils.snackbar( VeriCodeLoginActivity.this, "手机号不能为空");
                    return;
                }
                if (phone.length() < 11){
                    Utils.snackbar( VeriCodeLoginActivity.this, "手机号长度不正确");
                    return;
                }
                if (TextUtils.isEmpty(code)){
                    Utils.snackbar( VeriCodeLoginActivity.this, "验证码不能为空");
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
                    Utils.snackbar( VeriCodeLoginActivity.this, "点击过快");
                    return;
                }
                String myphone = binding.etPhone.getText().toString();
                if (!Utils.isPhone(myphone, VeriCodeLoginActivity.this)){
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
        Map<String ,String> map = new HashMap<>();
        map.put("phone", myphone);
        map.put("type", String.valueOf(3));
        new MyRequest().req(API.URL_GET_SMS, map, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray) {
                if(timeCount!=null){
                    timeCount.start();
                }
            }
        });
//        String url = API.BASE_URL + API.SMSCODE;
//        RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("type", String.valueOf(3));
//        map.put("phone", myphone);
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().myrequest(url, requestParams, VeriCodeLoginActivity.this, getApplicationContext(), false, new Myparams() {
//            @Override
//            public void message(String json) {
//                if(timeCount!=null){
//                    timeCount.start();
//                }
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
        Map<String ,String> map = new HashMap<>();
        map.put("phone", phone);
        new MyRequest().req(API.URL_LOGIN_VERIFICATION, map, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray) {

            }
        });
//        String url = API.BASE_URL + API.LOGIN_VERI_CODE;
//        RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("type", String.valueOf(3));
//        map.put("phone", phone);
//        map.put("code", code);
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().myrequest(url, requestParams, VeriCodeLoginActivity.this, getApplicationContext(), true, new Myparams() {
//            @Override
//            public void message(String json) {
////                BaseRequest.getInfo(VeriCodeLoginActivity.this, getApplicationContext());
////                SharePref.put(VeriCodeLoginActivity.this,API.islogin,true);
//                User.getInstance().setIsneedlogin(false);
//                // TODO: 2019/10/12 0012  从服务器返回的数据设置是否需要身份认证
//                Intent intent = new Intent(VeriCodeLoginActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
//        });
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
