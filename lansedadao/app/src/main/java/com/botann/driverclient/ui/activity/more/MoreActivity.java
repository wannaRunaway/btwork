package com.botann.driverclient.ui.activity.more;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.botann.driverclient.BaseObjectString;
import com.botann.driverclient.BuildConfig;
import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityMoreBinding;
import com.botann.driverclient.db.SharedPreferencesHelper;
import com.botann.driverclient.model.BindCar;
import com.botann.driverclient.model.User;
import com.botann.driverclient.model.customerservice.BaseCustomerServicebean;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.MyPlatelistActivity;
import com.botann.driverclient.ui.activity.login.DriverCarInfoModel;
import com.botann.driverclient.ui.activity.login.LoginActivity;
import com.botann.driverclient.ui.activity.login.PrivacyPolicyActivity;
import com.botann.driverclient.ui.activity.login.UploadDriverCarInfoActivity;
import com.botann.driverclient.ui.activity.login.UseProtocolActivity;
import com.botann.driverclient.ui.activity.promotions.PromotionsActivity;
import com.botann.driverclient.ui.activity.promotions.PromotionsRecordActivity;
import com.botann.driverclient.ui.activity.refund.ReFundChongdianActivity;
import com.botann.driverclient.ui.activity.refund.ReFundHuandianActivity;
import com.botann.driverclient.ui.activity.refund.ReFundListActivity;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * created by xuedi on 2019/1/8
 */
public class MoreActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMoreBinding binding;
    private Dialog mQuitDialog;
    /**
     * 客服请求界面列表
     */
    private BaseCustomerServicebean customerServicebeanList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_more);
        binding.reActivity.setOnClickListener(this);
        binding.reCustomer.setOnClickListener(this);
        binding.tvLoginout.setOnClickListener(this);
        binding.reActivityRecord.setOnClickListener(this);
