package com.botann.driverclient.ui.activity.promotions;

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
import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.databinding.LayoutPriceDetailsBinding;
import com.botann.driverclient.inter.PromotionCarConfirm;
import com.botann.driverclient.model.AuthResult;
import com.botann.driverclient.model.Carinfo.CarContent;
import com.botann.driverclient.model.Carinfo.Carinfo;
import com.botann.driverclient.model.PayResult;
import com.botann.driverclient.model.User;
import com.botann.driverclient.model.promotions.PromotionDetail;
import com.botann.driverclient.model.promotions.Promotions;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.PayActivity;
import com.botann.driverclient.ui.activity.PayResultActivity;
import com.botann.driverclient.ui.basepopup.PayPopup;
import com.botann.driverclient.ui.basepopup.PrciePopup;
import com.botann.driverclient.ui.fragment.CarSelectorDialogFragment;
import com.botann.driverclient.utils.Constants;
import com.botann.driverclient.utils.DateUtils;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.ToastUtil;
import com.botann.driverclient.utils.Utils;
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
import java.util.Date;
import java.util.Map;

public class PriceDetailsAcrivity extends AppCompatActivity implements View.OnClickListener, PromotionCarConfirm {
    private LayoutPriceDetailsBinding binding;
    private Promotions promotions;
    private PrciePopup promotionPopup;
    private PayPopup payPopup;
    private String carplateNum, name;
    private int bindId;
    private int startYear, startMonth;
    private CarContent carContent;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private String activityType;
    private int isStartThisMonth = 0; //?????????radiobutton,0????????????1?????????
    private int isOnlyStartNextMonth = 0; //??????????????????0??????????????????1???????????????

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_price_details);
        promotions = (Promotions) getIntent().getSerializableExtra("promotions");
        activityType = getIntent().getStringExtra("type");
        binding.tvBack.setOnClickListener(this);
        binding.tvJoin.setOnClickListener(this);
        binding.tvType.setText("????????????:" + activityType);
        binding.tvTime.setText("????????????:" + DateUtils.stampToYear(promotions.getStartTime()) + "~~" + DateUtils.stampToYear(promotions.getEndTime()));
        binding.tvName.setText("????????????:" + promotions.getName());
        promotionPopup = new PrciePopup(PriceDetailsAcrivity.this);
        payPopup = new PayPopup(this);
        loadData();
        initPromotionPopup();
        initPayPopup();
        carSelectorDialogFragment = new CarSelectorDialogFragment();
    }

    private void initPayPopup() {
        payPopup.tv_weixing.setOnClickListener(this);
        payPopup.tv_alipay.setOnClickListener(this);
    }

    /**
     * ?????????popup??????
     */
    private void initPromotionPopup() {
        promotionPopup.re.setOnClickListener(this);
        promotionPopup.tv_buy.setOnClickListener(this);
        promotionPopup.rb_thismonth.setOnClickListener(this);
        promotionPopup.rb_nextmonth.setOnClickListener(this);
        promotionPopup.tv_precaution_add.setText("");
        SharedPreferences sharedPreferences = MainApp.getInstance().getSharedPreferences("carplate", Context.MODE_PRIVATE);
        String carplateSave = sharedPreferences.getString("carplate" + User.getInstance().getAccountId(), "");
        bindId = sharedPreferences.getInt("id" + User.getInstance().getAccountId(), 0);
        carplateNum = carplateSave;
        if (carplateSave == null || carplateSave.equals("")) {
            carplateSave = "???????????????";
        }
        promotionPopup.tv_carplatenum.setText(carplateSave);
        promotionPopup.tv_activity_time.setText(DateUtils.stampToYear(promotions.getStartTime()) + "~" + DateUtils.stampToYear(promotions.getEndTime()));
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        name = preferences.getString("name", "");
    }

    private void initUserCarInfo(CarContent carContent) {
        if (carplateNum == null || carplateNum.equals("")) {
            promotionPopup.tv_user_info.setText(name);
        } else {
            promotionPopup.tv_user_info.setText(name + "  " + carplateNum + "(" + carContent.getCarType() + ")");
        }
    }

    //?????????radiobutton
    private void initRadioBUtton(CarContent carContent) {
        if (carContent.isStartCurrMonth()) {
            promotionPopup.rb_thismonth.setEnabled(true);
            promotionPopup.rb_thismonth.setChecked(true);
            promotionPopup.rb_nextmonth.setEnabled(true);
            isOnlyStartNextMonth = 0;
            clickNow(carContent);
        } else {
            promotionPopup.rb_thismonth.setEnabled(false);
            promotionPopup.rb_nextmonth.setEnabled(true);
            promotionPopup.rb_nextmonth.setChecked(true);
            isOnlyStartNextMonth = 1;
            clickNext(carContent);
        }
    }

    /**
     * ??????????????????
     */
    private long yuyueTime;
    private void loadCarData() {
        String url = API.BASE_URL + API.URL_CAR_INFOR;
        RequestParams requestParams = new RequestParams();
        requestParams.add("bindId", String.valueOf(bindId));
        requestParams.add("activityId", String.valueOf(promotions.getId()));
        if (bindId == 0){
            return;
        }
        Log.d(Utils.TAG, "??????url:" + url);
        Log.d(Utils.TAG, "????????????:" + requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(PriceDetailsAcrivity.this);
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Log.d(Utils.TAG, "?????????json:" + json);
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
                    yuyueTime = carContent.getStartTime();
                    initRadioBUtton(carContent);
                    initUserCarInfo(carContent);
                    if (carinfo.getContent().size() == 0 || !carinfo.getContent().get(0).isFlag() || yuyueTime >= dateToStamp(DateUtils.getLastDayOfMonth(startYear, startMonth))) {  //false ???????????????????????????
                        promotionPopup.tv_buy.setClickable(false);
                        promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom_dark));
                        promotionPopup.tv_precaution_add.setText(carContent.getReason() + "");
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
                Toast.makeText(PriceDetailsAcrivity.this, "????????????", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
     ???????????????????????????view??????
     */
    private void initView(int year, int month, CarContent carContent) {
        promotionPopup.tv_time.setText(DateUtils.getFirstDayOfMonth(year, month) + "~~" + getNextYearAndMonth(year, month, 1));
        promotionPopup.tv_totalmoney.setText(promotions.getPackagePrice() + "???");
        if (carContent != null) {
            promotionPopup.tv_precaution_add.setText(carContent.getReason() + "");
        }
    }

    /**
     * ??????????????????
     */
    private void loadData() {
        String url = API.BASE_URL + API.URL_ACTIVITY_CONTENT;
        RequestParams requestParams = new RequestParams();
        requestParams.add("activityId", String.valueOf(promotions.getId()));
        Log.d(Utils.TAG, "??????url:" + url);
        Log.d(Utils.TAG, "????????????:" + requestParams);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Log.d(Utils.TAG, "?????????json:" + json);
                PromotionDetail promotionDetail = GsonUtils.getInstance().fromJson(json, PromotionDetail.class);
                binding.tvContent.setText(promotionDetail.getContent().getRemark());
                loadCarData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(PriceDetailsAcrivity.this, "????????????", Toast.LENGTH_LONG).show();
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
            case R.id.tv_buy: //????????????
//                buyPackage();
                payPopup.showPopupWindow();
                break;
            case R.id.rb_thismonth:
                clickNow(carContent);
                break;
            case R.id.rb_nextmonth:
                clickNext(carContent);
                break;
            case R.id.re: //??????????????????
                Bundle bundle = new Bundle();
                bundle.putBoolean(API.isChongdian, false);
                carSelectorDialogFragment.setArguments(bundle);
                carSelectorDialogFragment.show(getSupportFragmentManager(), "data");
                break;
            case R.id.tv_weixing://????????????
                buyPackage(0);
                break;
            case R.id.tv_alipay://???????????????
                buyPackage(1);
                break;
            default:
                break;
        }
    }

    //??????????????????button
    private void clickNow(CarContent carContent) {
        int year = DateUtils.stampToYears(System.currentTimeMillis());
        int month = DateUtils.stampToMonth(System.currentTimeMillis());
        startYear = year;
        startMonth = month;
        isStartThisMonth = 0;
        if (carContent == null){
            return;
        }
        initView(startYear, startMonth, carContent);
        if (!carContent.isFlag()) {  //false ???????????????????????????
            promotionPopup.tv_buy.setClickable(false);
            promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom_dark));
            promotionPopup.tv_precaution_add.setText(carContent.getReason() + "");
        } else {
            promotionPopup.tv_buy.setClickable(true);
            promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom));
            promotionPopup.tv_precaution_add.setText(carContent.getReason() + "");
        }
    }

    //??????????????????radiobutton
    private void clickNext(CarContent carContent) {
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
        isStartThisMonth = 1;
        if (carContent == null){
            return;
        }
        initView(startYear, startMonth, carContent);
        if (!carContent.isFlag()) {  //false ???????????????????????????
            promotionPopup.tv_buy.setClickable(false);
            promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom_dark));
            promotionPopup.tv_precaution_add.setText("?????????????????????");
        } else {
            if (2 - isOnlyStartNextMonth > 1) {
                promotionPopup.tv_precaution_add.setText("??????:?????????????????????????????????");
                promotionPopup.tv_precaution_add.setVisibility(View.VISIBLE);
                promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom_dark));
                promotionPopup.tv_buy.setClickable(false);
            } else {
                promotionPopup.tv_buy.setClickable(true);
                promotionPopup.tv_buy.setBackground(getResources().getDrawable(R.drawable.promotion_bottom));
            }
        }
    }

    //????????????
    private void buyPackage(int i) {
        /**
         * tradeType	[int]	???	???????????????????????????1???????????????????????????0????????????
         * tradeRemark	[string]	???	carTypeId,carId,carNo,startTime,months	??????
         * ???????????????yyyy-MM-dd 1,68,???123456,2019-01-01,4
         */
        String url = API.BASE_URL + API.URL_PAY;
        RequestParams params = new RequestParams();
        /**
         *
         */
        params.add("tradeType", "4");
        String stringMonth = String.valueOf(startMonth);
        if (stringMonth.length() < 2) {
            stringMonth = "0" + stringMonth;
        }
        params.add("tradeRemark", carContent.getCarTypeId() + "," + carContent.getCarId() + "," + carContent.getCarNo() + "," + startYear + "-" + stringMonth + "-01~"+DateUtils.getLastDayOfMonth(startYear, startMonth) + "," + "1" + "," + promotions.getId());
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
                Log.d(Utils.TAG, "?????????:" + json);
                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                Integer code = obj.get("code").getAsInt();
                if (code == -1) {
                    ToastUtil.showToast(PriceDetailsAcrivity.this, obj.get("msg").getAsString());
                    return;
                }
                JsonObject data = obj.get("content").getAsJsonObject();
                Integer type = data.get("type").getAsInt();
                if (type == 1) {
                    String tradeCode = data.get("tradeCode").getAsString();
                    View v = new View(PriceDetailsAcrivity.this);
                    payV2(v, tradeCode);
                } else if (type == 0) {
                    JsonObject paramsMap = data.get("paramsMap").getAsJsonObject();
                    wxpay(paramsMap);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                Toast.makeText(MainApp.getInstance().getApplicationContext(), "???????????????????????????", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private String getNextYearAndMonth(int year, int month, int monthnum) {
        int yearnext = (month + monthnum - 1) / 12;
        int monthnext = (month + monthnum - 1) % 12;
        double a = (month + monthnum - 1) / 12.0;
        if (a <= 1.0) {
//            return year + "???" + (month + monthnum - 1) + "??????";
            return DateUtils.getLastDayOfMonth(year, month + monthnum - 1);
        } else {
            if (monthnext == 0) {
//                return year + yearnext + "???" + "12??????";
                return DateUtils.getLastDayOfMonth(year + yearnext, 12);
            }
            return DateUtils.getLastDayOfMonth(year + yearnext, monthnext);
        }
    }

    //???????????????????????????????????????view
    @Override
    public void click(String plateNum, int bind_id) {
        carplateNum = plateNum;
        bindId = bind_id;
        promotionPopup.tv_carplatenum.setText(carplateNum);
        loadCarData();
    }

    //????????????
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
                PayTask alipay = new PayTask(PriceDetailsAcrivity.this);
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
                     ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                     */
                    String resultInfo = payResult.getResult();// ?????????????????????????????????
                    String resultStatus = payResult.getResultStatus();
                    // ??????resultStatus ???9000?????????????????????
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // ??????????????????????????????????????????????????????????????????????????????
                        //Toast.makeText(PayActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                        Intent message = new Intent(MainApp.getInstance().getApplicationContext(), PayResultActivity.class);
                        message.putExtra("isPackage", true);
                        message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainApp.getInstance().getApplicationContext().startActivity(message);
                        finish();
                    } else {
                        // ???????????????????????????????????????????????????????????????????????????
                        Toast.makeText(PriceDetailsAcrivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    // ??????resultStatus ??????9000??????result_code
                    // ??????200?????????????????????????????????????????????????????????????????????????????????
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // ??????alipay_open_id???????????????????????????extern_token ???value
                        // ??????????????????????????????????????????
                        Toast.makeText(PriceDetailsAcrivity.this,
                                "????????????\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // ?????????????????????????????????
                        Toast.makeText(PriceDetailsAcrivity.this,
                                "????????????" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /*
     * ???????????????????????????
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

