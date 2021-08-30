package com.btkj.millingmachine.rechargequery;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityRechargeQueryBinding;
import com.btkj.millingmachine.errorview.FixActivity;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MyApplication;
import com.btkj.millingmachine.model.BaseObject;
import com.btkj.millingmachine.model.VipInquire.VipInquireModel;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.SoundPlayUtils;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
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
 * created by xuedi on 2019/5/22
 */
public class RechargeLoginCardPhoneActivity extends BaseActivity implements View.OnClickListener, TimeFinishInterface {
    private ActivityRechargeQueryBinding binding;
    private Crt603rx mCrt603rx = new Crt603rx();
    //    private String money;
    private long strlog;
    private int iRet = 0;
    // iMode  寻卡模式 0 寻所有卡， 1 只寻未休眠卡
    private int iMode = 0;
    private int[] iOutLen = new int[2];
    private byte[] byOutUid = new byte[128];
    private Handler handler = new Handler();
    private String phone = "";
    private String verification_code = "";
    private boolean isPhone = true;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getCarUid();
        }
    };
    private TimeCount timeCount;
//    private Thread thread;

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recharge_query);
//        money = getIntent().getStringExtra("money");
        bindingOnclick();
        SoundPlayUtils.play(8);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(120000, 1000, binding.center.time, this);
        timeCount.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
        initCarUid();
        getCarUid();
