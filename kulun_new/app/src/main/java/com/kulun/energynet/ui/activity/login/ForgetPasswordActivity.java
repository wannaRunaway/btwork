package com.kulun.energynet.ui.activity.login;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityForgetPasswordBinding;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.loginmodel.ForgetPasswordModel;
import com.kulun.energynet.model.loginmodel.SmscodeModel;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.MD5;
import com.kulun.energynet.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/4/17
 */
public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityForgetPasswordBinding binding;
    private TimeCount timeCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        timeCount = new TimeCount(60000, 1000);
        binding.tvBack.setOnClickListener(this);
        binding.tvGetcode.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
    }

    //验证码60s后再次发送
    private void getCode() {
        if (!Utils.isFastClick()){
            Utils.toast(ForgetPasswordActivity.this, "点击过快");
            return;
        }
        if (binding.etPhone.getText().toString().isEmpty()) {
            Toast.makeText(ForgetPasswordActivity.this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return;
        }
        final String url = API.BASE_URL + API.URL_GET_SMS;
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        final RequestParams requestParams = new RequestParams();
        requestParams.add("phone", binding.etPhone.getText().toString());
        requestParams.add("type", "2");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                SmscodeModel smscodeModel = GsonUtils.getInstance().fromJson(json, SmscodeModel.class);
                Utils.toast(ForgetPasswordActivity.this, smscodeModel.getMsg() + "");
                if (smscodeModel.isSuccess()) {
                    timeCount.start();
                } else {
                    Utils.toast(ForgetPasswordActivity.this, smscodeModel.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(ForgetPasswordActivity.this, "连接到服务器失败");
            }
        });
    }

    //获取验证码空判断
    private int etnotNull() {
        if (binding.etPhone.getText().toString().isEmpty()) {
            Toast.makeText(ForgetPasswordActivity.this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return 0;
        }
        if (binding.etVerification.getText().toString().isEmpty()) {
            Toast.makeText(ForgetPasswordActivity.this, "请输入短信验证码", Toast.LENGTH_LONG).show();
            return 0;
        }
        String passwordNew = binding.etPasswordNew.getText().toString();
        String passwordConfirm = binding.etPasswordConfirm.getText().toString();
        if (passwordNew.length() < 6){
            Utils.toast(this, "新密码长度最少为6");
            return 0;
        }
        if (passwordConfirm.length() < 6){
            Utils.toast(this, "再次输入新密码长度最少为6");
            return 0;
        }
        if (passwordNew.isEmpty() || passwordConfirm.isEmpty()) {
            Utils.toast(this, "密码不能为空");
            return 0;
        }
        if (!passwordNew.equals(passwordConfirm)) {
            Utils.toast(this, "两次输入密码不同，请检查");
            return 0;
        }
        return 1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_getcode:
                getCode();
                break;
            case R.id.tv_login:
                if (etnotNull() == 0) {
                    return;
                }
                retrieve();
                break;
            default:
                break;
        }
    }

    //找回密码上传信息

    /**
     * phone
     * [string]	是	手机
     * pwd
     * [string]	是	新密码
     * oldPwd
     * [string]		原密码（用于使用原密码修改密码）
     * code复制
     * [string]		验证码（用于短信验证码修改密码）
     */
    private void retrieve() {
        final String url = API.BASE_URL + API.URL_LOGIN_VERIFICATION;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("phone", binding.etPhone.getText().toString());
        requestParams.add("pwd", MD5.encode(binding.etPasswordConfirm.getText().toString()));
        requestParams.add("code", binding.etVerification.getText().toString());
//        requestParams.add("type", "1");
//        Map<String ,String> map = new HashMap<>();
//        map.put("phone", binding.etPhone.getText().toString());
//        map.put("code", binding.etVerification.getText().toString());
//        map.put("pwd", RSAUtils.getRSAString(binding.etPasswordConfirm.getText().toString()));
//        requestParams.add(RequestParamsCustomize.SIGN,RequestParamsCustomize.getRequestParams(map, requestParams));
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                ForgetPasswordModel forgetPasswordModel = GsonUtils.getInstance().fromJson(json, ForgetPasswordModel.class);
                Utils.toast(ForgetPasswordActivity.this, forgetPasswordModel.getMsg() + "");
                if (forgetPasswordModel.isSuccess()) {
                    finish();
                } else {
                    Utils.toast(ForgetPasswordActivity.this, forgetPasswordModel.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(ForgetPasswordActivity.this, "连接到服务器失败");
            }
        });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            binding.tvGetcode.setClickable(false);
            binding.tvGetcode.setText(millisUntilFinished / 1000 + "秒后可重新发送");
            binding.tvGetcode.setTextColor(getResources().getColor(R.color.snow_darker));
        }

        @Override
        public void onFinish() {
            binding.tvGetcode.setText("重新获取验证码");
            binding.tvGetcode.setClickable(true);
            binding.tvGetcode.setTextColor(getResources().getColor(R.color.transparent));
        }
    }
}
