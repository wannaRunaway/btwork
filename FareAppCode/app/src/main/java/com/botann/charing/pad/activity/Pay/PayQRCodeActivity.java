package com.botann.charing.pad.activity.Pay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.utils.QRCodeUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import com.botann.charging.pad.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.activity.BaseActivity;

/**
 * Created by mengchenyun on 2017/2/16.
 */

public class PayQRCodeActivity extends BaseActivity {

    private ImageView ivQRCode;
    private TextView tvRechargeAmount;

    private String account;
    private String driverName;
    private String totalAmount;
    private String chargeAmount;

    private String outTradeNo;
    private boolean payResult;
    public Timer mTimer = new Timer();
    private Boolean isFromChangeBattery = false;
    private boolean isPackage;
    private String tradeRemark;
    //新加入套餐订单明细
    private RelativeLayout re_activity_info;
    private TextView tv_activity_name, tv_driver, tv_activity_buy_time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        driverName = getIntent().getStringExtra("driverName");
        setTitle("账户充值: "+ driverName);
    }



    @Override
    public int viewLayout() {
        return R.layout.pay_qrcode_main;
    }


    @Override
    public void initView()
    {
        Intent intent = getIntent();
        isFromChangeBattery = intent.getBooleanExtra("isFromChangeBattery",false);
        account = intent.getStringExtra("account");
        driverName = intent.getStringExtra("driverName");
        totalAmount = intent.getStringExtra("totalAmount");
        chargeAmount = intent.getStringExtra("chargeAmount");
        int payIndex = intent.getIntExtra("payIndex",1);
        isPackage = intent.getBooleanExtra("isPackage", false);
        tradeRemark = intent.getStringExtra("tradeRemark");
        tvRechargeAmount = (TextView) findViewById(R.id.tv_recharge_amount);
        if (isPackage){
            tvRechargeAmount.setText("支付金额：" + chargeAmount + "元");
        }else {
            tvRechargeAmount.setText("充值金额：" + chargeAmount + "元");
        }
        ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
        initActivityInfoUI();
        requestRQCodeInfo(payIndex);
//        测试查看
//        Button btn = (Button)findViewById(R.id.btnResult);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent resultIntent = new Intent(getApplicationContext(), PayResultActivity.class);
//                resultIntent.putExtra("status", "success");
////                resultIntent.putExtra("prev", totalAmount);
//                resultIntent.putExtra("driverName", driverName);
//                resultIntent.putExtra("payWay", "支付宝");
//                resultIntent.putExtra("chargeAmount", chargeAmount);
//                resultIntent.putExtra("isFromChangeBattery",true);
////                resultIntent.putExtra("status", "failure");
//                startActivity(resultIntent);
//            }
//        });

    }

    //加入套餐活动，司机信息界面
    private void initActivityInfoUI() {
        re_activity_info = (RelativeLayout) findViewById(R.id.re_activity_info);
        tv_activity_name = (TextView) findViewById(R.id.tv_activity_name);
        tv_driver = (TextView) findViewById(R.id.tv_driver);
        tv_activity_buy_time = (TextView) findViewById(R.id.tv_activity_buy_time);
        tv_driver.setText("司机姓名:"+driverName+"     车牌号:"+getIntent().getStringExtra("carplateNum"));
        tv_activity_name.setText("活动名称:"+getIntent().getStringExtra("activityName")+"");
        tv_activity_buy_time.setText("购买周期:"+getIntent().getStringExtra("activityTime")+"");
        re_activity_info.setVisibility(isPackage?View.VISIBLE:View.GONE);
    }

    /**
     * 请求二维码账单信息
     * @param payIndex
     */
    private void requestRQCodeInfo(final Integer payIndex) {

        URLParams params = new URLParams();
        params.put("siteId", User.shared().getStationId());
        params.put("account",account);
        params.put("amount",chargeAmount);
        params.put("type",payIndex);// type=1微信
        if (isPackage) {
            params.put("tradeType", "1");
            params.put("tradeRemark", tradeRemark);
        }
        SGHTTPManager.POST(API.URL_ACCOUNT_RECHARGE, params, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                try {
                    if (isSuccess) {
                        String code ;
                        if (payIndex == 0) {
                            String tradeCode = fetchModel.getSting("tradeCode");
                            JSONObject jsonObject = new JSONObject(tradeCode);
                            String alipay_trade_precreate_response = jsonObject.getString("alipay_trade_precreate_response");
                            JSONObject jsonObject1 = new JSONObject(alipay_trade_precreate_response);
                            code = jsonObject1.getString("qr_code");
                            outTradeNo = jsonObject1.getString("out_trade_no");
                        } else {
                            code = fetchModel.getSting("codeUrl");
                            outTradeNo = fetchModel.getSting("outTradeNo");
                        }
                        refreshQRCode(code);
                        startTimerTask(payIndex);
                    } else {
                        onButtonLeftPressed();
                        toast(userInfo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    public void refreshQRCode(String str)
    {
        if (str == null || str.isEmpty()) {
            Toast.makeText(getApplicationContext(), "二维码内容为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap image = QRCodeUtil.qrCodeImageWithStr(str);
        if (image != null) ivQRCode.setImageBitmap(image);
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what < 0) {
                SGHTTPManager.POST(API.URL_PAY_RESULT, new URLParams("outTradeNo",outTradeNo), new SGHTTPManager.SGRequestCallBack() {
                    @Override
                    public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                        if (isSuccess) payResult = true;
                    }
                });
            } else {
                Intent resultIntent = new Intent(getApplicationContext(), PayResultActivity.class);
                resultIntent.putExtra("isPackage", isPackage);
                resultIntent.putExtra("status", "success");
                resultIntent.putExtra("driverName", driverName);
                resultIntent.putExtra("account",account);
                resultIntent.putExtra("payWay", msg.what == 0 ? "支付宝": "微信");
                resultIntent.putExtra("chargeAmount", chargeAmount);
                resultIntent.putExtra("isFromChangeBattery",isFromChangeBattery);
                startActivity(resultIntent);
                mTimer.cancel();//
            }

        }
    };


    /**
     * 轮询支付结果
     * @param payIndex 0：支付宝支付，1：微信支付
     */
    private void startTimerTask(int payIndex)
    {
        try
        {
            if(mTimer!=null)
            {
                mTimer.cancel();// 退出之前的mTimer
            }
            if (outTradeNo == null) {
                toast("订单号获取失败，请去充值记录查看支付结果！");
                return;
            }
            mTimer = new Timer();// new一个Timer,否则会报错
            payResult = false;
            timerTask(payIndex);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void timerTask(final int payIndex) {
        //创建定时线程执行更新任务
        mTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (payResult) {
                    System.out.println("TimerTask-->Id is "
                            + Thread.currentThread().getId());// TimerTask在它自己的线程中
                    mHandler.sendEmptyMessage(payIndex);// 向Handler发送消息
                } else {
                    mHandler.sendEmptyMessage(-1);
                }
            }
        }, 2000, 2000);// 定时任务
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();// 程序退出时cancel timer
        }
        super.onDestroy();
    }
}
