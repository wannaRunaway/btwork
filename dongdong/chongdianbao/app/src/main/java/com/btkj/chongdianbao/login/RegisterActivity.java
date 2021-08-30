package com.btkj.chongdianbao.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityRegisterBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.TimerCountUtils;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;

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
                    Utils.snackbar(getApplicationContext(), RegisterActivity.this, "手机号不能为空");
                    return;
                }
                if (phone.length() < 11){
                    Utils.snackbar(getApplicationContext(), RegisterActivity.this, "手机号长度不正确");
                    return;
                }
                if (code.isEmpty()){
                    Utils.snackbar(getApplicationContext(), RegisterActivity.this, "验证码不能为空");
                    return;
                }
                register(phone, code, binding.etInvite.getText().toString());
                break;
            case R.id.tv_sms_receive:
                if (System.currentTimeMillis() - time < 5000){
                    time = System.currentTimeMillis();
                    Utils.snackbar(getApplicationContext(), RegisterActivity.this, "点击过快");
                    return;
                }
                String myphone = binding.etPhone.getText().toString();
                if (!Utils.isPhone(myphone, getApplicationContext(), RegisterActivity.this)){
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
        String url = API.BASE_URL + API.SMSCODE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(1));
        map.put("phone", myphone);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, RegisterActivity.this, getApplicationContext(), false, new Myparams() {
            @Override
            public void message(String json) {
                timeCount.start();
            }
        });
    }

    /**
     * type  [int]	是	验证码类型 1注册 2更新密码 3登录
     * phone   [string]	是	电话号码
     * code   [string]	是	验证码
     * pwd  [string]		type=2新密码
     * invitePhone  [string]		type=1邀请人电话
     */
    private void register(final String phone, String code, final String invitePhone) {
        String url = API.BASE_URL + API.LOGIN_VERI_CODE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(1));
        map.put("phone", phone);
        map.put("code", code);
        if (!invitePhone.isEmpty()){
            map.put("invitePhone", invitePhone);
        }
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, RegisterActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                Intent intent = new Intent(RegisterActivity.this, SetPasswordActivity.class);
                intent.putExtra("phone", phone);
                intent.putExtra("invitePhone", invitePhone);
                startActivity(intent);
            }
        });
    }

}
