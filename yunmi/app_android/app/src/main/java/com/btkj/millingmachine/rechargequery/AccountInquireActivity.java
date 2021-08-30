package com.btkj.millingmachine.rechargequery;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityBalanceAccountBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
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
 * created by xuedi on 2019/5/4
 */
public class AccountInquireActivity extends BaseActivity implements View.OnClickListener, TimeFinishInterface {
    private ActivityBalanceAccountBinding binding;
    private Crt603rx mCrt603rx = new Crt603rx();
    private String money;
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
    private TimeCount timeCount;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getCarUid();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        initCarUid();
        getCarUid();
    }

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_balance_account);
        bindingOnclick();
        SoundPlayUtils.play(5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(60000, 1000, binding.includeHeader.tvTime, this);
        timeCount.start();
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
            Utils.log("accountinquireHandler读卡循环");
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
            loadData(true, String.valueOf(strlog), "0");
        }
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
        map.put("deviceId", String.valueOf(SharePref.get(AccountInquireActivity.this, "id", 0)));
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
                    Intent intent = new Intent(AccountInquireActivity.this, AccountActivity.class);
                    intent.putExtra("data", vipInquireModel);
                    startActivity(intent);
                } else {
                    Utils.toast(AccountInquireActivity.this, vipInquireModel.getError());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
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
            case R.id.img_clear:
                if (isPhone) {
                    phone = "";
                    binding.tvPhone.setText(phone);
                } else {
                    verification_code = "";
                    binding.tvVerificat.setText(verification_code);
                }
                break;
            case R.id.img_confirm:
                if (binding.tvPhone.getText().toString().isEmpty()) {
                    Utils.toast(AccountInquireActivity.this, "手机号码不能为空");
                    return;
                }
                if (binding.tvVerificat.getText().toString().isEmpty()) {
                    Utils.toast(AccountInquireActivity.this, "验证码不能为空");
                    return;
                }
                loadData(false, "0", "0");
                break;
            case R.id.img_phone_close:
                if (!phone.isEmpty()) {
                    phone = phone.substring(0, phone.length() - 1);
                    binding.tvPhone.setText(phone);
                }
                break;
            case R.id.img_sms_close:
                if (!verification_code.isEmpty()) {
                    verification_code = verification_code.substring(0, verification_code.length() - 1);
                    binding.tvVerificat.setText(verification_code);
                }
                break;
            case R.id.tv_phone:
                binding.tvPhone.setBackground(getResources().getDrawable(R.drawable.dialog_press_back));
                binding.tvVerificat.setBackground(getResources().getDrawable(R.drawable.tv_back));
                isPhone = true;
                break;
            case R.id.tv_verificat:
                binding.tvPhone.setBackground(getResources().getDrawable(R.drawable.tv_back));
                binding.tvVerificat.setBackground(getResources().getDrawable(R.drawable.dialog_press_back));
                isPhone = false;
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_getsms:
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
                binding.tvPhone.setText(phone);
            } else {
                Utils.toast(this, API.phonelengthwarnning);
            }
        } else {
            verification_code = verification_code + value;
            binding.tvVerificat.setText(verification_code);
        }
    }

    //获取短信
    private void getSms() {
        if (phone.isEmpty()) {
            Utils.toast(AccountInquireActivity.this, "电话号码不能为空");
            return;
        }
        String url = API.BASE_URL + API.GET_SMS;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(SharePref.get(AccountInquireActivity.this, "id", 0)));
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
                Utils.toast(AccountInquireActivity.this, baseObject.getError());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        clearHandler();
        timeCount.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearHandler();
        timeCount.cancel();
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
        binding.imgClear.setOnClickListener(this);
        binding.imgConfirm.setOnClickListener(this);
        binding.imgPhoneClose.setOnClickListener(this);
        binding.imgSmsClose.setOnClickListener(this);
        binding.tvPhone.setOnClickListener(this);
        binding.tvVerificat.setOnClickListener(this);
        binding.layoutBack.imgBack.setOnClickListener(this);
        binding.tvGetsms.setOnClickListener(this);
    }

    @Override
    public void timeFinish() {
        Utils.toMain(this);
    }

    private void clearHandler() {
        handler.removeCallbacksAndMessages(null);
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
            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.layoutBottom.tvPhone.setText("客服电话：" + config.getServiceCall());
        }
    }
}