//            }
//        });
//        thread.run();
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
            handler.postDelayed(runnable, 6000);
            Utils.log("rechargecardphone读卡循环");
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
            Utils.log(strlog + "");
            clearHandler();
            judgeisFix();
        }
    }

    //判断是否为维修卡
    private void judgeisFix() {
        String url = API.BASE_URL + API.CARD_FIX_PAY;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        /**
         * deviceId（int）， icCardChipId（String）
         */
        map.put("deviceId", String.valueOf(SharePref.get(this, "id", 0)));
        map.put("icCardChipId", String.valueOf(strlog));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(persistentCookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
                if (baseObject.isSuccess()) {
                    Intent intent = new Intent(RechargeLoginCardPhoneActivity.this, FixActivity.class);
                    intent.putExtra("phone", String.valueOf(strlog));
                    startActivityForResult(intent, 2);
                } else {
                    loadData(true, String.valueOf(strlog), "0");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    //加载会员信息
    private void loadData(boolean iscard, String carid, String phonecode) {
        /**
         * deviceId[int]	是	设备id
         * type[short]	是	卡片查询0，电话号查询1
         * icCardChipId[string]		type=0必填
         * phone[string]		type=1必填
         * code[string]	是	type=1必填
         */
        String url = API.BASE_URL + API.VIP_INQUIRE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(SharePref.get(RechargeLoginCardPhoneActivity.this, "id", 0)));
        if (iscard) {
            map.put("type", "0");
            map.put("icCardChipId", carid);
        } else {
            map.put("type", "1");
            map.put("phone", phone);
            map.put("code", verification_code);
        }
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        Utils.log(url + "\n" + requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(json);
                VipInquireModel vipInquireModel = GsonUtils.getInstance().fromJson(json, VipInquireModel.class);
                if (vipInquireModel.isSuccess()) {
                    clearHandler();
                    toRecharge(iscard, vipInquireModel);
                } else {
                    Utils.toast(RechargeLoginCardPhoneActivity.this, vipInquireModel.getError());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    //去充值支付界面
    private void toRecharge(boolean iscard, VipInquireModel vipInquireModel) {
        Intent intent = new Intent(this, RechargeAccountActivity.class);
        intent.putExtra("carid", String.valueOf(strlog));
//        intent.putExtra("money", money);
        intent.putExtra("isCard", iscard);
        intent.putExtra("phone", phone);
        intent.putExtra("code", binding.yanzhengma.getText().toString());
        intent.putExtra("vip", vipInquireModel);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.qingchuquanbu:
                if (isPhone) {
                    phone = "";
                    binding.phone.setText(phone);
                } else {
                    verification_code = "";
                    binding.yanzhengma.setText(verification_code);
                }
                break;
            case R.id.queding:
                if (binding.phone.getText().toString().isEmpty()) {
                    Utils.toast(RechargeLoginCardPhoneActivity.this, "手机号码不能为空");
                    return;
                }
                if (binding.yanzhengma.getText().toString().isEmpty()) {
                    Utils.toast(RechargeLoginCardPhoneActivity.this, "验证码不能为空");
                    return;
                }
                loadData(false, "0", binding.phone.getText().toString());
                break;
            case R.id.shanchu:
                if (isPhone) {
                    if (!phone.isEmpty()) {
                        phone = phone.substring(0, phone.length() - 1);
                        binding.phone.setText(phone);
                    }
                } else {
                    if (!verification_code.isEmpty()) {
                        verification_code = verification_code.substring(0, verification_code.length() - 1);
                        binding.yanzhengma.setText(verification_code);
                    }
                }
                break;
            case R.id.phone:
                binding.phone.setBackground(getResources().getDrawable(R.drawable.dialog_press_back));
                binding.yanzhengma.setBackground(getResources().getDrawable(R.drawable.tv_back));
                isPhone = true;
                break;
            case R.id.yanzhengma:
                binding.phone.setBackground(getResources().getDrawable(R.drawable.tv_back));
                binding.yanzhengma.setBackground(getResources().getDrawable(R.drawable.dialog_press_back));
                isPhone = false;
                break;
            case R.id.back:
                finish();
                break;
            case R.id.huoquyanzhengma:
                getSms();
                break;
            default:
                break;
        }
    }

    //控制手机号充值
    private void selectInput(String value) {
        if (isPhone) {
            if (phone.length() < 12) {
                phone = phone + value;
                binding.phone.setText(phone);
            } else {
                Utils.toast(this, API.phonelengthwarnning);
            }
        } else {
            verification_code = verification_code + value;
            binding.yanzhengma.setText(verification_code);
        }
    }

    //获取短信
    private void getSms() {
        if (phone.isEmpty()) {
            Utils.toast(RechargeLoginCardPhoneActivity.this, "电话号码不能为空");
            return;
        }
        String url = API.BASE_URL + API.GET_SMS;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(SharePref.get(RechargeLoginCardPhoneActivity.this, "id", 0)));
        map.put("phone", phone);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        Utils.log(url + "\n" + requestParams);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
                Utils.log(json);
                Utils.toast(RechargeLoginCardPhoneActivity.this, baseObject.getError());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.toast(RechargeCardPhoneActivity.this, "网络连接失败");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
//        thread.s
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

    //设置点击
    private void bindingOnclick() {
        binding.img0.setOnClickListener(this);
        binding.img1.setOnClickListener(this);
        binding.img2.setOnClickListener(this);
        binding.img3.setOnClickListener(this);
        binding.img4.setOnClickListener(this);
        binding.img5.setOnClickListener(this);
        binding.img6.setOnClickListener(this);
        binding.img7.setOnClickListener(this);
        binding.img8.setOnClickListener(this);
        binding.img9.setOnClickListener(this);
        binding.qingchuquanbu.setOnClickListener(this);
        binding.shanchu.setOnClickListener(this);
        binding.queding.setOnClickListener(this);
//        binding.imgPhoneClose.setOnClickListener(this);
//        binding.imgSmsClose.setOnClickListener(this);
        binding.phone.setOnClickListener(this);
        binding.yanzhengma.setOnClickListener(this);
        binding.center.back.setOnClickListener(this);
        binding.huoquyanzhengma.setOnClickListener(this);
    }

    @Override
    public void timeFinish() {
        Utils.toMain(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2) {
            String shicha = (String) SharePref.get(RechargeLoginCardPhoneActivity.this, API.qidongshicha, "");
            String guozaidianliu = (String) SharePref.get(RechargeLoginCardPhoneActivity.this, API.guozaibaohu, "");
            byte[] shacha = new byte[4];
            shacha[0] = 0x23;
            shacha[1] = (byte) 0xD1;
            shacha[2] = 0x01;
            shacha[3] = (byte) (Integer.parseInt(shicha) & 0xff);
            MyApplication.getInstance().sendShichaQidong(shacha);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    byte[] dianliu = new byte[5];
                    dianliu[0] = 0x23;
                    dianliu[1] = (byte) 0xD2;
                    dianliu[2] = 0x02;
                    dianliu[3] = (byte) ((Integer.parseInt(guozaidianliu) >> 8) & 0xff);
                    dianliu[4] = (byte) (Integer.parseInt(guozaidianliu) & 0xff);
                    MyApplication.getInstance().sendShichaQidong(dianliu);
                }
            }, 500);
        }
    }

    //初始化头部和底部
    @Override
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null) {
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
//            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("服务电话：" + config.getServiceCall());
        }
    }
}
