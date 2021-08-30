package com.kulun.kulunenergy.mine;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivitySystemSettingBinding;
import com.kulun.kulunenergy.login.PasswordLoginActivity;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.model.ServiceListModel;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.Customize;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.requestparams.Myparams;
import com.kulun.kulunenergy.utils.SharePref;
import com.loopj.android.http.RequestParams;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;
import java.util.HashMap;
import java.util.Map;
public class SystemSettingActivity extends BaseActivity {
    private ActivitySystemSettingBinding binding;
    //private String apkpath;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_system_setting);
        binding.header.title.setText("设置");
        //apkpath = getIntent().getStringExtra(API.apppath);
        binding.header.left.setOnClickListener(view -> finish());
        binding.rePersonl.setOnClickListener(view -> {
            Intent intent = new Intent(SystemSettingActivity.this, PersonalActivity.class);
            startActivity(intent);
        });
        binding.reChangePassword.setOnClickListener(view -> {
            Intent intent = new Intent(SystemSettingActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        });
        binding.tvLoginOut.setOnClickListener(view -> {
            loginOut();
        });
    }

    private EasyPopup easyPopup;
    private void loadData() {
        String url = API.BASE_URL + API.SERVICELIST;
        RequestParams requestParams = new RequestParams();
        Map<String,String> map = new HashMap<>();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, SystemSettingActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                ServiceListModel model = GsonUtils.getInstance().fromJson(json, ServiceListModel.class);
//                showEasyPopup(model);
            }
        });
    }

//    private void showEasyPopup(ServiceListModel model) {
//        easyPopup = new EasyPopup(this)
//                .setContentView(R.layout.layout_easy_popup)
//                //是否允许点击PopupWindow之外的地方消失
//                .setFocusAndOutsideEnable(true)
//                .createPopup();
//        easyPopup.showAtAnchorView(binding.reConnectService, VerticalGravity.BELOW, HorizontalGravity.ALIGN_RIGHT, 0, 0);
//        TextView tv_phone = easyPopup.getView(R.id.tv_phone);
//        TextView tv_time = easyPopup.getView(R.id.tv_time);
//        TextView tv_call = easyPopup.getView(R.id.tv_call);
//        tv_phone.setText("客服电话\n"+model.getContent().get(0).getValue());
//        tv_time.setText(model.getContent().get(1).getValue()+"\n"+model.getContent().get(2).getValue());
//        tv_call.setOnClickListener(view -> {
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            Uri data = Uri.parse("tel:" + model.getContent().get(0).getValue());
//            intent.setData(data);
//            startActivity(intent);
//        });
//    }

    private void loginOut() {
        String url = API.BASE_URL + API.LOGINOUT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, SystemSettingActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                SharePref.put(SystemSettingActivity.this, API.bindId, 0);
                SharePref.put(SystemSettingActivity.this, API.carId, 0);
                SharePref.put(SystemSettingActivity.this, API.plateNo, "");
                SharePref.put(SystemSettingActivity.this, API.carType, 0);
                SharePref.put(SystemSettingActivity.this, API.batterycount, 0);
//                User.getInstance().setYuyue(false);
//                User.getInstance().setDelay(false);
                User.getInstance().setIsneedlogin(true);
//                User.getInstance().setIsneedcheckIdcard(false);
//                    User.getInstance().setStation(null);
                Intent intent = new Intent(SystemSettingActivity.this, PasswordLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}