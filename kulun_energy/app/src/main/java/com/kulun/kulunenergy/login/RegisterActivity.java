package com.kulun.kulunenergy.login;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityRegisterBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.requestparams.Response;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.utils.TimerCountUtils;
import com.kulun.kulunenergy.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/8/6
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ActivityRegisterBinding binding;
    private TimerCountUtils timeCount;
    private long time;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.imgBack.setOnClickListener(this);
        binding.imgRegister.setOnClickListener(this);
        binding.tvSmsReceive.setOnClickListener(this);
        binding.tvXieyi.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_register:
                String phone = binding.etPhone.getText().toString();
                String code = binding.etCode.getText().toString();
                if (phone.isEmpty()){
                    Utils.snackbar(RegisterActivity.this, "手机号不能为空");
                    return;
                }
                if (phone.length() < 11){
                    Utils.snackbar( RegisterActivity.this, "手机号长度不正确");
                    return;
                }
                if (code.isEmpty()){
                    Utils.snackbar(RegisterActivity.this, "验证码不能为空");
                    return;
                }
                register(phone, code);
                break;
            case R.id.tv_sms_receive:
                if (System.currentTimeMillis() - time < 5000){
                    time = System.currentTimeMillis();
                    Utils.snackbar( RegisterActivity.this, "点击过快");
                    return;
                }
                String myphone = binding.etPhone.getText().toString();
                if (!Utils.isPhone(myphone,  RegisterActivity.this)){
                    return;
                }
                getSmsCode(myphone);
                break;
            case R.id.tv_xieyi:
                Intent intent = new Intent(RegisterActivity.this, UseProtocolActivity.class);
                startActivity(intent);
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
        map.put("type", "1");
        new MyRequest().req(API.URL_GET_SMS, map, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray) {
                if (timeCount != null) {
                    timeCount.start();
                }
            }
        });
        //requestParams.add("phone", binding.etPhone.getText().toString());
        //                requestParams.add("type", "1");
//        String url = API.BASE_URL + API.SMSCODE;
//        RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("type", String.valueOf(1));
//        map.put("phone", myphone);
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().myrequest(url, requestParams, RegisterActivity.this, getApplicationContext(), false, new Myparams() {
//            @Override
//            public void message(String json) {
//                timeCount.start();
//            }
//        });
    }

    /**
     * type  [int]	是	验证码类型 1注册 2更新密码 3登录
     * phone   [string]	是	电话号码
     * code   [string]	是	验证码
     * pwd  [string]		type=2新密码
     * invitePhone  [string]		type=1邀请人电话
     */
    private void register(final String phone, String code) {
        Map<String ,String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        new MyRequest().req(API.URL_LOGIN_VERIFICATION, map, this, null, null,
                new Response() {
                    @Override
                    public void response(JsonObject json, JsonArray jsonArray) {
                        Intent intent = new Intent(RegisterActivity.this, SetPasswordActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                    }
                });
//        String url = API.BASE_URL + API.LOGIN_VERI_CODE;
//        RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("type", String.valueOf(1));
//        map.put("phone", phone);
//        map.put("code", code);
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().myrequest(url, requestParams, RegisterActivity.this, getApplicationContext(), true, new Myparams() {
//            @Override
//            public void message(String json) {
//                Intent intent = new Intent(RegisterActivity.this, SetPasswordActivity.class);
//                intent.putExtra("phone", phone);
//                startActivity(intent);
//            }
//        });
    }
}
