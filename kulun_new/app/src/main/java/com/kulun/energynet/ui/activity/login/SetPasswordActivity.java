package com.kulun.energynet.ui.activity.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivitySetPasswordBinding;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.loginmodel.SmscodeModel;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.activity.PunchActivity;
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
 * created by xuedi on 2019/4/18
 */
public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivitySetPasswordBinding binding;
    private String phone;
    private String registertype;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_password);
        binding.imgBack.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
        phone = getIntent().getStringExtra("phone");
        registertype = getIntent().getStringExtra(API.registertype);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_login:
                setPassword();
                break;
            default:
                break;
        }
    }

    //点击确定设置密码
    private void setPassword() {
        /**
         phone
         [string]	是	电话
         code
         [string]		短息验证码 上一个页面已验证 ，这个可不不填
         pwd
         [string]	是	密码
         parentCode
         [string]		邀请司机二维码扫描结果 没有可以不填写
         */
        String passwordNew = binding.etPasswordNew.getText().toString();
        String passwordConfirm = binding.etPasswordConfirm.getText().toString();
        if (passwordNew.isEmpty() || passwordConfirm.isEmpty()) {
            Utils.toast(this, "密码不能为空");
            return;
        }
        if (!passwordNew.equals(passwordConfirm)) {
            Utils.toast(this, "两次输入密码不同，请检查");
            return;
        }
        if (passwordNew.length() < 6 || passwordConfirm.length() < 6) {
            Utils.toast(this, "密码长度不能小于6位");
            return;
        }
        final String url = API.BASE_URL + API.URL_NEW_USER_REGISTER;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("phone", phone);
        requestParams.add("pwd", MD5.encode(passwordConfirm));
        requestParams.add("type", "0");
        //0换电账户，1充电账户，默认1
        switch (registertype) {
            case "huandian":
                requestParams.add("accountType", "0");
                break;
            case "chongdian":
                requestParams.add("accountType", "1");
                break;
            default:
                break;
        }
        Utils.log(url, requestParams, null);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                SmscodeModel smscodeModel = GsonUtils.getInstance().fromJson(json, SmscodeModel.class);
                if (smscodeModel.isSuccess()) {
//                    Utils.toast(SetPasswordActivity.this, "注册成功，请登录");
                    switch (registertype) {
                        case "huandian":
                            new AlertDialog.Builder(SetPasswordActivity.this).setMessage("仅需要自营出租车业务提交审核信息，其他业务无需提交")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            Intent intents = new Intent(SetPasswordActivity.this, UploadDriverCarInfoActivity.class);
                                            intents.putExtra("phone", phone);
                                            intents.putExtra("type", 0);
                                            intents.putExtra(API.islogin, false);
                                            startActivity(intents);
                                        }
                                    }).setNegativeButton("跳过", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Utils.toast(SetPasswordActivity.this, "注册成功，请登录");
                                    Intent intent = new Intent(SetPasswordActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }).create().show();
                            break;
                        case "chongdian":
                            Intent intent = new Intent();
                            intent.putExtra("phone", phone);
                            intent.putExtra("isregister", true);
                            intent.setClass(SetPasswordActivity.this, BindCarActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                } else {
                    Utils.toast(SetPasswordActivity.this, smscodeModel.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(SetPasswordActivity.this, "连接到服务器失败");
            }
        });
    }
}
