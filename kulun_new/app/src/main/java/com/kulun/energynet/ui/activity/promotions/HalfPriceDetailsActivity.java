package com.kulun.energynet.ui.activity.promotions;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alipay.sdk.app.PayTask;
import com.kulun.energynet.MainApp;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.LayoutPromotionCountBinding;
import com.kulun.energynet.inter.PromotionCarConfirm;
import com.kulun.energynet.model.AuthResult;
import com.kulun.energynet.model.Carinfo.CarContent;
import com.kulun.energynet.model.Carinfo.Carinfo;
import com.kulun.energynet.model.PayResult;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.promotions.PromotionDetail;
import com.kulun.energynet.model.promotions.Promotions;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.activity.PayResultActivity;
import com.kulun.energynet.ui.basepopup.CountPromotionPopup;
import com.kulun.energynet.ui.basepopup.MonthPopup;
import com.kulun.energynet.ui.basepopup.MonthTypePopup;
import com.kulun.energynet.ui.basepopup.PayPopup;
import com.kulun.energynet.ui.fragment.CarSelectorDialogFragment;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.DateUtils;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.ToastUtil;
import com.kulun.energynet.utils.Utils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class HalfPriceDetailsActivity extends AppCompatActivity implements View.OnClickListener, PromotionCarConfirm {
    private LayoutPromotionCountBinding binding;
    private Promotions promotions;
    private CountPromotionPopup promotionPopup;
    private PayPopup payPopup;
    private MonthPopup monthPopup;
    private MonthTypePopup monthTypePopup;
    private String carplateNum, name;
    private int bindId;
    private CarContent carContent;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private String activityType;
    private int startmonth;
    private boolean isshangbanyue = true;
    private long yuyueTime;
    private boolean flag = false;
    private String reason = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_promotion_count);
        promotions = (Promotions) getIntent().getSerializableExtra("promotions");
        activityType = getIntent().getStringExtra("type");
        binding.tvBack.setOnClickListener(this);
        binding.tvJoin.setOnClickListener(this);
        binding.tvType.setText("活动类型:" + activityType);
        binding.tvTime.setText("活动时间:" + DateUtils.stampToYear(promotions.getStartTime()) + "~~" + DateUtils.stampToYear(promotions.getEndTime()));
        binding.tvName.setText("活动名称:" + promotions.getName());
        startmonth = getMonth();
        promotionPopup = new CountPromotionPopup(HalfPriceDetailsActivity.this);
        payPopup = new PayPopup(this);
        monthPopup = new MonthPopup(this);
        monthTypePopup = new MonthTypePopup(this);
        loadData();
        initPromotionPopup();
        initPayPopup();
        initMonthPopup();
        initMonthTypePopup();
        initTime();
        carSelectorDialogFragment = new CarSelectorDialogFragment();
    }

    private void initMonthTypePopup() {
        monthTypePopup.tv1.setOnClickListener(this);
        monthTypePopup.tv2.setOnClickListener(this);
    }

    private void initMonthPopup() {
        monthPopup.tv1.setOnClickListener(this);
        monthPopup.tv2.setOnClickListener(this);
        monthPopup.tv1.setText(getMonth() + "月");
        monthPopup.tv2.setText(getMonth() + 1 + "月");
    }

    private int getMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    private int getYear() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.YEAR);
        return month;
    }

    private void initPayPopup() {
        payPopup.tv_weixing.setOnClickListener(this);
        payPopup.tv_alipay.setOnClickListener(this);
    }

    /**
     * 弹出的popup设置
     */
    private void initPromotionPopup() {
        promotionPopup.re.setOnClickListener(this);
        promotionPopup.tv_buy.setOnClickListener(this);
        promotionPopup.tv1.setText(startmonth + "月");
        promotionPopup.tv2.setText("上半月");
        SharedPreferences sharedPreferences = MainApp.getInstance().getSharedPreferences("carplate", Context.MODE_PRIVATE);
        String carplateSave = sharedPreferences.getString("carplate" + User.getInstance().getAccountId(), "");
        bindId = sharedPreferences.getInt("id" + User.getInstance().getAccountId(), 0);
        carplateNum = carplateSave;
        if (carplateSave == null || carplateSave.equals("")) {
            carplateSave = "请选择车牌";
        }
        promotionPopup.tv_carplatenum.setText(carplateSave);
        promotionPopup.tv_activity_time.setText(DateUtils.stampToYear(promotions.getStartTime()) + "~" + DateUtils.stampToYear(promotions.getEndTime()));
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        name = preferences.getString("name", "");
        promotionPopup.tv1.setOnClickListener(this);
        promotionPopup.tv2.setOnClickListener(this);
        initView();
    }

    private void initUserCarInfo(CarContent carContent) {
        if (carplateNum == null || carplateNum.equals("")) {
            promotionPopup.tv_user_info.setText(name);
        } else {
            promotionPopup.tv_user_info.setText(name + "  " + carplateNum + "(" + carContent.getCarType() + ")");
        }
        promotionPopup.tv_precaution_add.setText(carContent.getReason() + "");
    }

    /**
     * 加载车辆信息
     */
    private void loadCarData() {
        String url = API.BASE_URL + API.URL_CAR_INFOR;
        RequestParams requestParams = new RequestParams();
        requestParams.add("bindId", String.valueOf(bindId));
        if (bindId == 0) {
            return;
        }
        requestParams.add("activityId", String.valueOf(promotions.getId()));
        Log.d(Utils.TAG, "请求url:" + url);
        Log.d(Utils.TAG, "请求参数:" + requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(HalfPriceDetailsActivity.this);
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Log.d(Utils.TAG, "返回的json:" + json);
                Carinfo carinfo = GsonUtils.getInstance().fromJson(json, Carinfo.class);
                if (carinfo.isSuccess()) {
                    if (carinfo.getContent() != null && carinfo.getContent().size() > 0) {
                        carContent = carinfo.getContent().get(0);
                    } else {
                        carContent = new CarContent();
                        carContent.setReason("");
                        carContent.setStartCurrMonth(true);
                        carContent.setCarType("");
                        carContent.setStartTime(0);
                    }
                    initUserCarInfo(carContent);
                    yuyueTime = carContent.getStartTime();
                    String endTime = "";
                    if (isshangbanyue) { //上半月
                        String stringMonth = String.valueOf(startmonth);
                        if (stringMonth.length() < 2) {
                            stringMonth = "0" + stringMonth;
                        }
                        endTime = getYear() + "-" + stringMonth + "-" + 15;
                    } else {
                        endTime = DateUtils.getLastDayOfMonth(getYear(), startmonth);
                    }
                    flag = !carinfo.getContent().get(0).isFlag();
                    reason = carContent.getReason();
                    if (carinfo.getContent().size() == 0 || !carinfo.getContent().get(0).isFlag() || yuyueTime >= dateToStamp(endTime)) {  //false 表示不可以进行活动
                        promotionPopup.tv_buy.setClickable(false);
                        promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom_dark));
                        promotionPopup.tv_precaution_add.setText(carContent.getReason() + "");
//                        if ( yuyueTime >= dateToStamp(endTime)){
//                            promotionPopup.tv_precaution_add.setText("已绑定优先规则，具体问题请联系客服");
//                        }
                    } else {
                        promotionPopup.tv_buy.setClickable(true);
                        promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom));
                        promotionPopup.tv_precaution_add.setText("");
                    }
                } else {
//                    Utils.toast(PromotionDetailsActivity.this, carinfo.getMsg()+"");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(HalfPriceDetailsActivity.this, "请求失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
     点击按钮弹窗变化的view处理
     */
    private void initView() {
        promotionPopup.tv_totalmoney.setText(promotions.getPackagePrice() + "元");
    }

    /**
     * 请求活动内容
     */
    private void loadData() {
        String url = API.BASE_URL + API.URL_ACTIVITY_CONTENT;
        RequestParams requestParams = new RequestParams();
        requestParams.add("activityId", String.valueOf(promotions.getId()));
        Log.d(Utils.TAG, "请求url:" + url);
        Log.d(Utils.TAG, "请求参数:" + requestParams);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Log.d(Utils.TAG, "返回的json:" + json);
                PromotionDetail promotionDetail = GsonUtils.getInstance().fromJson(json, PromotionDetail.class);
                binding.tvContent.setText(promotionDetail.getContent().getRemark());
                loadCarData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(HalfPriceDetailsActivity.this, "请求失败", Toast.LENGTH_LONG).show();
                loadCarData();
            }
        });
    }

    private CarSelectorDialogFragment carSelectorDialogFragment;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_join:
                promotionPopup.showPopupWindow();
                break;
            case R.id.tv_buy: //购买套餐
                payPopup.showPopupWindow();
                break;
            case R.id.re: //车辆选择界面
                Bundle bundle = new Bundle();
                bundle.putBoolean(API.isChongdian, false);
                carSelectorDialogFragment.setArguments(bundle);
                carSelectorDialogFragment.show(getSupportFragmentManager(), "data");
                break;
            case R.id.tv_weixing://微信支付
                buyPackage(0);
                break;
            case R.id.tv_alipay://支付宝支付
                buyPackage(1);
                break;
            case R.id.tv1:
                monthPopup.showPopupWindow();
                break;
            case R.id.tv2:
                monthTypePopup.showPopupWindow();
                break;
            case R.id.tv_month_first:
                startmonth = getMonth();
                initTime();
                promotionPopup.tv1.setText(startmonth + "月");
                monthPopup.dismiss();
                break;
            case R.id.tv_month_next:
                startmonth = getMonth() + 1;
                initTime();
                promotionPopup.tv1.setText(startmonth + "月");
                monthPopup.dismiss();
                break;
            case R.id.tv_month_type_first:
                isshangbanyue = true;
                initTime();
                promotionPopup.tv2.setText("上半月");
                monthTypePopup.dismiss();
                break;
            case R.id.tv_month_type_second:
                isshangbanyue = false;
                initTime();
                promotionPopup.tv2.setText("下半月");
                monthTypePopup.dismiss();
                break;
            default:
                break;
        }
    }

    //购买套餐
    private void buyPackage(int i) {
        /**
         * tradeType	[int]	是	套餐活动调用时值为1；正常充值活动值为0或者没有
         * tradeRemark	[string]	是	carTypeId,carId,carNo,startTime,months	收缩
         * 参数示例：yyyy-MM-dd 1,68,浙123456,2019-01-01,4
         *
         * tradeType=3，tradeRemark=[carTypeId],[carId],[carNo],[startTime]~[endTime],1,[activityId]
         * 车牌号id，车id，车牌号，开始时间~结束时间（yyyy-MM-dd），购买套餐次数（默认1），活动id
         * 和原来里程套餐的格式类型，时间变为 【开始~结束】
         */
        String startTime = "";
        String endTime = "";
        if (isshangbanyue) { //上半月
            startTime = DateUtils.getFirstDayOfMonth(getYear(), startmonth);
            String stringMonth = String.valueOf(startmonth);
            if (stringMonth.length() < 2) {
                stringMonth = "0" + stringMonth;
            }
            endTime = getYear() + "-" + stringMonth + "-" + 15;
        } else {
            endTime = DateUtils.getLastDayOfMonth(getYear(), startmonth);
            String stringMonth = String.valueOf(startmonth);
            if (stringMonth.length() < 2) {
                stringMonth = "0" + stringMonth;
            }
            startTime = getYear() + "-" + stringMonth + "-" + 16;
        }
        String url = API.BASE_URL + API.URL_PAY;
        RequestParams params = new RequestParams();
        params.add("tradeType", "4");
        params.add("tradeRemark", carContent.getCarTypeId() + "," + carContent.getCarId() + "," + carContent.getCarNo() + "," + startTime + "~" + endTime + "," + "1" + "," + promotions.getId());
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        params.add("rechargeAmount", String.valueOf(promotions.getPackagePrice()));
        params.add("type", String.valueOf(i));
        Log.d(Utils.TAG, "url:" + url);
        Log.d(Utils.TAG, "requestParams:" + params);
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
        client.setCookieStore(myCookieStore);
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                Log.d(Utils.TAG, "返回值:" + json);
                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                Integer code = obj.get("code").getAsInt();
                if (code == -1) {
                    ToastUtil.showToast(HalfPriceDetailsActivity.this, obj.get("msg").getAsString());
                    return;
                }
                JsonObject data = obj.get("content").getAsJsonObject();
                Integer type = data.get("type").getAsInt();
                if (type == 1) {
                    String tradeCode = data.get("tradeCode").getAsString();
                    View v = new View(HalfPriceDetailsActivity.this);
                    payV2(v, tradeCode);
                } else if (type == 0) {
                    JsonObject paramsMap = data.get("paramsMap").getAsJsonObject();
                    wxpay(paramsMap);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
            }

        });
    }

    //选择车牌回来请求接口，刷新view
    @Override
    public void click(String plateNum, int bind_id) {
        carplateNum = plateNum;
        bindId = bind_id;
        promotionPopup.tv_carplatenum.setText(carplateNum);
        loadCarData();
    }

    //微信支付
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
                PayTask alipay = new PayTask(HalfPriceDetailsActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.d(Utils.TAG, result.toString());
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
                        message.putExtra("isPackage", true);
                        message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainApp.getInstance().getApplicationContext().startActivity(message);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(HalfPriceDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(HalfPriceDetailsActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(HalfPriceDetailsActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void initTime() {
        String startTime = "";
        String endTime = "";
        if (isshangbanyue) { //上半月
            startTime = DateUtils.getFirstDayOfMonth(getYear(), startmonth);
            String stringMonth = String.valueOf(startmonth);
            if (stringMonth.length() < 2) {
                stringMonth = "0" + stringMonth;
            }
            endTime = getYear() + "-" + stringMonth + "-" + 15;
        } else {
            endTime = DateUtils.getLastDayOfMonth(getYear(), startmonth);
            String stringMonth = String.valueOf(startmonth);
            if (stringMonth.length() < 2) {
                stringMonth = "0" + stringMonth;
            }
            startTime = getYear() + "-" + stringMonth + "-" + 16;
        }
        promotionPopup.tv_time.setText(startTime + "~~" + endTime);
        if (dateToStamp(endTime) > promotions.getEndTime() || yuyueTime >= dateToStamp(endTime) || flag) {
            promotionPopup.tv_buy.setClickable(false);
            promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom_dark));
            if (dateToStamp(endTime) > promotions.getEndTime()) {
                promotionPopup.tv_precaution_add.setText("购买套餐活动要在活动期限内");
            } else if (yuyueTime >= dateToStamp(endTime)) {
                promotionPopup.tv_precaution_add.setText("已绑定预约规则，具体问题请联系客服");
            } else {
                promotionPopup.tv_precaution_add.setText(reason + "");
            }
        } else {
            promotionPopup.tv_buy.setClickable(true);
            promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom));
            promotionPopup.tv_precaution_add.setText("");
        }
    }

    /*
     * 将时间转换为时间戳
     */
    public long dateToStamp(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }
}
