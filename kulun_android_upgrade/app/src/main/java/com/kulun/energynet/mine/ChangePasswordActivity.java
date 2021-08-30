package com.kulun.energynet.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityChangePasswordBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.Customize;
import com.kulun.energynet.utils.MD5;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Myparams;
import com.kulun.energynet.utils.Utils;
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

  //{
  //    "password": "123456",
  //    "new_password": "000000"
  //}
    private void changePassword(String newpassword, String oldpassword) {
        HashMap<String,String> map = new HashMap<>();
        map.put("password", MD5.encode(oldpassword));
        map.put("new_password", MD5.encode(newpassword));
        new MyRequest().myRequest(API.changePassword,true, map,true,  this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                Utils.snackbar(ChangePasswordActivity.this, "密码修改成功");
                finish();
            }
        });
    }
}
