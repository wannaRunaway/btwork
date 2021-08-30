package com.kulun.energynet.mine;

import android.annotation.SuppressLint;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.bill.BillActivity;
import com.kulun.energynet.databinding.ActivityRechargeBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.model.AuthResult;
import com.kulun.energynet.model.PayResult;
import com.kulun.energynet.model.WxPayModel;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.requestparams.ResponseString;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.CashInputFilter;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.utils.JsonSplice;
import com.kulun.energynet.utils.Utils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

//import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class RechargeActivity extends BaseActivity implements View.OnClickListener {
    private ActivityRechargeBinding binding;
    private int rechargeType = 1;
    IWXAPI api;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recharge);
        bindClick();
        binding.header.title.setText("充值");
        binding.img100.setSelected(true);
        binding.rechargeAlipay.setSelected(true);
        InputFilter[] filters = {new CashInputFilter()};
        binding.etRecharge.setFilters(filters);
        binding.etRecharge.setRawInputType(InputType.TYPE_CLASS_NUMBER);
//        binding.etRecharge.addTextChangedListener(this);
        binding.include.finish.setText("充值");
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_rmb);
        drawable.setBounds(0,0,22,30);
        binding.etRecharge.setCompoundDrawables(drawable,null,null,null);
    }

    private void bindClick() {
        binding.header.left.setOnClickListener(this);
        binding.img100.setOnClickListener(this);
        binding.img200.setOnClickListener(this);
        binding.img300.setOnClickListener(this);
        binding.img400.setOnClickListener(this);
        binding.include.finish.setOnClickListener(this);
        binding.rechargeAlipay.setOnClickListener(this);
        binding.rechargeWx.setOnClickListener(this);
        //binding.etRecharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.img100:
                clearImgSelected();
                binding.img100.setSelected(true);
                binding.etRecharge.setText("100");
//                binding.etRecharge.setSelection(3);

                break;
            case R.id.img200:
                clearImgSelected();
                binding.img200.setSelected(true);
                binding.etRecharge.setText("200");
//                binding.etRecharge.setSelection(3);
                break;
            case R.id.img300:
                clearImgSelected();
                binding.img300.setSelected(true);
                binding.etRecharge.setText("300");
//                binding.etRecharge.setSelection(3);
                break;
            case R.id.img400:
                clearImgSelected();
                binding.img400.setSelected(true);
                binding.etRecharge.setText("400");
//                binding.etRecharge.setSelection(3);
                break;
            case R.id.recharge_alipay:
                binding.wxChoice.setSelected(false);
                binding.alipayChoice.setSelected(true);
                rechargeType = 1;
                break;
            case R.id.recharge_wx:
                binding.wxChoice.setSelected(true);
                binding.alipayChoice.setSelected(false);
                rechargeType = 2;
                break;
            case R.id.finish:
                String money = binding.etRecharge.getText().toString();
                if (money.isEmpty()) {
                    Utils.snackbar(RechargeActivity.this, "请选择或者填写支付金额");
                    return;
                }
                if (rechargeType == 0) {
                    Utils.snackbar(RechargeActivity.this, "请选择微信或者支付宝支付");
                    return;
                }
                recharge(money);
                break;
           /* case R.id.et_recharge:
                clearImgSelected();
                break;*/
            default:
                break;
        }
    }

    /**
     * payWay       [int]	是	alipay,1; wxpay,2;
     * balance      [number]	是	充值金额，元
     */
    private void recharge(String money) {
        try {
            if (Double.parseDouble(money) < 100 ) {
                Utils.snackbar( RechargeActivity.this, "充值金额至少100元");
                return;
            }
        }catch (Exception e){
            Utils.log(null,"","充值金额错误");
        }
        //Type订单类型0换电消费1充值2活动消费AppType应用类型app/ipad/min/webPayType支付渠道0支付宝1微信
        //{
        //  "type": 1,
        //  "pay_type": 2,
        //  "pay_mode": 0,
        //
        //  "amount": "0.01"
        //}
        int type = rechargeType == 1 ? 0 : 1;
//        double amount = Double.parseDouble(binding.etRecharge.getText().toString());
        String json = JsonSplice.leftparent + JsonSplice.yin + "type" + JsonSplice.yinandmao + type + JsonSplice.dou +
                JsonSplice.yin + "pay_type" + JsonSplice.yinandmao + 2 + JsonSplice.dou +
                JsonSplice.yin + "amount" + JsonSplice.yinandmao + JsonSplice.yin + money + JsonSplice.yinanddou +
                JsonSplice.yin + "pay_mode" + JsonSplice.yinandmao + 0 + JsonSplice.rightparent;
        new MyRequest().pay(API.pay, true, json, this, null, null, new ResponseString() {
            @Override
            public void response(String s, JsonObject jsonObject) {
                if (rechargeType == 2) {
                    WxPayModel.ContentBean model = GsonUtils.getInstance().fromJson(jsonObject, WxPayModel.ContentBean.class);
                    wxpay(model);
                } else if (rechargeType == 1) {
                    payV2(s);
                }
            }
        });
    }

    public void payV2(String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Utils.log(null, "", "支付包 orderInfo" + result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void wxpay(WxPayModel.ContentBean content) {
        api = WXAPIFactory.createWXAPI(this, content.getAppid());
        UserLogin.getInstance().setWxappid(content.getAppid());
        PayReq request = new PayReq();
        request.appId = content.getAppid();
        request.partnerId = content.getPartnerid();
        request.prepayId = content.getPrepayid();
        request.packageValue = content.getPackageX();
        request.nonceStr = content.getNoncestr();
        request.timeStamp = content.getTimestamp();
        request.sign = content.getSign();
        boolean b = api.sendReq(request);
    }

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
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
//                        EventBus.getDefault().post("rechargesuccess");
                        Utils.snackbar(RechargeActivity.this, "支付成功");
                        Intent intent = new Intent(RechargeActivity.this, BillActivity.class);
                        startActivity(intent);
                        finish();
//                        Intent message = new Intent(RechargeActivity.this, PayResultActivity.class);
//                        message.putExtra("isPackage", false);
//                        message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(message);
//                        finish();
                    } else {
                        Utils.snackbar(RechargeActivity.this, "支付失败");
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
                        Utils.snackbar(RechargeActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        Utils.snackbar(RechargeActivity.this,
                                "授权失败\n" + String.format("authCode:%s", authResult.getAuthCode()));

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void clearImgSelected() {
        binding.img100.setSelected(false);
        binding.img200.setSelected(false);
        binding.img300.setSelected(false);
        binding.img400.setSelected(false);
    }

//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        binding.etRecharge.removeTextChangedListener(this);
//        if (s != null) {
//            String amount = binding.etRecharge.getText().toString().trim();
//            if (!amount.equals("100")) {
//                binding.img100.setSelected(false);
//            }
//            if (!amount.equals("200")) {
//                binding.img200.setSelected(false);
//            }
//            if (!amount.equals("500")) {
//                binding.img300.setSelected(false);
//            }
//            if (!amount.equals("1000")) {
//                binding.img400.setSelected(false);
//            }
//
//        }
//        binding.etRecharge.addTextChangedListener(this);
//    }
}
