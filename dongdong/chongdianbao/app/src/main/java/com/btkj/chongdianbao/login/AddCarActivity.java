package com.btkj.chongdianbao.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityAddCarBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * created by xuedi on 2019/8/8
 */
public class AddCarActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAddCarBinding binding;
    private boolean islogin = false;
    private long time;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_car);
        binding.imgFinish.setOnClickListener(this);
        binding.imgNext.setOnClickListener(this);
        binding.layoutHeader.left.setOnClickListener(this);
        binding.layoutHeader.right.setOnClickListener(this);
        binding.layoutHeader.title.setText("添加车辆");
        islogin = getIntent().getBooleanExtra(API.islogin, false);
        binding.layoutHeader.right.setVisibility(islogin?View.VISIBLE:View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.right:
                toMainActivity();
                break;
            case R.id.img_finish:
                if (System.currentTimeMillis() - time < 5){
                    time = System.currentTimeMillis();
                    Utils.snackbar(getApplicationContext(), AddCarActivity.this, "点击过快");
                    return;
                }
                String carPlate = binding.etCarplate.getText().toString();
                if (TextUtils.isEmpty(carPlate)) {
                    Utils.snackbar(getApplicationContext(), AddCarActivity.this, "车牌号码不能为空");
                    return;
                }
                if (carPlate.length() > 8) {
                    Utils.snackbar(getApplicationContext(), AddCarActivity.this, "车牌号码位数不能超过8位");
                    return;
                }
                if (carPlate.length() < 7) {
                    Utils.snackbar(getApplicationContext(), AddCarActivity.this, "车牌号码位数不能小于7位");
                    return;
                }
                if (!checkCarNumber(carPlate)) {
                    Utils.snackbar(getApplicationContext(), AddCarActivity.this,"车牌含有特殊字符");
                    return;
                }
                addCar(true, carPlate);
                break;
            case R.id.img_next:
                String carPlates= binding.etCarplate.getText().toString();
                if (TextUtils.isEmpty(carPlates)) {
                    Utils.snackbar(getApplicationContext(), AddCarActivity.this, "车牌号不能为空");
                    return;
                }
                addCar(false, carPlates);
                break;
            default:
                break;
        }
    }

    /**
     * plateNo [string]	是	车牌号
     */
    private void addCar(final boolean isFinish, String carPlates) {
        final String url = API.BASE_URL + API.ADDCAR;
        final RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("plateNo", carPlates);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, AddCarActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                EventBus.getDefault().post("refreshmycar");
                if (isFinish){
                    toMainActivity();
                }else {
                    binding.etCarplate.setText("");
                }
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
//                AddCarModel model = GsonUtils.getInstance().fromJson(json, AddCarModel.class);
//                if (model.isSuccess()) {
//                    if (isFinish){
//                        toMainActivity();
//                    }else {
//                        binding.etCarplate.setText("");
//                    }
//                } else {
//                    if (!model.getMsg().isEmpty()) {
//                        Utils.snackbar(getApplicationContext(), AddCarActivity.this, model.getMsg());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.log(url, requestParams, "失败");
//                Utils.snackbar(getApplicationContext(), AddCarActivity.this, API.net_error);
//            }
//        });
    }

    private void toMainActivity() {
        if (islogin) {
            Utils.snackbar(getApplicationContext(), AddCarActivity.this, "注册成功，请登录");
            Intent intent = new Intent(AddCarActivity.this, PasswordLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            finish();
        }
    }

    /**
     * 你就把非数字 字母  非地区简称首字都排除掉吧
     * 还有不含字母o和l
     */
    public boolean checkCarNumber(String content) {
        String pattern = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
        return Pattern.matches(pattern, content);
    }
}
