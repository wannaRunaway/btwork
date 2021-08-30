package com.kulun.energynet.mine;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityPersonalBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.Utils;

import java.util.HashMap;

/**
 * created by xuedi on 2019/8/28
 */
public class PersonalActivity extends BaseActivity {
    private ActivityPersonalBinding binding;
    private boolean isRegister;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal);
        binding.title.left.setOnClickListener(view -> {
            if (isRegister){
                Utils.userParse(null, this);
            }else {
              finish();
            }
        });
        binding.tvConfrim.setOnClickListener(view -> {
            String name = binding.etName.getText().toString();
            String id = binding.etId.getText().toString();
            if (TextUtils.isEmpty(name)) {
                Utils.snackbar(PersonalActivity.this, "请填写姓名");
                return;
            }
            if (TextUtils.isEmpty(id)) {
                Utils.snackbar(PersonalActivity.this, "身份证号码为空");
                return;
            }
            if (!Utils.IDCardValidate(id)) {
                Utils.snackbar(PersonalActivity.this, "身份证号格式不正确");
                return;
            }
            postData(name, id);
        });
        isRegister = getIntent().getBooleanExtra(API.register, false);
        loadData();
    }

    private void loadData() {
        new MyRequest().myRequest(API.INFO, false, null, true, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (json != null || jsonArray != null) {
                    UserLogin userLogin = GsonUtils.getInstance().fromJson(json, UserLogin.class);
                    if (userLogin != null) {
                        if (!userLogin.getName().equals("")&&!userLogin.getIdentity().equals("")){
                            binding.etName.setText(userLogin.getName());
                            binding.etId.setText(userLogin.getIdentity());
                            binding.etName.setTextColor(getResources().getColor(R.color.reserverunfinish));
                            binding.etId.setTextColor(getResources().getColor(R.color.reserverunfinish));
                            binding.etName.setEnabled(false);
                            binding.etId.setEnabled(false);
                            binding.tvConfrim.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    //{
//    "name": "名字修改",
//    "identity": "1221221121121"
//}
    private void postData(String name, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("identity", id);
        new MyRequest().myRequest(API.INFO, true, map, true, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                Utils.snackbar(PersonalActivity.this, "个人信息上传成功");
                if (isRegister) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Utils.userParse(null, PersonalActivity.this);
                        }
                    }, 500);
                }else {
                    finish();
                }
            }
        });
    }
}
