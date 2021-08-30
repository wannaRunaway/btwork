
package com.kulun.energynet.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.kulun.energynet.MainApp;
import com.kulun.energynet.R;
import com.kulun.energynet.db.SharedPreferencesHelper;
import com.kulun.energynet.model.AuthResult;
import com.kulun.energynet.model.BindCar;
import com.kulun.energynet.model.PayResult;
import com.kulun.energynet.model.User;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.activity.BaseActivity;
import com.kulun.energynet.ui.activity.PayResultActivity;
import com.kulun.energynet.utils.Constant;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.ToastUtil;
import com.kulun.energynet.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Map;

public class PayActivity extends BaseActivity implements View.OnClickListener {

    private TextView mPayBack;
    private EditText mRechargeNum;
    private TextView mRechargeAmount1;
    private TextView mRechargeAmount2;
    private TextView mRechargeAmount3;
    private TextView mRechargeAmount4;
    private TextView mRechargeTypeAlipay;
    private TextView mRechargeTypeWechat;
    private TextView mToRecharge;
    private TextView tv_info, tv_huandian, tv_chongdian, tv_geren, tv_chengzuren;
    private LinearLayout li_huandian, li_chongdian, li_geren, li_chengzuren;
    private LinearLayout li_type;
    private Context mContext;
    private static PayActivity mInstance = null;
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_AUTH_FLAG = 2;

    
    public static PayActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInstance = this;
        Constants.rechargeType = 1;
        setContentView(R.layout.pay_main);
        bindView();
    }

    public void bindView() {
        mPayBack = (TextView) this.findViewById(R.id.pay_back);
        mRechargeNum = (EditText) this.findViewById(R.id.recharge_num);
        mRechargeAmount1 = (TextView) this.findViewById(R.id.recharge_amount_1);
        mRechargeAmount2 = (TextView) this.findViewById(R.id.recharge_amount_2);
        mRechargeAmount3 = (TextView) this.findViewById(R.id.recharge_amount_3);
        mRechargeAmount4 = (TextView) this.findViewById(R.id.recharge_amount_4);
        mRechargeTypeAlipay = (TextView) this.findViewById(R.id.recharge_type_alipay);
        mRechargeTypeWechat = (TextView) this.findViewById(R.id.recharge_type_wechat);
        mToRecharge = (TextView) this.findViewById(R.id.to_recharge);
        li_huandian = findViewById(R.id.li_huandian);
        li_chongdian = findViewById(R.id.li_chongdian);
        li_geren = findViewById(R.id.li_geren);
        li_chengzuren = findViewById(R.id.li_chengzuren);
        li_huandian.setOnClickListener(this);
        li_chongdian.setOnClickListener(this);
        li_geren.setOnClickListener(this);
        li_chengzuren.setOnClickListener(this);
        tv_huandian = findViewById(R.id.tv_huandian);
        tv_chongdian = findViewById(R.id.tv_chongdian);
        tv_geren = findViewById(R.id.tv_geren);
        tv_chengzuren = findViewById(R.id.tv_chengzuren);
        li_type = findViewById(R.id.li_type);
        mRechargeTypeAlipay.setSelected(true);
        mRechargeNum.setText("");
        mPayBack.setOnClickListener(this);
        mRechargeNum.setOnClickListener(this);
        mRechargeAmount1.setOnClickListener(this);
        mRechargeAmount2.setOnClickListener(this);
        mRechargeAmount3.setOnClickListener(this);
        mRechargeAmount4.setOnClickListener(this);
        mRechargeTypeAlipay.setOnClickListener(this);
        mRechargeTypeWechat.setOnClickListener(this);
        mToRecharge.setOnClickListener(this);
        tv_info = findViewById(R.id.tv_info);
        accountJudge();
    }

    //充电和换电是否显示

    /**
     * 充值页面的充值账户要根据绑定关系做筛选，只有绑定了自营出租车的对公司机才有“承租人账户”选项，并且不出现“个人账户”选项；
     * 非自营出租车的司机还是只有一个个人账户充值入口（不影响老业务）
     */
    private void accountJudge() {
        final String url = API.BASE_URL + API.URL_INFO;
        final RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        final AsyncHttpClient client = new AsyncHttpClient();
        //保存cookie，自动保存到了sharepreferences
        PersistentCookieStore myCookieStore = new PersistentCookieStore(PayActivity.this);
        client.setCookieStore(myCookieStore);
        client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        String json = new String(response);
                        Utils.log(url, params, json);
                        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                        JsonObject data = obj.get("content").getAsJsonObject();
                        boolean hasHuandian = data.get("hasChargeAccount").getAsBoolean();
                        if (hasHuandian) {
//            tv_chongdian.setVisibility(View.VISIBLE);
                            li_chongdian.setVisibility(View.VISIBLE);
                        } else {
//            tv_chongdian.setVisibility(View.INVISIBLE);
                            li_chongdian.setVisibility(View.GONE);
                        }
                        JsonArray bindCarList = data.get("bindCarList").getAsJsonArray();
                        ArrayList<BindCar> bindCarArrayList = new ArrayList<>();
                        for (int i = 0; i < bindCarList.size(); i++) {
                            JsonObject jsonObject = bindCarList.get(i).getAsJsonObject();
                            BindCar bindCar = new BindCar();
                            bindCar.setId(jsonObject.get("id").getAsInt());
                            bindCar.setPlate_number(jsonObject.get("plate_number").getAsString());
                            bindCar.setStatus(jsonObject.get("status").getAsInt());
                            bindCar.setVin(jsonObject.get("vin").getAsString());
                            if (jsonObject.has("account_type")){
                                bindCar.setAccount_type(jsonObject.get("account_type").getAsInt());
                            }
                            bindCar.setBusiness_type(jsonObject.get("business_type").getAsInt());
                            bindCarArrayList.add(bindCar);
                        }
//                        ArrayList<BindCar> bindCarArrayList = (ArrayList<BindCar>) GsonUtils.jsontoList(SharedPreferencesHelper.getInstance(this).getAccountString("bindCarList", ""));
                        if (bindCarArrayList.size() > 0) {
//            tv_huandian.setVisibility(View.VISIBLE);
                            li_huandian.setVisibility(View.VISIBLE);
                            li_type.setVisibility(View.VISIBLE);
                        } else {
//            tv_huandian.setVisibility(View.INVISIBLE);
                            li_huandian.setVisibility(View.GONE);
                            li_type.setVisibility(View.GONE);
                        }
                        if (!hasHuandian && bindCarArrayList.size() > 0) {
                            tv_huandian.setSelected(true);
                        }
                        if (hasHuandian && bindCarArrayList.size() == 0) {
                            tv_chongdian.setSelected(true);
                        }
                        boolean ischengzuren = false;
                        for (int i = 0; i < bindCarArrayList.size(); i++) {
                            if (bindCarArrayList.get(i).getAccount_type() == 1 && bindCarArrayList.get(i).getBusiness_type() == 6) {
                                ischengzuren = true;
                            }
                        }
                        if (ischengzuren){
                            li_chengzuren.setVisibility(View.VISIBLE);
                            li_geren.setVisibility(View.GONE);
                            tv_chengzuren.setSelected(true);
                        }else {
                            li_chengzuren.setVisibility(View.GONE);
                            li_geren.setVisibility(View.VISIBLE);
                            tv_geren.setSelected(true);
                        }
                        if (!hasHuandian && bindCarArrayList.size() == 0) {
                            tv_info.setText("当前无法充值，请联系客服");
                            tv_info.setTextSize(16);
                        } else {
                            tv_info.setText("充值账户");
                            tv_info.setTextSize(20);
                        }
                    }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + error.toString());
                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clear() {
        mRechargeAmount1.setSelected(false);
        mRechargeAmount2.setSelected(false);
        mRechargeAmount3.setSelected(false);
        mRechargeAmount4.setSelected(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_back:
                finish();
                break;

            case R.id.li_huandian:
                tv_huandian.setSelected(true);
                tv_chongdian.setSelected(false);
                break;

            case R.id.li_chongdian:
                tv_huandian.setSelected(false);
                tv_chongdian.setSelected(true);
                break;

            case R.id.li_geren:
                tv_geren.setSelected(true);
                tv_chengzuren.setSelected(false);
                break;

            case R.id.li_chengzuren:
                tv_geren.setSelected(false);
                tv_chengzuren.setSelected(true);
                break;

            case R.id.recharge_num:
                clear();
                break;

            case R.id.recharge_amount_1:
                clear();
                mRechargeNum.setText("100");
                mRechargeAmount1.setSelected(true);
                break;

            case R.id.recharge_amount_2:
                clear();
                mRechargeNum.setText("200");
                mRechargeAmount2.setSelected(true);
                break;

            case R.id.recharge_amount_3:
                clear();
                mRechargeNum.setText("300");
                mRechargeAmount3.setSelected(true);
                break;

            case R.id.recharge_amount_4:
                clear();
                mRechargeNum.setText("500");
                mRechargeAmount4.setSelected(true);
                break;

            case R.id.recharge_type_alipay:
                mRechargeTypeAlipay.setSelected(true);
                mRechargeTypeWechat.setSelected(false);
                Constants.rechargeType = 1;
                break;

            case R.id.recharge_type_wechat:
                mRechargeTypeAlipay.setSelected(false);
                mRechargeTypeWechat.setSelected(true);
                Constants.rechargeType = 0;
                break;

            case R.id.to_recharge:
                String s = mRechargeNum.getText().toString();
                if (!tv_huandian.isSelected() && !tv_chongdian.isSelected()) {
                    Utils.toast(mContext, "请选择充值账户");
                    return;
                }
                if (!tv_geren.isSelected() && !tv_chengzuren.isSelected()) {
                    Utils.toast(mContext, "请选择充值类型");
                    return;
                }
                if ("".equals(s)) {
                    ToastUtil.showToast(mContext, "请填写充值金额");
                    return;
                }
                final Double rechargeAmount = Double.valueOf(s);
//                if(rechargeAmount < 100) {
//                    ToastUtil.showToast(mContext, "充值金额至少为100");
//                    return;
//                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url = API.BASE_URL + API.URL_PAY;
                        RequestParams params = new RequestParams();
                        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
                        params.add("rechargeAmount", rechargeAmount.toString());
                        params.add("type", Constants.rechargeType.toString());
                        if (tv_chongdian.isSelected()) {
                            params.add("tradeType", "50");
                        }
                        if (tv_chengzuren.isSelected()) {
                            params.add("tradeType", "2");
                        }
                        final AsyncHttpClient client = new AsyncHttpClient();
                        //保存cookie，自动保存到了sharepreferences
                        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                        client.setCookieStore(myCookieStore);
                        Log.d(Utils.TAG, "url:" + url);
                        Log.d(Utils.TAG, "requestParams:" + params);
                        client.get(url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                String json = new String(response);
                                Log.d(Utils.TAG, "json:" + json);
                                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                Integer code = obj.get("code").getAsInt();
                                if (code == -1) {
                                    ToastUtil.showToast(mContext, obj.get("msg").getAsString());
                                    accountJudge();
                                    return;
                                }
                                JsonObject data = obj.get("content").getAsJsonObject();

                                Integer type = data.get("type").getAsInt();

                                if (type == 1) {
                                    String tradeCode = data.get("tradeCode").getAsString();
                                    View v = new View(mContext);
                                    payV2(v, tradeCode);
                                } else if (type == 0) {
                                    JsonObject paramsMap = data.get("paramsMap").getAsJsonObject();
                                    User.getInstance().setWxAppid(paramsMap.get("appid").getAsString());
                                    wxpay(paramsMap);
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                                Log.d(Utils.TAG, "requestParams:" + e.toString());
//                                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                }, 200);
                break;

            default:
                break;
        }
    }

    public void wxpay(JsonObject paramsMap) {
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.wx_appid);
        PayReq request = new PayReq();
        request.appId = paramsMap.get("appid").getAsString();
        request.partnerId = paramsMap.get("partnerid").getAsString();
        request.prepayId = paramsMap.get("prepayid").getAsString();
        request.packageValue = paramsMap.get("package").getAsString();
        request.nonceStr = paramsMap.get("noncestr").getAsString();
        request.timeStamp = paramsMap.get("timestamp").getAsString();
        request.sign = paramsMap.get("sign").getAsString();
        boolean b = api.sendReq(request);
    }

    public void payV2(View v, final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent message = new Intent(MainApp.getInstance().getApplicationContext(), PayResultActivity.class);
                        message.putExtra("isPackage", false);
                        message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainApp.getInstance().getApplicationContext().startActivity(message);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

}
