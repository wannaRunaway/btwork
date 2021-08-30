package com.btkj.chongdianbao.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivitySetPasswordBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.MD5;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/8/8
 */
public class SetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySetPasswordBinding binding;
    private String phone;
    private String invitePhone;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_password);
        binding.imgClose.setOnClickListener(this);
        binding.imgConfrim.setOnClickListener(this);
        phone = getIntent().getStringExtra("phone");
        invitePhone = getIntent().getStringExtra("invitePhone");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_confrim:
                String passwordNew = binding.etPasswordNew.getText().toString();
                String passwordConfirm = binding.etPasswordCertain.getText().toString();
                if (TextUtils.isEmpty(passwordNew) || TextUtils.isEmpty(passwordConfirm)) {
                    Utils.snackbar(getApplicationContext(), SetPasswordActivity.this, "请输入密码");
                    return;
                }
                if (passwordNew.length() < 6 || passwordConfirm.length() < 6){
                    Utils.snackbar(getApplicationContext(), SetPasswordActivity.this, "密码不能少于6位");
                    return;
                }
                if (!passwordNew.equals(passwordConfirm)) {
                    Utils.snackbar(getApplicationContext(), SetPasswordActivity.this, "两次密码输入不一致");
                    return;
                }
                if (Utils.teshu(passwordNew)){
                    Utils.snackbar(getApplicationContext(), SetPasswordActivity.this, "密码含有特殊字符");
                    return;
                }
                setPassword(passwordNew, passwordConfirm);
                break;
            default:
                break;
        }
    }

    /**
     * phone [string]	是	电话
     * pwd  [string]	是	初始化密码
     * invitePhone [string]		邀请人电话
     */
    private void setPassword(String passwordNew, String passwordConfirm) {
        final String url = API.BASE_URL + API.REGISTER;
        final RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("pwd", MD5.encode(passwordNew));
        map.put("invitePhone", invitePhone);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, SetPasswordActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                Intent intent = new Intent(SetPasswordActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                BaseObject object = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (object.isSuccess()){
//                    Intent intent = new Intent(SetPasswordActivity.this, AddCarActivity.class);
//                    intent.putExtra(API.islogin, true);
//                    startActivity(intent);
//                }else {
//                    Utils.snackbar(getApplicationContext(), SetPasswordActivity.this, object.getMsg());
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), SetPasswordActivity.this, API.net_error);
//                Utils.log(url, requestParams, "");
//            }
//        });
    }
}
