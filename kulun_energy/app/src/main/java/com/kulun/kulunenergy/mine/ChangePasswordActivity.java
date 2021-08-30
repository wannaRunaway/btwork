package com.kulun.kulunenergy.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;

import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityChangePasswordBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.Customize;
import com.kulun.kulunenergy.utils.MD5;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.requestparams.Myparams;
import com.kulun.kulunenergy.utils.Utils;
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
                Utils.snackbar( ChangePasswordActivity.this, "请输入原密码");
                return;
            }
            if (newpassword.isEmpty()) {
                Utils.snackbar(ChangePasswordActivity.this, "请输入新密码");
                return;
            }
            if (confirmpassword.isEmpty()) {
                Utils.snackbar( ChangePasswordActivity.this, "请再次输入新密码");
                return;
            }
            if (newpassword.length() < 6 || confirmpassword.length() < 6) {
                Utils.snackbar(ChangePasswordActivity.this, "密码不能少于6位");
                return;
            }
            if (!newpassword.equals(confirmpassword)) {
                Utils.snackbar( ChangePasswordActivity.this, "两次密码输入不一致");
                return;
            }
            if (Utils.teshu(newpassword)) {
                Utils.snackbar( ChangePasswordActivity.this, "密码含有特殊字符");
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
                Utils.snackbar(ChangePasswordActivity.this, "密码修改成功");
                new Handler().postDelayed(() -> {
                    finish();
                }, 500);
            }
        });
    }
}
