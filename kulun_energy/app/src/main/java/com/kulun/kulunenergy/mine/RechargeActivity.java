package com.kulun.kulunenergy.mine;

import android.annotation.SuppressLint;
import androidx.databinding.DataBindingUtil;
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
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityRechargeBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.model.AuthResult;
import com.kulun.kulunenergy.model.BaseObject;
import com.kulun.kulunenergy.model.PayResult;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.model.WxPayModel;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.CashInputFilter;
import com.kulun.kulunenergy.utils.Constants;
import com.kulun.kulunenergy.utils.Customize;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.requestparams.Myparams;
import com.kulun.kulunenergy.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class RechargeActivity extends BaseActivity implements View.OnClickListener , TextWatcher {
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
        InputFilter[] filters={new CashInputFilter()};
        binding.etRecharge.setFilters(filters);
        binding.etRecharge.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        binding.etRecharge.addTextChangedListener(this);
    }

    private void bindClick() {
        binding.header.left.setOnClickListener(this);
        binding.img100.setOnClickListener(this);
        binding.img200.setOnClickListener(this);
        binding.img300.setOnClickListener(this);
        binding.img400.setOnClickListener(this);
        binding.recharge.setOnClickListener(this);
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
                binding.etRecharge.setSelection(3);

                break;
            case R.id.img200:
                clearImgSelected();
                binding.img200.setSelected(true);
                binding.etRecharge.setText("200");
                binding.etRecharge.setSelection(3);
                break;
            case R.id.img300:
                clearImgSelected();
                binding.img300.setSelected(true);
                binding.etRecharge.setText("500");
                binding.etRecharge.setSelection(3);
                break;
            case R.id.img400:
                clearImgSelected();
                binding.img400.setSelected(true);
                binding.etRecharge.setText("1000");
                binding.etRecharge.setSelection(4);
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
            case R.id.recharge:
                String money = binding.etRecharge.getText().toString();
                if (money.isEmpty()) {
                    Utils.snackbar( RechargeActivity.this, "请选择或者填写支付金额");
                    return;
                }
                if (rechargeType == 0) {
                    Utils.snackbar( RechargeActivity.this, "请选择微信或者支付宝支付");
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
            if (Double.parseDouble(money) < 1.00&& !Constants.isDebug ) {
                Utils.snackbar( RechargeActivity.this, "充值金额最少为1元");
                return;
            }
        }catch (Exception e){
            Utils.log(null,null,"充值金额错误");
        }
        String url = API.BASE_URL + API.RECHARGE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("payWay", String.valueOf(rechargeType));
        map.put("balance", money);
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, RechargeActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                if (rechargeType == 2) {
                    WxPayModel model = GsonUtils.getInstance().fromJson(json, WxPayModel.class);
                    if (model.isSuccess()) {
                        wxpay(model.getContent());
                    } else {
                        Utils.snackbar( RechargeActivity.this, model.getMsg());
                    }
                } else if (rechargeType == 1) {
                    BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
                    if (baseObject.isSuccess()) {
                        payV2(baseObject.getContent());
                    } else {
                        Utils.snackbar( RechargeActivity.this, baseObject.getMsg());
                    }
                }
            }
        });
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                if (rechargeType == 2) {
//                    WxPayModel model = GsonUtils.getInstance().fromJson(json, WxPayModel.class);
//                    if (model.isSuccess()) {
//                        wxpay(model.getContent());
//                    } else {
//                        Utils.snackbar( RechargeActivity.this, model.getMsg());
//                    }
//                } else if (rechargeType == 1) {
//                    BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                    if (baseObject.isSuccess()) {
//                        payV2(baseObject.getContent());
//                    } else {
//                        Utils.snackbar( RechargeActivity.this, baseObject.getMsg());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar( RechargeActivity.this, API.net_error);
//                Utils.log(url, requestParams, null);
//            }
//        });
    }

    public void payV2(String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Utils.log(null,null, "支付包 orderInfo"+result.toString());
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
        User.getInstance().setWxappid(content.getAppid());
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
                        EventBus.getDefault().post("rechargesuccess");
                        Utils.snackbar( RechargeActivity.this, "支付成功");
//                        Intent message = new Intent(RechargeActivity.this, PayResultActivity.class);
//                        message.putExtra("isPackage", false);
//                        message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(message);
//                        finish();
                    } else {
                        Utils.snackbar( RechargeActivity.this, "支付失败");
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
                        Utils.snackbar( RechargeActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        Utils.snackbar( RechargeActivity.this,
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        binding.etRecharge.removeTextChangedListener(this);
        if(s!=null){
            String amount=binding.etRecharge.getText().toString().trim();
            if(!amount.equals("100")){
                binding.img100.setSelected(false);
            }
            if(!amount.equals("200")){
                binding.img200.setSelected(false);
            }
            if(!amount.equals("500")){
                binding.img300.setSelected(false);
            }
            if(!amount.equals("1000")){
                binding.img400.setSelected(false);
            }

        }
        binding.etRecharge.addTextChangedListener(this);
    }
}
