package com.kulun.kulunenergy.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityInfoBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.Customize;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.requestparams.Myparams;
import com.kulun.kulunenergy.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/8/28
 */
public class InfoActivity extends BaseActivity {
    private ActivityInfoBinding binding;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info);
        binding.imgClose.setOnClickListener(v -> finish());
        binding.tvConfrim.setOnClickListener(view -> {
            String name = binding.etName.getText().toString();
            String id = binding.etId.getText().toString();
            if (TextUtils.isEmpty(name)) {
                Utils.snackbar(InfoActivity.this, "请填写姓名");
                return;
            }else if(!name.matches("[\u4E00-\u9FA5]+")){
                Utils.snackbar(InfoActivity.this, "姓名须为中文");
                return;
            }
            if (!TextUtils.isEmpty(id)&&!Utils.IDCardValidate(id)){
                Utils.snackbar( InfoActivity.this, "身份证号格式不正确");
                return;
            }
            loadData(name, id);
        });
    }

    //name       [string]	是	姓名
    //identity   [string]	是	身份证号码
    private void loadData(String name, String id) {
        String url = API.BASE_URL + API.UPDATEAUTH;
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("identity", id);
        params.add(Customize.SIGN, Customize.getRequestParams(map, params));
        new MyRequest().myrequest(url, params, InfoActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                Intent intent = new Intent(InfoActivity.this, AddCarActivity.class);
                intent.putExtra(API.islogin, true);
                startActivity(intent);
            }
        });
    }
}
