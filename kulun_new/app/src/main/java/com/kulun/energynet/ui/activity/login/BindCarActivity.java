package com.kulun.energynet.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityBindCarBinding;
import com.kulun.energynet.db.SharedPreferencesHelper;
import com.kulun.energynet.model.BindCar;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.chexing.BindCarModel;
import com.kulun.energynet.model.chexing.ChexingContent;
import com.kulun.energynet.model.chexing.ChexingModel;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.activity.BaseActivity;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by xuedi on 2019/6/28
 */
public class BindCarActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ActivityBindCarBinding binding;
    private ChexingModel chexingModel;
    private int carTypeId;
    //    private String phone;
    private boolean isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bind_car);
//      phone = User.getInstance().getPhone();
        isRegister = getIntent().getBooleanExtra("isregister", false);
        binding.tvJump.setOnClickListener(this);
        binding.tvBindingConfirm.setOnClickListener(this);
        binding.tvBack.setOnClickListener(this);
        binding.tvJump.setVisibility(isRegister ? View.VISIBLE : View.GONE);
        loadData();
    }

    private void loadData() {
        final String url = API.BASE_URL + API.GET_CAR_TYPE;
        final RequestParams requestParams = new RequestParams();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                chexingModel = GsonUtils.getInstance().fromJson(json, ChexingModel.class);
                if (chexingModel.isSuccess()) {
                    initAdapter(chexingModel.getContent());
                } else {
                    Utils.toast(BindCarActivity.this, chexingModel.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(BindCarActivity.this, API.error_internet);
            }
        });
    }

    private void initAdapter(List<ChexingContent> content) {
        ArrayList<String> list = new ArrayList<>();
        list.add("请选择车型");
        for (int i = 0; i < content.size(); i++) {
            list.add(content.get(i).getName());
        }
//        list.add("其他车型");
        ArrayAdapter<String> partAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        partAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerChejia.setAdapter(partAdapter);
        binding.spinnerChejia.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jump:
                toLoginActivity();
                break;
            case R.id.tv_binding_confirm:
                uploadCarplate();
                break;
            case R.id.tv_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 上传车牌和车型
     * phone
     * [string]	是	账户电话号码
     * plateNumber
     * [string]	是	绑定车牌
     * carTypeId
     * [string]	是	绑定车型Id
     */
    private void uploadCarplate() {
        String plate = binding.etCarplate.getText().toString();
        if (plate.isEmpty()) {
            Utils.toast(this, "车牌号码不能为空，请填写车牌号码");
            return;
        }
        if (plate.length() > 8) {
            Utils.toast(this, "车牌号码位数不能超过8位");
            return;
        }
        if (plate.length() < 7) {
            Utils.toast(this, "车牌号码位数不能小于7位");
            return;
        }
        if (!Utils.checkCarNumber(plate)) {
            Utils.toast(this, "车牌含有特殊字符");
            return;
        }
        if (carTypeId == 0) {
            Utils.toast(this, "请选择车型");
            return;
        }
        final String url = API.BASE_URL + API.UPLOAD_CARPLATE;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("phone", User.getInstance().getPhone());
        requestParams.add("plateNumber", plate);
        requestParams.add("carTypeId", String.valueOf(carTypeId));
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                BindCarModel bindCarModel = GsonUtils.getInstance().fromJson(json, BindCarModel.class);
                if (bindCarModel.isSuccess()) {
                    Utils.toast(BindCarActivity.this, "车牌绑定完成");
                    if (isRegister) {
                        toLoginActivity();
                    } else {
                        //查询充电车
                        final String url = API.BASE_URL + API.URL_INFO;
                        final RequestParams params = new RequestParams();
                        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
                        final AsyncHttpClient client = new AsyncHttpClient();
                        //保存cookie，自动保存到了sharepreferences
                        PersistentCookieStore myCookieStore = new PersistentCookieStore(BindCarActivity.this);
                        client.setCookieStore(myCookieStore);
                        client.post(url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                String json = new String(response);
                                Utils.log(url, params, json);
                                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                JsonObject data = obj.get("content").getAsJsonObject();
                                JsonArray chongdianBindCarList = null;
                                if (!data.get("chargeBindCarList").equals(JsonNull.INSTANCE)) {
                                    chongdianBindCarList = data.get("chargeBindCarList").getAsJsonArray();
                                    ArrayList<BindCar> chongdianbindCarArrayList = new ArrayList<>();
                                    for (int i = 0; i < chongdianBindCarList.size(); i++) {
                                        JsonObject jsonObject = chongdianBindCarList.get(i).getAsJsonObject();
                                        BindCar bindCar = new BindCar();
                                        bindCar.setId(jsonObject.get("id").getAsInt());
                                        bindCar.setPlate_number(jsonObject.get("plate_number").getAsString());
                                        bindCar.setStatus(jsonObject.get("status").getAsInt());
                                        chongdianbindCarArrayList.add(bindCar);
                                    }
                                    SharedPreferencesHelper.getInstance(BindCarActivity.this).putAccountString("chargeBindCarList", GsonUtils.getInstance().toJson(chongdianbindCarArrayList));
                                }
                                finish();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                finish();
                            }
                        });
                    }
                } else {
                    Utils.toast(BindCarActivity.this, bindCarModel.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(BindCarActivity.this, API.error_internet);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_chejia:
                String item = (String) adapterView.getSelectedItem();
                for (int j = 0; j < chexingModel.getContent().size(); j++) {
                    if (item.equals(chexingModel.getContent().get(j).getName())) {
                        carTypeId = chexingModel.getContent().get(j).getId();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    //登录注册流程完成，去主页面app
    private void toLoginActivity() {
        Utils.toast(BindCarActivity.this, "注册成功，请登录");
        Intent intent = new Intent(BindCarActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
