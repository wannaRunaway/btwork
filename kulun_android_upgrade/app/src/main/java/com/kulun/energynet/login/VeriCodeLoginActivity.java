package com.kulun.energynet.login;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityVeriCodeBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.JsonSplice;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.TimerCountUtils;
import com.kulun.energynet.utils.Utils;

import java.util.HashMap;

public class VeriCodeLoginActivity extends BaseActivity implements View.OnClickListener {
    private ActivityVeriCodeBinding binding;
    private TimerCountUtils timeCount;
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
                if (Utils.isFastClick()) {
                    Utils.snackbar(VeriCodeLoginActivity.this, "点击过快");
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
     {
     "phone": "17794590705",
     "sms_type": 1
     }
     */
    private void getSmsCode(String myphone) {
        String spliceJson = JsonSplice.leftparent+JsonSplice.yin+"phone"+JsonSplice.yinandmao+JsonSplice.yin+myphone+JsonSplice.yinanddou+
                JsonSplice.yin+"sms_type"+JsonSplice.yinandmao+1+JsonSplice.rightparent;
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

    //{
    //    "phone": "13212312312",
    //    "sms_code": "123456",
    //    "password": "21ef05aed5af92469a50b35623d52101"
    //}
    private void login(String phone, String code) {
        HashMap<String ,String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("sms_code", code);
        new MyRequest().myRequest(API.LOGIN, true,map, true,this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                UserLogin userLogin = GsonUtils.getInstance().fromJson(json, UserLogin.class);
                if (userLogin != null) {
                    SharePref.put(VeriCodeLoginActivity.this, API.phone, phone);
                }
                Utils.userParse(json, VeriCodeLoginActivity.this);
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
