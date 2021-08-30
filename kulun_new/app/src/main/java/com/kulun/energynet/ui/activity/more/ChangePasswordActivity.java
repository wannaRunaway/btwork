package com.kulun.energynet.ui.activity.more;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityChangePasswordBinding;
import com.kulun.energynet.model.User;
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

/**
 * created by xuedi on 2019/4/18
 */
public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityChangePasswordBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        binding.tvBack.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_login:
                changePassword();
                break;
            default:
                break;
        }
    }

    private void changePassword() {
        /**
         * phone
         * [string]	是	手机
         * pwd
         * [string]	是	新密码
         * oldPwd复制
         * [string]		原密码（用于使用原密码修改密码）
         * code
         * [string]		验证码（用于短信验证码修改密码）
         */
        String oldPassword = binding.etPasswordOld.getText().toString();
        String newPassword = binding.etPasswordNew.getText().toString();
        String confirmPassword = binding.etPasswordConfirm.getText().toString();
        if (oldPassword.isEmpty()||newPassword.isEmpty()||confirmPassword.isEmpty()){
            Utils.toast(this, "密码不能为空");
            return;
        }
        if (!newPassword.equals(confirmPassword)){
            Utils.toast(this, "新密码和再次输入密码不同，请检查");
            return;
        }
        if (oldPassword.length() < 6 || newPassword.length() < 6 || confirmPassword.length() <6 ){
            Utils.toast(this, "密码长度不能小于6位");
            return;
        }
        final String url = API.BASE_URL + API.URL_NEW_USER_REGISTER;
        final RequestParams requestParams = new RequestParams();
        SharedPreferences preferences = getSharedPreferences("data",this.MODE_PRIVATE);
        String phone = preferences.getString("phone", "");
        requestParams.add("phone", phone);
        requestParams.add("oldPwd", MD5.encode(oldPassword));
        requestParams.add("pwd", MD5.encode(confirmPassword));
        requestParams.add("type", "2");
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                SmscodeModel smscodeModel = GsonUtils.getInstance().fromJson(json, SmscodeModel.class);
                Utils.toast(ChangePasswordActivity.this, smscodeModel.getMsg()+"");
                if (smscodeModel.isSuccess()){
                    finish();
                }else {
                    Utils.toast(ChangePasswordActivity.this, smscodeModel.getMsg());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(ChangePasswordActivity.this, "无法连接服务器");
            }
        });
    }
}