//        binding.rePersonal.setOnClickListener(this);
        binding.reChangePassword.setOnClickListener(this);
        binding.tvBack.setOnClickListener(this);
        binding.reRefundChongdian.setOnClickListener(this);
        binding.reRefundHuandian.setOnClickListener(this);
        binding.reRefundList.setOnClickListener(this);
        binding.reXieyi.setOnClickListener(this);
        binding.reYingsi.setOnClickListener(this);
        binding.reActivityPlateList.setOnClickListener(this);
        binding.tvVersion.setText("应用版本   " + BuildConfig.VERSION_NAME + "-" + BuildConfig.VERSION_CODE);
        refundShowOrHide();
        loadCustomerList();
    }

    private void refundShowOrHide() {
        //nt_type` int(2) NOT NULL DEFAULT '1' COMMENT '0：换电账户；1 充电账户；2 即是换电又是充电； 3 两者都不是'
        switch (SharedPreferencesHelper.getInstance(MoreActivity.this).getInt(API.accountType, -1)){
            case 0:
                if (SharedPreferencesHelper.getInstance(MoreActivity.this).getBoolean(API.exchangeAccountAppRefund, false)) {
                    binding.reRefundHuandian.setVisibility(View.VISIBLE);
                }
                binding.reActivityPlateList.setVisibility(View.VISIBLE);
                break;
            case 1:
                binding.reRefundChongdian.setVisibility(View.VISIBLE);
                binding.reActivityPlateList.setVisibility(View.GONE);
                break;
            case 2:
                if (SharedPreferencesHelper.getInstance(MoreActivity.this).getBoolean(API.exchangeAccountAppRefund, false)) {
                    binding.reRefundHuandian.setVisibility(View.VISIBLE);
                }
                binding.reRefundChongdian.setVisibility(View.VISIBLE);
                binding.reActivityPlateList.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.reActivityPlateList.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
    /**
     * 请求客服列表界面
     */
    private void loadCustomerList() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String Url = API.BASE_URL + API.URL_CUSTOMERSERVICELIST;
                RequestParams requestParams = new RequestParams();
                requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
                Log.d(Utils.TAG, "请求url:" + Url);
                Log.d(Utils.TAG, "请求参数" + requestParams);
                AsyncHttpClient httpClient = new AsyncHttpClient();
                final PersistentCookieStore cookieStore = new PersistentCookieStore(MoreActivity.this);
                httpClient.setCookieStore(cookieStore);
                httpClient.post(Url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d(Utils.TAG, "返回json" + responseBody.toString());
                        String json = new String(responseBody);
                        customerServicebeanList = GsonUtils.getInstance().fromJson(json, BaseCustomerServicebean.class);
                        if (customerServicebeanList.getContent().size() > 0) {
                            binding.reCustomer.setVisibility(View.VISIBLE);
                        } else {
                            binding.reCustomer.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(MoreActivity.this, "请求失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止附属在activity上的dialog因为finish()而内存溢出
        if (mQuitDialog != null) {
            mQuitDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.re_activity:
                intent = new Intent(MoreActivity.this, PromotionsActivity.class);
                startActivity(intent);
                break;
            case R.id.re_customer:
                connectCustomer();
                break;
            case R.id.tv_loginout:
                loginout();
                break;
            case R.id.re_activity_record:
                intent = new Intent(MoreActivity.this, PromotionsRecordActivity.class);
                intent.putExtra("isPayResult", false);
                startActivity(intent);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.re_change_password:
                intent = new Intent(MoreActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
//            case R.id.re_personal:
//                SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
//                if (!preferences.getBoolean("hasAuthAccount", false)){
//                    intent = new Intent(MoreActivity.this, PersonalActivity.class);
//                    startActivity(intent);
//                }else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
//                    builder.setMessage("您已经修改了个人信息，无法再修改了");
//                    builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//                    builder.show();
//                }
//                break;
            case R.id.re_refund_chongdian:
                intent = new Intent(MoreActivity.this, ReFundChongdianActivity.class);
                startActivity(intent);
                break;
            case R.id.re_refund_huandian:
                intent = new Intent(MoreActivity.this, ReFundHuandianActivity.class);
                startActivity(intent);
                break;
            case R.id.re_refund_list:
                intent = new Intent(MoreActivity.this, ReFundListActivity.class);
                startActivity(intent);
                break;
//            case R.id.re_upload_carinfo:
//                loadCarInfo();
//                break;
            case R.id.re_xieyi:
                intent = new Intent(MoreActivity.this, UseProtocolActivity.class);
                startActivity(intent);
                break;
            case R.id.re_yingsi:
                intent = new Intent(MoreActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
                break;
            case R.id.re_activity_plate_list:
                intent = new Intent(MoreActivity.this, MyPlatelistActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

//    private void loadCarInfo() {
//        final RequestParams requestParams = new RequestParams();
//        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
//        final String url = API.BASE_URL + API.DRIVERCARINFO;
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        PersistentCookieStore cookieStore = new PersistentCookieStore(MoreActivity.this);
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                final String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                try {
//                    JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
//                    String msg = obj.get("msg").getAsString();
//                    if (msg == null || msg.equals("")){
//                        BaseObjectString baseObjectString = GsonUtils.getInstance().fromJson(json, BaseObjectString.class);
//                        if (baseObjectString.getContent() == null){
//                            Intent intents = new Intent(MoreActivity.this, UploadDriverCarInfoActivity.class);
//                            intents.putExtra(API.islogin, true);
//                            intents.putExtra("type", 0);
//                            startActivity(intents);
//                        }
//                    }else {
//                            DriverCarInfoModel model = GsonUtils.getInstance().fromJson(json, DriverCarInfoModel.class);
//                            if (model != null) {
//                                if (model.isSuccess()) {
//                                    if (model.getContent() == null) {
//                                        Intent intents = new Intent(MoreActivity.this, UploadDriverCarInfoActivity.class);
//                                        intents.putExtra(API.islogin, true);
//                                        intents.putExtra("type", 0);
//                                        startActivity(intents);
//                                    } else {
//                                        switch (model.getContent().getStatus()) {
//                                            case 0: //审核
//                                                new AlertDialog.Builder(MoreActivity.this).setMessage("正在审核中")
//                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                                dialogInterface.dismiss();
//                                                            }
//                                                        }).create().show();
//                                                break;
//                                            case 1://成功
//                                                Intent intents = new Intent(MoreActivity.this, UploadDriverCarInfoActivity.class);
//                                                intents.putExtra(API.islogin, true);
//                                                intents.putExtra("type", 0);
//                                                intents.putExtra("data", model.getContent());
//                                                intents.putExtra("success", true);
//                                                startActivity(intents);
//                                                break;
//                                            case 2://失败
//                                                Intent intent = new Intent(MoreActivity.this, UploadDriverCarInfoActivity.class);
//                                                intent.putExtra(API.islogin, true);
//                                                intent.putExtra("type", 0);
//                                                intent.putExtra("data", model.getContent());
//                                                intent.putExtra("success", false);
//                                                startActivity(intent);
//                                                break;
//                                            default:
//                                                break;
//                                        }
//                                    }
//                                }
//                        }
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                Utils.toast(MoreActivity.this, "服务器繁忙，请稍候...");
//            }
//        });
//    }

    /**
     * 联系客服，只有一个显示弹窗，多个进入客服列表界面
     */
    private void connectCustomer() {
        if (customerServicebeanList.getContent() != null && customerServicebeanList.getContent().size() == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
            builder.setTitle(customerServicebeanList.getContent().get(0).getDescription());
            builder.setMessage(customerServicebeanList.getContent().get(0).getPhone() + "\n" + customerServicebeanList.getContent().get(0).getWorkTime());
            builder.setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callPhone(customerServicebeanList.getContent().get(0).getPhone());
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            Intent intent = new Intent(MoreActivity.this, CustomerServiceActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    private void loginout() {
        mQuitDialog = new Dialog(MoreActivity.this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(MoreActivity.this).inflate(R.layout.quit_account, null);
        root.findViewById(R.id.btn_confirm_quit).setOnClickListener(btnlistener);
        root.findViewById(R.id.btn_cancel).setOnClickListener(btnlistener);
        mQuitDialog.setContentView(root);
        Window dialogWindow = mQuitDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = -20;
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mQuitDialog.setCanceledOnTouchOutside(true);
        mQuitDialog.show();
    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 确认退出
                case R.id.btn_confirm_quit:
                    SharedPreferences.Editor consume = getSharedPreferences("consume", MODE_PRIVATE).edit();
                    consume.clear();
                    consume.commit();
                    SharedPreferences.Editor recharge = getSharedPreferences("recharge", MODE_PRIVATE).edit();
                    recharge.clear();
                    recharge.commit();
                    SharedPreferences.Editor coupon = getSharedPreferences("coupon", MODE_PRIVATE).edit();
                    coupon.clear();
                    coupon.commit();
                    Intent toLogin = new Intent(MoreActivity.this, LoginActivity.class);
                    toLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(toLogin);
//                    qiyuLogout();
                    break;
                // 取消
                case R.id.btn_cancel:
                    if (mQuitDialog != null) {
                        mQuitDialog.dismiss();
                    }
                    break;
            }
        }
    };

    /**
     * 七鱼退出
     */
//    private void qiyuLogout(){
//        Unicorn.logout();
//    }
}
