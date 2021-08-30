package com.kulun.kulunenergy.login;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityForgetPasswordBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.requestparams.Response;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.MD5;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.utils.TimerCountUtils;
import com.kulun.kulunenergy.utils.Utils;

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
                if (System.currentTimeMillis() - time < 5000) {
                    time = System.currentTimeMillis();
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
        /**
         * type  [int]	是	验证码类型 1注册 2更新密码 3登录
         * phone   [string]	是	电话号码
         * code   [string]	是	验证码
         * pwd  [string]		type=2新密码
         * invitePhone  [string]		type=1邀请人电话
         */
        Map<String ,String> map = new HashMap<>();
        map.put("phone", myphones);
        map.put("code", code);
        map.put("pwd", MD5.encode(passwordNew));
        new MyRequest().req(API.URL_LOGIN_VERIFICATION, map,
                this, null, null, new Response() {
                    @Override
                    public void response(JsonObject json , JsonArray jsonArray) {
                        Utils.snackbar(ForgetPasswordActivity.this, "密码修改成功，请登录");
                        finish();
                    }
                });
    }

    /**
     * type
     * [short]	是	验证码类型 1注册 2更新密码 3登录
     * phone
     * [string]	是	电话
     *
     * @param myphone
     */
    private void getSmsCode(String myphone) {
        Map<String ,String> map = new HashMap<>();
        map.put("phone", myphone);
        map.put("type", String.valueOf(2));
        new MyRequest().req(API.URL_GET_SMS, map, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray) {
                if (timeCount != null) {
                    timeCount.start();
                }
            }
        });
//        String url = API.BASE_URL + API.SMSCODE;
//        RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("type", String.valueOf(2));
//        map.put("phone", myphone);
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().myrequest(url, requestParams, ForgetPasswordActivity.this, getApplicationContext(), false, new Myparams() {
//            @Override
//            public void message(String json) {
//                if(timeCount!=null){
//                    timeCount.start();
//                }
//            }
//        });
    }

    public boolean teshu(String string) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
