package com.botann.driverclient.ui.activity.login;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityNewUserBinding;
import com.botann.driverclient.model.loginmodel.SmscodeModel;
import com.botann.driverclient.model.loginmodel.VerificationLoginModel;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * created by xuedi on 2019/4/17
 */
public class NewuserRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityNewUserBinding binding;
    private TimeCount timeCount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_user);
        timeCount = new TimeCount(60000, 1000);
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_getcode:
                if (!Utils.isFastClick()){
                    Utils.toast(NewuserRegisterActivity.this, "点击过快");
                    return;
                }
                if (binding.etPhone.getText().toString().isEmpty()){
                    Utils.toast(NewuserRegisterActivity.this, "请输入手机号");
                    return;
                }
                String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
                Pattern p = Pattern.compile(regex);
                boolean canNext = p.matches(regex, binding.etPhone.getText().toString());
                if (!canNext){
                    Utils.toast(NewuserRegisterActivity.this, "手机号格式不正确");
                    return;
                }
                final String url = API.BASE_URL + API.URL_GET_SMS;
                PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
                final RequestParams requestParams = new RequestParams();
                requestParams.add("phone", binding.etPhone.getText().toString());
                requestParams.add("type", "1");
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.setCookieStore(persistentCookieStore);
                asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String json = new String(responseBody);
                        Utils.log(url, requestParams, json);
                        SmscodeModel smscodeModel = GsonUtils.getInstance().fromJson(json, SmscodeModel.class);
                        Utils.toast(NewuserRegisterActivity.this, smscodeModel.getMsg()+"");
                        if (smscodeModel.isSuccess()){
                            timeCount.start();
                        }else {
                            Utils.toast(NewuserRegisterActivity.this, smscodeModel.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Utils.toast(NewuserRegisterActivity.this, "连接到服务器失败");
                    }
                });
                break;
            case R.id.tv_login:
                register();
                break;
            default:
                break;
        }
    }

    /**
     * 验证手机号码是否合法
     * 176, 177, 178;
     * 180, 181, 182, 183, 184, 185, 186, 187, 188, 189;
     * 145, 147;
     * 130, 131, 132, 133, 134, 135, 136, 137, 138, 139;
     * 150, 151, 152, 153, 155, 156, 157, 158, 159;
     *
     * "13"代表前两位为数字13,
     * "[0-9]"代表第二位可以为0-9中的一个,
     * "[^4]" 代表除了4
     * "\\d{8}"代表后面是可以是0～9的数字, 有8位。
     */
    public static boolean isMobileNumber(String mobiles) {
        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    //注册
    private void register() {
        if (binding.etPhone.getText().toString().isEmpty()){
            Utils.toast(NewuserRegisterActivity.this, "请输入手机号");
            return;
        }
        if (binding.etVerification.getText().toString().isEmpty()){
            Utils.toast(NewuserRegisterActivity.this, "请输入验证码");
            return;
        }
        final String url = API.BASE_URL + API.URL_LOGIN_VERIFICATION;
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        final RequestParams requestParams = new RequestParams();
        requestParams.add("phone",binding.etPhone.getText().toString());
        requestParams.add("code", binding.etVerification.getText().toString());
//        requestParams.add("type", "0");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                VerificationLoginModel smscodeModel = GsonUtils.getInstance().fromJson(json, VerificationLoginModel.class);
                if (smscodeModel.isSuccess()){
                    Intent intent = new Intent(NewuserRegisterActivity.this, SetPasswordActivity.class);
                    intent.putExtra("phone", binding.etPhone.getText().toString());
                    startActivity(intent);
                }else {
                    Utils.toast(NewuserRegisterActivity.this, smscodeModel.getMsg()+"");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(NewuserRegisterActivity.this, "连接到服务器失败");
            }
        });
    }

    private void initView() {
        binding.tvBack.setOnClickListener(this);
        binding.tvGetcode.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            binding.tvGetcode.setClickable(false);
            binding.tvGetcode.setText(millisUntilFinished/1000+"秒后可重新发送");
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
