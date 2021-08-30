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
import com.kulun.energynet.databinding.LayoutPromotionDetailsBinding;
import com.kulun.energynet.inter.PromotionCarConfirm;
import com.kulun.energynet.model.AuthResult;
import com.kulun.energynet.model.Carinfo.CarContent;
import com.kulun.energynet.model.Carinfo.Carinfo;
import com.kulun.energynet.model.PayResult;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.promotions.PromotionDetail;
import com.kulun.energynet.model.promotions.Promotions;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.activity.PayActivity;
import com.kulun.energynet.ui.activity.PayResultActivity;
import com.kulun.energynet.ui.basepopup.PayPopup;
import com.kulun.energynet.ui.basepopup.PromotionPopup;
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

import java.util.Map;

/**
 * created by xuedi on 2019/1/18
 */
public class PromotionDetailsActivity extends AppCompatActivity implements View.OnClickListener, PromotionCarConfirm {
    private LayoutPromotionDetailsBinding binding;
    private Promotions promotions;
    private PromotionPopup promotionPopup;
    private PayPopup payPopup;
    private String carplateNum, name;
    private int bindId;
    private int startYear, startMonth;
    private CarContent carContent;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private String activityType;
    private int isStartThisMonth = 0; //点击的radiobutton,0这个月，1下个月
    private int isOnlyStartNextMonth = 0; //得到的月份，0这个月开始，1下个月开始

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_promotion_details);
        promotions = (Promotions) getIntent().getSerializableExtra("promotions");
        activityType = getIntent().getStringExtra("type");
        binding.tvBack.setOnClickListener(this);
        binding.tvJoin.setOnClickListener(this);
        binding.tvType.setText("活动类型:" + activityType);
        binding.tvTime.setText("活动时间:" + DateUtils.stampToYear(promotions.getStartTime()) + "~~" + DateUtils.stampToYear(promotions.getEndTime()));
        binding.tvName.setText("活动名称:" + promotions.getName());
        promotionPopup = new PromotionPopup(PromotionDetailsActivity.this);
        payPopup = new PayPopup(this);
        loadData();
        initPromotionPopup();
        initPayPopup();
        carSelectorDialogFragment = new CarSelectorDialogFragment();
        initTvJoin(activityType);
    }

    private void initPayPopup() {
        payPopup.tv_weixing.setOnClickListener(this);
        payPopup.tv_alipay.setOnClickListener(this);
    }

    //立即参与按钮初始化
    private void initTvJoin(String type) {
        if (type.equals("里程套餐") || type.equals("充值送")){
            binding.tvJoin.setVisibility(View.VISIBLE);
        }else {
            binding.tvJoin.setVisibility(View.GONE);
        }
    }

    /**
     * 弹出的popup设置
     */
    private void initPromotionPopup() {
        promotionPopup.img_add.setOnClickListener(this);
        promotionPopup.img_minus.setOnClickListener(this);
        promotionPopup.re.setOnClickListener(this);
        promotionPopup.tv_buy.setOnClickListener(this);
        promotionPopup.rb_thismonth.setOnClickListener(this);
        promotionPopup.rb_nextmonth.setOnClickListener(this);
        SharedPreferences sharedPreferences = MainApp.getInstance().getSharedPreferences("carplate", Context.MODE_PRIVATE);
        String carplateSave = sharedPreferences.getString("carplate" + User.getInstance().getAccountId(), "");
        bindId = sharedPreferences.getInt("id" + User.getInstance().getAccountId(), 0);
        carplateNum = carplateSave;
        if (carplateSave == null || carplateSave.equals("")){
            carplateSave = "请选择车牌";
        }
        promotionPopup.tv_carplatenum.setText(carplateSave);
        promotionPopup.tv_activity_time.setText(DateUtils.stampToYear(promotions.getStartTime())+"~"+DateUtils.stampToYear(promotions.getEndTime()));
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        name = preferences.getString("name", "");
    }

    private void initUserCarInfo(CarContent carContent){
        if (carplateNum == null || carplateNum.equals("")){
            promotionPopup.tv_user_info.setText(name);
        }else {
            promotionPopup.tv_user_info.setText(name + "  " + carplateNum + "(" + carContent.getCarType() + ")");
        }
    }
    //初始化radiobutton
    private void initRadioBUtton(CarContent carContent) {
        if (carContent.isStartCurrMonth()){
            promotionPopup.rb_thismonth.setEnabled(true);
            promotionPopup.rb_thismonth.setChecked(true);
            promotionPopup.rb_nextmonth.setEnabled(true);
            isOnlyStartNextMonth = 0;
            clickNow(carContent);
        }else {
            promotionPopup.rb_thismonth.setEnabled(false);
            promotionPopup.rb_nextmonth.setEnabled(true);
            promotionPopup.rb_nextmonth.setChecked(true);
            isOnlyStartNextMonth = 1;
            clickNext(carContent);
        }
    }

    /**
     * 加载车辆信息
     */
    private void loadCarData() {
        String url = API.BASE_URL + API.URL_CAR_INFOR;
        RequestParams requestParams = new RequestParams();
        requestParams.add("bindId", String.valueOf(bindId));
        if (bindId == 0){
            return;
        }
        requestParams.add("activityId", String.valueOf(promotions.getId()));
        Log.d(Utils.TAG, "请求url:" + url);
        Log.d(Utils.TAG, "请求参数:" + requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(PromotionDetailsActivity.this);
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
                    }
                    initRadioBUtton(carContent);
                    initUserCarInfo(carContent);
                    if (carinfo.getContent().size() == 0 || !carinfo.getContent().get(0).isFlag()) {  //false 表示不可以进行活动
                        promotionPopup.tv_buy.setClickable(false);
                        promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom_dark));
                        promotionPopup.tv_precaution_add.setText(carContent.getReason() + "");
                    } else {
                        promotionPopup.tv_buy.setClickable(true);
                        promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom));
                        promotionPopup.tv_precaution_add.setText("");
                    }
                }else {
//                    Utils.toast(PromotionDetailsActivity.this, carinfo.getMsg()+"");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(PromotionDetailsActivity.this, "请求失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
     点击按钮弹窗变化的view处理
     */
    private int monthNum = 1;
    private void initView(int year, int month) {
        promotionPopup.tv_monthnum.setText(monthNum + "个月");
        if (carContent != null && carContent.getPackagePriceCurrMonth() != 0 && carContent.isStartCurrMonth()){
            if (monthNum == 1){
                promotionPopup.tv_totalmoney.setText(carContent.getPackagePriceCurrMonth() + "元x" + monthNum + "个月");
                promotionPopup.tv_money.setText("￥" + carContent.getPackagePriceCurrMonth() * monthNum);
            }else {
                int monthDay = monthNum - 1;
                promotionPopup.tv_totalmoney.setText("("+carContent.getPackagePriceCurrMonth() + "x1) + ("+promotions.getPackagePrice()+"x"+monthDay+")=");
                double first = carContent.getPackagePriceCurrMonth() * 1;
                double second = promotions.getPackagePrice()*monthDay;
                double resoult = first + second;
                promotionPopup.tv_money.setText("￥" + resoult);
            }
            promotionPopup.tv_time.setText(carContent.getPackageDayCurrMonth()+ "~~" + getNextYearAndMonth(year, month, monthNum));
        }else {
            promotionPopup.tv_totalmoney.setText(promotions.getPackagePrice() + "元x" + monthNum + "个月");
            promotionPopup.tv_money.setText("￥" + promotions.getPackagePrice() * monthNum);
            promotionPopup.tv_time.setText(DateUtils.getFirstDayOfMonth(year, month)+ "~~" + getNextYearAndMonth(year, month, monthNum));
        }
        promotionPopup.tv_precaution_add.setText(carContent.getReason() + "");
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
                Toast.makeText(PromotionDetailsActivity.this, "请求失败", Toast.LENGTH_LONG).show();
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
                if (activityType.equals("里程套餐")){
                    promotionPopup.showPopupWindow();
                }else {
                    Intent intent = new Intent(PromotionDetailsActivity.this, PayActivity.class);
                    startActivity(intent);
                }
                break;
            /**
             * popup弹窗的按钮响应
             */
            case R.id.img_add: //增加月份
                if (monthNum + isStartThisMonth - isOnlyStartNextMonth >= carContent.getMaxMonths()) {
                    Toast.makeText(PromotionDetailsActivity.this, "购买周期不能超过活动限制", Toast.LENGTH_LONG).show();
                    return;
                }
                if (monthNum >= promotions.getLimitMaxMonths()){
                    Toast.makeText(PromotionDetailsActivity.this, "购买周期不能超过"+promotions.getLimitMaxMonths()+"个月", Toast.LENGTH_LONG).show();
                    return;
                }
                monthNum = monthNum + 1;
                initView(startYear, startMonth);
                break;
            case R.id.img_minus: //减小月份
                if (monthNum == 1) {
                    Toast.makeText(PromotionDetailsActivity.this, "购买周期不能再减小了", Toast.LENGTH_LONG).show();
                    return;
                }
                monthNum = monthNum - 1;
                initView(startYear, startMonth);
                break;
            case R.id.tv_buy: //购买套餐
                payPopup.showPopupWindow();
//                buyPackage();
                break;
            case R.id.rb_thismonth:
                clickNow(carContent);
                break;
            case R.id.rb_nextmonth:
                clickNext(carContent);
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
            default:
                break;
        }
    }

    //点击这个月的button
    private void clickNow(CarContent carContent){
        int year = DateUtils.stampToYears(System.currentTimeMillis());
        int month = DateUtils.stampToMonth(System.currentTimeMillis());
        startYear = year;
        startMonth = month;
        monthNum = 1;
        initView(startYear, startMonth);
        isStartThisMonth = 0;
        if (!carContent.isFlag()) {  //false 表示不可以进行活动
            promotionPopup.tv_buy.setClickable(false);
            promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom_dark));
            promotionPopup.tv_precaution_add.setText(carContent.getReason() + "");
        } else {
            promotionPopup.tv_buy.setClickable(true);
            promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom));
            promotionPopup.tv_precaution_add.setText(carContent.getReason()+"");
        }
        promotionPopup.tv_warning.setVisibility(View.GONE);
    }

    //点击下个月的radiobutton
    private void clickNext(CarContent carContent){
        monthNum = 1;
        int year_next = DateUtils.stampToYears(System.currentTimeMillis());
        int month_next = DateUtils.stampToMonth(System.currentTimeMillis());
        if (month_next > 11) {
            month_next = 1;
            year_next = year_next + 1;
        } else {
            month_next = month_next + 1;
        }
        startYear = year_next;
        startMonth = month_next;
        initView(startYear, startMonth);
        isStartThisMonth = 1;
        if (!carContent.isFlag()) {  //false 表示不可以进行活动
            promotionPopup.tv_buy.setClickable(false);
            promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom_dark));
            promotionPopup.tv_warning.setVisibility(View.GONE);
        } else {
            if (monthNum+1-isOnlyStartNextMonth > carContent.getMaxMonths()) {
                promotionPopup.tv_warning.setText("提示:购买周期超过了活动时间");
                promotionPopup.tv_warning.setVisibility(View.VISIBLE);
                promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom_dark));
                promotionPopup.tv_buy.setClickable(false);
            }else {
                promotionPopup.tv_buy.setClickable(true);
                promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom));
            }
        }
    }

    //购买套餐
    private void buyPackage(int i) {
        /**
         * tradeType	[int]	是	套餐活动调用时值为1；正常充值活动值为0或者没有
         * tradeRemark	[string]	是	carTypeId,carId,carNo,startTime,months	收缩
         * 参数示例：yyyy-MM-dd 1,68,浙123456,2019-01-01,4
         */
        String url = API.BASE_URL + API.URL_PAY;
        RequestParams params = new RequestParams();
        params.add("tradeType", "1");
        String stringMonth = String.valueOf(startMonth);
        if (stringMonth.length() < 2){
            stringMonth = "0"+stringMonth;
        }
        params.add("accountId",String.valueOf(User.getInstance().getAccountId()));
        if (carContent != null && carContent.getPackagePriceCurrMonth() != 0 && carContent.isStartCurrMonth()){
            if (monthNum == 1){
                params.add("rechargeAmount", String.valueOf(carContent.getPackagePriceCurrMonth() * monthNum));
            }else {
                int monthDay = monthNum - 1;
                double first = carContent.getPackagePriceCurrMonth() * 1;
                double second = promotions.getPackagePrice()*monthDay;
                double resoult = first + second;
                params.add("rechargeAmount", String.valueOf(resoult));
            }
            params.add("tradeRemark", carContent.getCarTypeId() + "," + carContent.getCarId() + "," + carContent.getCarNo() + "," + carContent.getPackageDayCurrMonth() + "," + monthNum + "," + promotions.getId());
        }else {
            params.add("rechargeAmount", String.valueOf(promotions.getPackagePrice() * monthNum));
            params.add("tradeRemark", carContent.getCarTypeId() + "," + carContent.getCarId() + "," + carContent.getCarNo() + "," + startYear+"-"+stringMonth+"-01" + "," + monthNum + "," + promotions.getId());
        }
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
                    ToastUtil.showToast(PromotionDetailsActivity.this, obj.get("msg").getAsString());
                    return;
                }
                JsonObject data = obj.get("content").getAsJsonObject();
                Integer type = data.get("type").getAsInt();
                if (type == 1) {
                    String tradeCode = data.get("tradeCode").getAsString();
                    View v = new View(PromotionDetailsActivity.this);
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

    private String getNextYearAndMonth(int year, int month, int monthnum) {
        int yearnext = (month + monthnum - 1) / 12;
        int monthnext = (month + monthnum - 1) % 12;
        double a = (month + monthnum - 1) / 12.0;
        if (a <= 1.0) {
//            return year + "年" + (month + monthnum - 1) + "月底";
            return DateUtils.getLastDayOfMonth(year, month + monthnum - 1);
        } else {
            if (monthnext == 0) {
//                return year + yearnext + "年" + "12月底";
                return DateUtils.getLastDayOfMonth(year+yearnext, 12);
            }
            return DateUtils.getLastDayOfMonth(year+yearnext, monthnext);
        }
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
                PayTask alipay = new PayTask(PromotionDetailsActivity.this);
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
                        Toast.makeText(PromotionDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PromotionDetailsActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PromotionDetailsActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
