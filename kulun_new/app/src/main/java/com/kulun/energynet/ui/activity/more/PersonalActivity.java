package com.kulun.energynet.ui.activity.more;

import android.os.Bundle;
import android.renderscript.BaseObj;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityPersonalBinding;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.loginmodel.SmscodeModel;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.activity.BaseActivity;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * created by xuedi on 2019/7/3
 */
public class PersonalActivity extends BaseActivity implements View.OnClickListener {
    private ActivityPersonalBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal);
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
                if (binding.etName.getText().toString().isEmpty()){
                    Utils.toast(PersonalActivity.this, "姓名不能为空");
                    return;
                }
                if (!Utils.checkname(binding.etName.getText().toString())){
                    Utils.toast(PersonalActivity.this, "姓名不能包含特殊字符");
                    return;
                }
                if (binding.etIdcard.getText().toString().isEmpty()){
                    Utils.toast(PersonalActivity.this, "请输入身份证号");
                    return;
                }
                if (!Utils.IDCardValidate(binding.etIdcard.getText().toString())){
                    Utils.toast(PersonalActivity.this, "身份证号格式不正确");
                    return;
                }
                uploadInfo();
                break;
            default:
                break;
        }
    }
    private void uploadInfo() {
        /**
         * accountId
         * [int]	是	账户id
         * name
         * [string]	是	姓名
         * identity
         * [string]	是	身份证
         */
        final String url = API.BASE_URL + API.PERSONALINFO_UPLOAD;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        requestParams.add("name", binding.etName.getText().toString());
        requestParams.add("identity", binding.etIdcard.getText().toString());
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url,requestParams,json);
                SmscodeModel model = GsonUtils.getInstance().fromJson(json, SmscodeModel.class);
                if (model.isSuccess()){
                    Utils.toast(PersonalActivity.this, "修改信息成功");
                    finish();
                }else {
                    Utils.toast(PersonalActivity.this, model.getMsg());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(PersonalActivity.this, API.error_internet);
            }
        });
    }
}
