package com.btkj.chongdianbao.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityPersonalBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/8/28
 */
public class PersonalActivity extends BaseActivity {
    private ActivityPersonalBinding binding;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal);
        binding.imgClose.setOnClickListener(view -> finish());
        binding.tvConfrim.setOnClickListener(view -> {
            String name = binding.etName.getText().toString();
            String id = binding.etId.getText().toString();
            if (TextUtils.isEmpty(name)) {
                Utils.snackbar(getApplicationContext(), PersonalActivity.this, "请填写姓名");
                return;
            } else if(!name.matches("[\u4E00-\u9FA5]+")){
                Utils.snackbar(getApplicationContext(), PersonalActivity.this, "姓名须为中文");
                return;
            }
            if (!TextUtils.isEmpty(id)&&!Utils.IDCardValidate(id)){
                Utils.snackbar(getApplicationContext(), PersonalActivity.this, "身份证号格式不正确");
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
        new MyRequest().myrequest(url, params, PersonalActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                Utils.snackbar(getApplicationContext(), PersonalActivity.this, "个人信息上传成功");
                new Handler().postDelayed(() -> {
                    finish();
                }, 500);
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.setCookieStore(cookieStore);
//        httpClient.post(url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, params, json);
//                BaseObject object = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (object.isSuccess()) {
//                    Utils.snackbar(getApplicationContext(), PersonalActivity.this, "个人信息上传成功");
//                    new Handler().postDelayed(() -> {
//                        finish();
//                    }, 500);
//                } else {
//                    Utils.snackbar(getApplicationContext(), PersonalActivity.this, object.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), PersonalActivity.this, API.net_error);
//                Utils.log(url, params, null);
//            }
//        });
    }
}
