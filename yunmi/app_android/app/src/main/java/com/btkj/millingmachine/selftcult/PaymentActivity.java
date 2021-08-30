package com.btkj.millingmachine.selftcult;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityPaymentBinding;
import com.btkj.millingmachine.databinding.DialogPaywayBinding;
import com.btkj.millingmachine.errorview.ConnectErrorActivity;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MainActivity;
import com.btkj.millingmachine.homepage.MyApplication;
import com.btkj.millingmachine.model.BaseObject;
import com.btkj.millingmachine.model.aliwxpay.AliWxPayModel;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.model.vippay.VipPayModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.SoundPlayUtils;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.QRCodeUtil;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

import Crt603rxDrv.Crt603rx;

/**
 * created by xuedi on 2019/4/29
 * 4中支付方式
 */
public class PaymentActivity extends BaseActivity implements View.OnClickListener, TimeFinishInterface {
    private ActivityPaymentBinding binding;
    private DialogPaywayBinding dialogbinding;
    private AlertDialog dialog;
    private int weight;
    private Handler handler = new Handler();
    private Crt603rx mCrt603rx = new Crt603rx();
    private long strlog;
    private int iRet = 0;
    // iMode  寻卡模式 0 寻所有卡， 1 只寻未休眠卡
    private int iMode = 0;
    private int[] iOutLen = new int[2];
    private byte[] byOutUid = new byte[128];
    private boolean isPhone = true;
    private double price;
    private String money, serialNo;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getCarUid();
        }
    };
    private TimeCount timeCount;
    private int xiagusudu;
    //下谷速度
    private byte[] nianmistart = new byte[4];

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        weight = getIntent().getIntExtra("weight", 0);
        price = getIntent().getDoubleExtra("price", 0f);
        initCarUid();
        getCarUid();
        binding.includeBack.imgBack.setOnClickListener(this);
        binding.imgPhone.setOnClickListener(this);
        binding.imgIconPhone.setOnClickListener(this);
        initDialog();
        loadQrcode(3);
        loadQrcode(2);
        SoundPlayUtils.play(2);
        xiagusudu = Integer.parseInt((String) SharePref.get(this, API.xiagusudu1, ""));
        //23C00130
        nianmistart[0] = 0x23;
        nianmistart[1] = (byte) 0xC0;
        nianmistart[2] = 0x01;
        nianmistart[3] = (byte) (xiagusudu & 0xff);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(60000, 1000, binding.includeHeader.tvTime, this);
        timeCount.start();
    }

    //加载支付宝二维码
    private void loadQrcode(final int type) {
        /**
         * mode[short]	是	购米模式，卡片购米0，电话购米1，微信支付购米2，支付宝支付购米3
         * deviceId[int]	是	设备编号
         * weight[int]	是	购米重量，克
         * amount[int]	是	计算金额，分
         * icCardChipId[string]		mode=0，必填
         * phone[string]		mode=1，必填
         * code[string]		mode=1，.必填
         */
        String url = API.BASE_URL + API.PAY;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("mode", String.valueOf(type));
        map.put("deviceId", String.valueOf(SharePref.get(this, "id", 0)));
        map.put("weight", String.valueOf(weight));
        //TODO
        map.put("amount", String.valueOf((int)(weight/10*price)));
//        map.put("amount", String.valueOf(1));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        Utils.log(url + "\n" + requestParams);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(json);
                AliWxPayModel aliWxPayModel = GsonUtils.getInstance().fromJson(json, AliWxPayModel.class);
                if (aliWxPayModel.isSuccess()) {
                    refreshQRCode(aliWxPayModel.getData().getBody(), type, aliWxPayModel.getData().getSerialNo());
                } else {
                    Utils.toast(PaymentActivity.this, aliWxPayModel.getError());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.toast(PaymentActivity.this, "网络请求异常");
            }
        });
    }

    private void initCarUid() {
        // 天线状态。 0 关闭， 1 开启
        int bWire = 0;
        //自动寻卡。 0 关闭， 1 开启
        int bAutoFindCard = 1;
        mCrt603rx.SetWorkMode(bWire, bAutoFindCard);
        mCrt603rx.OpenDevice(((String) SharePref.get(this, "duka", "")).toCharArray(), 19200);
    }

    private void getCarUid() {
        iRet = mCrt603rx.FindCard(iMode, iOutLen, byOutUid);
        if (iRet != 0) {
            handler.postDelayed(runnable, 2000);
            Utils.log("payment读卡循环");
        } else {
            String ss = "";
            for (int i = 0; i < iOutLen[0]; i++) {
                ss += String.format("%02x", byOutUid[i]);
            }
            String s01 = ss.substring(6, 8);
            String s02 = ss.substring(4, 6);
            String s03 = ss.substring(2, 4);
            String s04 = ss.substring(0, 2);
            String carnum = s01 + s02 + s03 + s04;
            strlog = Long.parseLong(carnum, 16);
            cardPayRequest(strlog);
        }
    }

    //刷卡消费
    private void cardPayRequest(long strlog) {
        String url = API.BASE_URL + API.PAY;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("mode", "0");
        map.put("deviceId", String.valueOf(SharePref.get(this, "id", 0)));
        map.put("weight", String.valueOf(weight));
        map.put("amount", String.valueOf((int)(weight/10*price)));
//        map.put("amount", String.valueOf(1));
        map.put("icCardChipId", String.valueOf(strlog));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        Utils.log(url + "\n" + requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                VipPayModel vipPayModel = GsonUtils.getInstance().fromJson(json, VipPayModel.class);
                Utils.log(json);
                if (vipPayModel.isSuccess()) {
                    topayResult(vipPayModel.getData().getAmount(), vipPayModel.getData().getSerialNo());
                } else {
                    Utils.toast(PaymentActivity.this, vipPayModel.getError());
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
        /**
         * mode[short]	是	购米模式，卡片购米0，电话购米1，微信支付购米2，支付宝支付购米3
         * deviceId[int]	是	设备编号
         * weight[int]	是	购米重量，克
         * amount复制[int]	是	计算金额，分
         * icCardChipId[string]		mode=0，必填
         */
    }

    private Runnable alipayrunnable = new Runnable() {
        @Override
        public void run() {
            cycleAliPayRequest(serialAli);
        }
    };
    private Runnable wxpayrunnable = new Runnable() {
        @Override
        public void run() {
            cycleWxiPayRequest(serialWxi);
        }
    };

    private void refreshQRCode(String str, int type, String serialNo) {
        if (str == null || str.isEmpty()) {
            Toast.makeText(getApplicationContext(), "二维码内容为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap image = QRCodeUtil.qrCodeImageWithStr(str);
        if (image != null) {
            switch (type) {
                case 3:
                    binding.imgAli.setImageBitmap(image);
                    cycleAliPayRequest(serialNo);
                    break;
                case 2:
                    binding.imgWx.setImageBitmap(image);
                    cycleWxiPayRequest(serialNo);
                    break;
                default:
                    break;
            }
        }
    }

    private String serialAli;
    private String serialWxi;

    //微信订单查询
    private void cycleWxiPayRequest(final String serialNo) {
        String url = API.BASE_URL + API.SERINO_REQUEST;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        /**
         * type[short]	是	0消费，1充值
         * deviceId[int]	是	设备id
         * serialNo[string]	是	充值或消费流水号
         */
        map.put("type", "0");
        map.put("deviceId", String.valueOf(SharePref.get(PaymentActivity.this, "id", 0)));
        map.put("serialNo", serialNo);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        Utils.logserial(url + "\n" + requestParams);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                VipPayModel vipPayModel = GsonUtils.getInstance().fromJson(json, VipPayModel.class);
                Utils.logserial(json + "微信订单查询serialNoOut:" + serialNo);
                if (vipPayModel.isSuccess()) {
                    topayResult(vipPayModel.getData().getAmount(), vipPayModel.getData().getSerialNo());
                } else {
                    serialWxi = serialNo;
                    handler.postDelayed(wxpayrunnable, 2000);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    //阿里订单查询
    private void cycleAliPayRequest(final String serialNo) {
        String url = API.BASE_URL + API.SERINO_REQUEST;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        /**
         * type[short]	是	0消费，1充值
         * deviceId[int]	是	设备id
         * serialNo[string]	是	充值或消费流水号
         */
        map.put("type", "0");
        map.put("deviceId", String.valueOf(SharePref.get(PaymentActivity.this, "id", 0)));
        map.put("serialNo", serialNo);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        Utils.logserial(url + "\n" + requestParams);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                VipPayModel vipPayModel = GsonUtils.getInstance().fromJson(json, VipPayModel.class);
                Utils.logserial(json + "阿里订单查询serialNoOut:" + serialNo);
                if (vipPayModel.isSuccess()) {
                    topayResult(vipPayModel.getData().getAmount(), vipPayModel.getData().getSerialNo());
                } else {
                    serialAli = serialNo;
                    handler.postDelayed(alipayrunnable, 2000);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    private void topayResult(String money, String serialNo) {
        this.money = money;
        this.serialNo = serialNo;
        startcult();
        toResult();
    }

    private void toResult() {
        Intent intent = new Intent(this, PayResultActivity.class);
        intent.putExtra("money", money);
        intent.putExtra("weight", weight);
        intent.putExtra("serialNo", serialNo);
        intent.putExtra("nianmi", nianmistart);
        startActivity(intent);
    }

    private void startcult() {
        MyApplication.getInstance().sendShichaQidong(nianmistart);
//        Utils.toast(PaymentActivity.this, "默认碾米速度" + xiagusudu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeCount.cancel();
        clearHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
        clearHandler();
    }

    private void clearHandler() {
        handler.removeCallbacksAndMessages(null);
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_payway, null);
        dialogbinding = DataBindingUtil.bind(view);
        dialogbinding.img0.setOnClickListener(this);
        dialogbinding.img1.setOnClickListener(this);
        dialogbinding.img2.setOnClickListener(this);
        dialogbinding.img3.setOnClickListener(this);
        dialogbinding.img4.setOnClickListener(this);
        dialogbinding.img5.setOnClickListener(this);
        dialogbinding.img6.setOnClickListener(this);
        dialogbinding.img7.setOnClickListener(this);
        dialogbinding.img8.setOnClickListener(this);
        dialogbinding.img9.setOnClickListener(this);
        dialogbinding.imgClear.setOnClickListener(this);
        dialogbinding.imgConfirm.setOnClickListener(this);
        dialogbinding.imgPhoneClose.setOnClickListener(this);
        dialogbinding.tvGetsms.setOnClickListener(this);
        dialogbinding.imgSmsClose.setOnClickListener(this);
        dialogbinding.tvVerificat.setOnClickListener(this);
        dialogbinding.tvPhone.setOnClickListener(this);
        builder.setView(view);
        dialog = builder.create();
    }

    private String phone = "";
    private String verification_code = "";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_phone:
            case R.id.img_icon_phone:
                dialog.show();
                timeCount.cancel();
                timeCount = new TimeCount(60000, 1000, binding.includeHeader.tvTime, this);
                timeCount.start();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.img0:
                selectInput("0");
                break;
            case R.id.img1:
                selectInput("1");
                break;
            case R.id.img2:
                selectInput("2");
                break;
            case R.id.img3:
                selectInput("3");
                break;
            case R.id.img4:
                selectInput("4");
                break;
            case R.id.img5:
                selectInput("5");
                break;
            case R.id.img6:
                selectInput("6");
                break;
            case R.id.img7:
                selectInput("7");
                break;
            case R.id.img8:
                selectInput("8");
                break;
            case R.id.img9:
                selectInput("9");
                break;
            case R.id.img_clear:
                if (isPhone) {
                    phone = "";
                    dialogbinding.tvPhone.setText(phone);
                } else {
                    verification_code = "";
                    dialogbinding.tvVerificat.setText(verification_code);
                }
                break;
            case R.id.img_confirm:
                phonePayRequest();
                break;
            case R.id.tv_getsms:
                getSms();
                break;
            case R.id.img_sms_close:
                if (!verification_code.isEmpty()) {
                    verification_code = verification_code.substring(0, verification_code.length() - 1);
                    dialogbinding.tvVerificat.setText(verification_code);
                }
                break;
            case R.id.img_phone_close:
                if (!phone.isEmpty()) {
                    phone = phone.substring(0, phone.length() - 1);
                    dialogbinding.tvPhone.setText(phone);
                }
                break;
            case R.id.tv_phone:
                dialogbinding.tvPhone.setBackground(getResources().getDrawable(R.drawable.dialog_press_back));
                dialogbinding.tvVerificat.setBackground(getResources().getDrawable(R.drawable.tv_back));
                isPhone = true;
                break;
            case R.id.tv_verificat:
                dialogbinding.tvPhone.setBackground(getResources().getDrawable(R.drawable.tv_back));
                dialogbinding.tvVerificat.setBackground(getResources().getDrawable(R.drawable.dialog_press_back));
                isPhone = false;
                break;
            default:
                break;
        }
    }

    //手机号码购米
    private void phonePayRequest() {
        /**
         * mode[short]	是	购米模式，卡片购米0，电话购米1，微信支付购米2，支付宝支付购米3
         * deviceId[int]	是	设备编号
         * weight[int]	是	购米重量，克
         * amount[int]	是	计算金额，分
         * icCardChipId[string]		mode=0，必填
         * phone[string]		mode=1，必填
         * code[string]		mode=1，.必填
         */
        if (phone.length() == 0) {
            Utils.toast(this, "请输入手机号");
            return;
        }
        if (verification_code.length() == 0) {
            Utils.toast(this, "验证码不能为空");
            return;
        }
        String url = API.BASE_URL + API.PAY;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("mode", "1");
        map.put("deviceId", String.valueOf(SharePref.get(this, "id", 0)));
        map.put("weight", String.valueOf(weight));
        map.put("amount", String.valueOf((int) (weight/10*price)));
//        map.put("amount", String.valueOf(1));
        map.put("phone", phone);
        map.put("code", verification_code);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        Utils.log(url + "\n" + requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                VipPayModel vipPayModel = GsonUtils.getInstance().fromJson(json, VipPayModel.class);
                Utils.log(json);
                if (vipPayModel.isSuccess()) {
                    topayResult(vipPayModel.getData().getAmount(), vipPayModel.getData().getSerialNo());
                } else {
                    Utils.toast(PaymentActivity.this, vipPayModel.getError());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.toast(PaymentActivity.this, "网络请求失败");
            }
        });
    }

    //获取短信
    private void getSms() {
        if (phone.isEmpty()) {
            Utils.toast(PaymentActivity.this, "电话号码不能为空");
            return;
        }
        String url = API.BASE_URL + API.GET_SMS;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(SharePref.get(PaymentActivity.this, "id", 0)));
        map.put("phone", phone);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
                Utils.toast(PaymentActivity.this, baseObject.getError());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.toast(PaymentActivity.this, "网络连接失败");
            }
        });
    }

    private void selectInput(String value) {
        if (isPhone) {
            if (phone.length() < 12) {
                phone = phone + value;
                dialogbinding.tvPhone.setText(phone);
            } else {
                Utils.toast(this, API.phonelengthwarnning);
            }
        } else {
            verification_code = verification_code + value;
            dialogbinding.tvVerificat.setText(verification_code);
        }
    }

    @Override
    public void timeFinish() {
        Utils.toMain(this);
    }

    //初始化头部和底部
    @Override
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null) {
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.includeHeader.imgTopIcon);
            }
            Utils.Glide(this, config.getLogoImgUrl(), binding.includeBottom.imgLogoBottom);
            binding.includeBottom.tvTitle.setText(config.getLogoTitle());
            binding.includeBottom.tvPhone.setText("客服电话：" + config.getServiceCall());
        }
    }
}