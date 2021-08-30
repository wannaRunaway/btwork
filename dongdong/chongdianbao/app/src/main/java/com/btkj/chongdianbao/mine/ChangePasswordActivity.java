package com.btkj.chongdianbao.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityChangePasswordBinding;
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
 * created by xuedi on 2019/8/28
 */
public class ChangePasswordActivity extends BaseActivity {
    private ActivityChangePasswordBinding binding;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        binding.imgClose.setOnClickListener(view -> finish());
        binding.tvConfrim.setOnClickListener(view -> {
            String oldpassword = binding.etPasswordOld.getText().toString();
            String newpassword = binding.etPasswordNew.getText().toString();
            String confirmpassword = binding.etPasswordConfirm.getText().toString();
            if (oldpassword.isEmpty()) {
                Utils.snackbar(getApplicationContext(), ChangePasswordActivity.this, "请输入原密码");
                return;
            }
            if (newpassword.isEmpty()) {
                Utils.snackbar(getApplicationContext(), ChangePasswordActivity.this, "请输入新密码");
                return;
            }
            if (confirmpassword.isEmpty()) {
                Utils.snackbar(getApplicationContext(), ChangePasswordActivity.this, "请再次输入新密码");
                return;
            }
            if (newpassword.length() < 6 || confirmpassword.length() < 6) {
                Utils.snackbar(getApplicationContext(), ChangePasswordActivity.this, "密码不能少于6位");
                return;
            }
            if (!newpassword.equals(confirmpassword)) {
                Utils.snackbar(getApplicationContext(), ChangePasswordActivity.this, "两次密码输入不一致");
                return;
            }
            if (Utils.teshu(newpassword)) {
                Utils.snackbar(getApplicationContext(), ChangePasswordActivity.this, "密码含有特殊字符");
                return;
            }
            changePassword(newpassword, oldpassword);
        });
    }

    //pwd[string]	是	新密码
    //oldPwd[string]	是	原始密码
    private void changePassword(String newpassword, String oldpassword) {
        String url = API.BASE_URL + API.UPDATEPASSWORD;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pwd", MD5.encode(newpassword));
        map.put("oldPwd", MD5.encode(oldpassword));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, ChangePasswordActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                Utils.snackbar(getApplicationContext(), ChangePasswordActivity.this, "密码修改成功");
                new Handler().postDelayed(() -> {
                    finish();
                }, 500);
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.setCookieStore(cookieStore);
//        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (baseObject.isSuccess()) {
//                    Utils.snackbar(getApplicationContext(), ChangePasswordActivity.this, "密码修改成功");
//                    new Handler().postDelayed(() -> {
//                        finish();
//                    }, 500);
//                } else {
//                    Utils.snackbar(getApplicationContext(), ChangePasswordActivity.this, baseObject.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), ChangePasswordActivity.this, API.net_error);
//                Utils.log(url, requestParams, null);
//            }
//        });
    }
}
