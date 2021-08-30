package com.kulun.energynet.login;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityRegisterBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.mine.PersonalActivity;
import com.kulun.energynet.model.Regsiter;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.JsonSplice;
import com.kulun.energynet.utils.MD5;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.TimerCountUtils;
import com.kulun.energynet.utils.Utils;

import java.util.HashMap;

/**
 * created by xuedi on 2019/8/6
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ActivityRegisterBinding binding;
    private TimerCountUtils timeCount;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.imgBack.setOnClickListener(this);
        binding.imgRegister.setOnClickListener(this);
        binding.tvSmsReceive.setOnClickListener(this);
        binding.tvXieyi.setOnClickListener(this);
        timeCount = new TimerCountUtils(binding.tvSmsReceive, 60000, 1000);
    }

    @Override
    protected void onDestroy() {
        if (timeCount != null) {
            timeCount.cancelTime();
            timeCount = null;
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
                String passwordNew = binding.etPassword.getText().toString();
                String passwordConfirm = binding.etPasswordConfirm.getText().toString();
                if (phone.isEmpty()) {
                    Utils.snackbar(RegisterActivity.this, "手机号不能为空");
                    return;
                }
                if (phone.length() < 11) {
                    Utils.snackbar(RegisterActivity.this, "手机号长度不正确");
                    return;
                }
                if (code.isEmpty()) {
                    Utils.snackbar(RegisterActivity.this, "验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(passwordNew) || TextUtils.isEmpty(passwordConfirm)) {
                    Utils.snackbar(this, "请输入密码");
                    return;
                }
                if (passwordNew.length() < 6 || passwordConfirm.length() < 6) {
                    Utils.snackbar(this, "密码不能少于6位");
                    return;
                }
                if (!passwordNew.equals(passwordConfirm)) {
                    Utils.snackbar(this, "两次密码输入不一致");
                    return;
                }
                if (Utils.teshu(passwordNew)) {
                    Utils.snackbar(this, "密码含有特殊字符");
                    return;
                }
                register(phone, code, passwordConfirm);
                break;
            case R.id.tv_sms_receive:
                if (Utils.isFastClick()) {
                    Utils.snackbar(RegisterActivity.this, "点击过快");
                    return;
                }
                String myphone = binding.etPhone.getText().toString();
                if (!Utils.isPhone(myphone, RegisterActivity.this)) {
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

    //{
    //    "phone": "17794590705",
    //    "sms_type": 1
    //}
    private void getSmsCode(String myphone) {
        String spliceJson = JsonSplice.leftparent + JsonSplice.yin + "phone" + JsonSplice.yinandmao + JsonSplice.yin + myphone + JsonSplice.yinanddou +
                JsonSplice.yin + "sms_type" + JsonSplice.yinandmao + 3 + JsonSplice.rightparent;
//        HashMap<String, String> map = new HashMap<>();
//        map.put("phone", myphone);
//        map.put("sms_type", "1");
        new MyRequest().spliceJson(API.getsms, true, spliceJson, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (timeCount != null) {
                    timeCount.start();
                }
            }
        });
    }

    //{
    //    "phone": "17006697457",
    //    "password": "123456",
    //    "sms_code": "123456"
    //}
    private void register(String phone, String code, String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("sms_code", code);
        map.put("password", MD5.encode(password));
        new MyRequest().myRequest(API.REGISTER, true, map, true, this, null, null,
                new Response() {
                    @Override
                    public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                        Regsiter regsiter = GsonUtils.getInstance().fromJson(json, Regsiter.class);
                        SharePref.put(RegisterActivity.this, API.phone, phone);
                        SharePref.put(RegisterActivity.this, API.password, code);
                        Intent intent = new Intent(RegisterActivity.this, PersonalActivity.class);
                        intent.putExtra(API.register, true);
                        startActivity(intent);
//                        HashMap<String,String> hashMap = new HashMap<>();
//                        hashMap.put("phone", phone);
//                        hashMap.put("password", MD5.encode(password));
//                        new MyRequest().myRequest(API.LOGIN, true,hashMap,true, RegisterActivity.this, null, null, new Response() {
//                            @Override
//                            public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
//                                UserLogin userLogin = GsonUtils.getInstance().fromJson(json, UserLogin.class);
//                                if (userLogin != null) {
//                                    SharePref.put(RegisterActivity.this, API.phone, phone);
//                                    SharePref.put(RegisterActivity.this, API.password, code);
//                                }
//                                Utils.userParse(json, RegisterActivity.this);
//                            }
//                        });
                    }
                });
    }
}
