package com.kulun.kulunenergy.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityLoginPasswordBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.main.MainActivity;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.MD5;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.requestparams.Response;
import com.kulun.kulunenergy.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/8/6
 */
public class PasswordLoginActivity extends BaseActivity implements View.OnClickListener {
    private ActivityLoginPasswordBinding binding;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_password);
        binding.imgClose.setOnClickListener(this);
        binding.imgLogin.setOnClickListener(this);
        binding.imgRegister.setOnClickListener(this);
        binding.tvLoginCode.setOnClickListener(this);
        binding.tvForgetPassword.setOnClickListener(this);
        requestMapPermissions();
        //User.getInstance().setIsneedlogin(false);
    }

    @Override
    public void onBackPressed() {
        Intent intents = new Intent(PasswordLoginActivity.this, MainActivity.class);
        intents.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intents);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                Intent intents = new Intent(PasswordLoginActivity.this, MainActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intents);
                finish();
                break;
            case R.id.img_login:
                String phone = binding.etPhone.getText().toString();
                String code = binding.etPassword.getText().toString();
                if (!Utils.isPhone(phone, PasswordLoginActivity.this)) {
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Utils.snackbar(PasswordLoginActivity.this, "密码不能为空");
                    return;
                }
                login(phone, code);
                break;
            case R.id.img_register:
                Intent intent = new Intent(PasswordLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login_code:
                Intent intent1 = new Intent(PasswordLoginActivity.this, VeriCodeLoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_forget_password:
                String tel = binding.etPhone.getText().toString();
                Intent intent2 = new Intent(PasswordLoginActivity.this, ForgetPasswordActivity.class);
                intent2.putExtra("tel", tel);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    private void login(String phone, String code) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String ,String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", MD5.encode(code));
        new MyRequest().req(API.URL_LOGIN, map, this, null, null, new Response() {
            @Override
            public void response(JsonObject json , JsonArray jsonArray) {
                User.getInstance().setIsneedlogin(false);
                // TODO: 2019/10/12 0012  从服务器返回的数据设置是否需要身份认证
                Intent intent = new Intent(PasswordLoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        /**
         * phone[string]	是	电话
         * pwd[string]	是	密码
         */
//        final String url = API.BASE_URL + API.LOGIN_PASSWORD;
//        final RequestParams params = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("phone", phone);
//        map.put("password", MD5.encode(code));
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        params.add("phone",phone);
//        params.add("password", MD5.encode(code));
//        new MyRequest().myrequest(url, params, PasswordLoginActivity.this, getApplicationContext(), true, new Myparams() {
//            @Override
//            public void message(String json) {
//                User.getInstance().setIsneedlogin(false);
//                // TODO: 2019/10/12 0012  从服务器返回的数据设置是否需要身份认证
//                Intent intent = new Intent(PasswordLoginActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    private void requestMapPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
        }
    }
}
