package com.kulun.kulunenergy.login;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivitySetPasswordBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.main.MainActivity;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.requestparams.Response;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.MD5;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.utils.Utils;

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
                    Utils.snackbar(SetPasswordActivity.this, "请输入密码");
                    return;
                }
                if (passwordNew.length() < 6 || passwordConfirm.length() < 6) {
                    Utils.snackbar(SetPasswordActivity.this, "密码不能少于6位");
                    return;
                }
                if (!passwordNew.equals(passwordConfirm)) {
                    Utils.snackbar(SetPasswordActivity.this, "两次密码输入不一致");
                    return;
                }
                if (Utils.teshu(passwordNew)) {
                    Utils.snackbar(SetPasswordActivity.this, "密码含有特殊字符");
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
        Map<String ,String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("pwd", MD5.encode(passwordConfirm));
        map.put("type", "0");
        map.put("accountType", "0");
        new MyRequest().req(API.URL_NEW_USER_REGISTER, map, this, null, null, new Response() {
                    @Override
                    public void response(JsonObject json, JsonArray jsonArray) {
                        Map<String ,String> map = new HashMap<>();
                        map.put("phone", phone);
                        map.put("password", MD5.encode(passwordConfirm));
                        new MyRequest().req(API.URL_LOGIN, map, SetPasswordActivity.this, null, null, new Response() {
                            @Override
                            public void response(JsonObject json, JsonArray jsonArray) {
                                User.getInstance().setIsneedlogin(false);
                                // TODO: 2019/10/12 0012  从服务器返回的数据设置是否需要身份认证
                                Intent intent = new Intent(SetPasswordActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
/**
 * final String url = API.BASE_URL + API.URL_NEW_USER_REGISTER;
 *         final RequestParams requestParams = new RequestParams();
 *         requestParams.add("phone", phone);
 *         requestParams.add("pwd", MD5.encode(passwordConfirm));
 *         requestParams.add("type", "0");
 *         //0换电账户，1充电账户，默认1
 *         switch (registertype) {
 *             case "huandian":
 *                 requestParams.add("accountType", "0");
 *                 break;
 *             case "chongdian":
 *                 requestParams.add("accountType", "1");
 *                 break;
 *             default:
 *                 break;
 *         }
 */
//        final String url = API.BASE_URL + API.REGISTER;
//        final RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("phone", phone);
//        map.put("pwd", MD5.encode(passwordNew));
//        map.put("invitePhone", invitePhone);
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().myrequest(url, requestParams, SetPasswordActivity.this, getApplicationContext(), true, new Myparams() {
//            @Override
//            public void message(String json) {
//                Intent intent = new Intent(SetPasswordActivity.this, InfoActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
