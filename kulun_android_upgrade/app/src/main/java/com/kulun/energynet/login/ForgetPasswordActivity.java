package com.kulun.energynet.login;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityForgetPasswordBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.JsonSplice;
import com.kulun.energynet.utils.MD5;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.utils.TimerCountUtils;
import com.kulun.energynet.utils.Utils;

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

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        binding.imgBack.setOnClickListener(this);
        binding.tvSmsReceive.setOnClickListener(this);
        binding.tvBack.setOnClickListener(this);
        binding.imgRegister.setOnClickListener(this);
        timeCount = new TimerCountUtils(binding.tvSmsReceive, 60000, 1000);
        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra("tel"))) {
            binding.etPhone.setText(getIntent().getStringExtra("tel"));
            binding.etCode.requestFocus();
        }
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
            case R.id.tv_back:
                finish();
                break;
            case R.id.img_register:
                String myphones = binding.etPhone.getText().toString();
                if (TextUtils.isEmpty(myphones)) {
                    Utils.snackbar(ForgetPasswordActivity.this, "手机号码不能为空");
                    return;
                }
                if (!Utils.isPhone(myphones, ForgetPasswordActivity.this)) {
                    return;
                }
                String code = binding.etCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Utils.snackbar(ForgetPasswordActivity.this, "验证码不能为空");
                    return;
                }
                String passwordNew = binding.etPasswordNew.getText().toString();
                String passwordConfirm = binding.etPasswordConfirm.getText().toString();
                if (TextUtils.isEmpty(passwordNew) || TextUtils.isEmpty(passwordConfirm)) {
                    Utils.snackbar(ForgetPasswordActivity.this, "请输入密码");
                    return;
                }
                if (passwordNew.length() < 6) {
                    Utils.snackbar(ForgetPasswordActivity.this, "密码不能少于6位");
                    return;
                }
                if (!passwordNew.equals(passwordConfirm)) {
                    Utils.snackbar(ForgetPasswordActivity.this, "两次密码输入不一致");
                    return;
                }
                if (teshu(passwordNew)) {
                    Utils.snackbar(ForgetPasswordActivity.this, "密码含有特殊字符");
                    return;
                }
                changePassword(myphones, code, passwordNew);
                break;
            case R.id.tv_sms_receive:
                if (Utils.isFastClick()) {
                    Utils.snackbar(ForgetPasswordActivity.this, "点击过快");
                    return;
                }
                String myphone = binding.etPhone.getText().toString();
                if (!Utils.isPhone(myphone, ForgetPasswordActivity.this)) {
                    return;
                }
                getSmsCode(myphone);
                break;
            default:
                break;
        }
    }

    private void changePassword(String myphones, String code, String passwordNew) {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", myphones);
        map.put("sms_code", code);
        map.put("password", MD5.encode(passwordNew));
        new MyRequest().myRequest(API.passwordReset, true,map,true,  this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                Utils.snackbar(ForgetPasswordActivity.this, "密码修改成功，请登录");
                finish();
            }
        });

    }

    //{
    //    "phone": "17794590705",
    //    "sms_type": 1
    //}
    private void getSmsCode(String myphone) {
        String spliceJson = JsonSplice.leftparent+JsonSplice.yin+"phone"+JsonSplice.yinandmao+JsonSplice.yin+myphone+JsonSplice.yinanddou+
                JsonSplice.yin+"sms_type"+JsonSplice.yinandmao+2+JsonSplice.rightparent;
//        HashMap<String, String> map = new HashMap<>();
//        map.put("phone", myphone);
//        map.put("sms_type", "1");
        new MyRequest().spliceJson(API.getsms, true,spliceJson,  this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                if (timeCount != null) {
                    timeCount.start();
                }
            }
        });
    }

    public boolean teshu(String string) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
