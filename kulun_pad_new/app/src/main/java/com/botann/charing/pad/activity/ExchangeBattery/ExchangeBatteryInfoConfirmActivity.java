package com.botann.charing.pad.activity.ExchangeBattery;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.botann.charing.pad.base.OnMultiClickListener;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.callbacks.SGFinishCallBack;
import com.botann.charing.pad.callbacks.SGOnAlertClick;
import com.botann.charging.pad.R;

import com.botann.charing.pad.model.User;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.activity.BaseActivity;
import com.botann.charing.pad.utils.ToastUtil;


/**
 * Created by mengchenyun on 2017/2/20.
 */

public class ExchangeBatteryInfoConfirmActivity extends BaseActivity {

    private TextView tvAccountType;
    private TextView tvUsername;
    private TextView tvBalance;
    private TextView tvRechargeAmount;
    private TextView tvTotalMiles;
    private TextView tvTripMiles;
    private TextView tvChargeMiles;
    private TextView tvTotalFee;
    private TextView tvCoupon;
    private Button btnConfirm;
    private Button btnCancel;
    private Activity mContext;
    private String exchangeRecordId;
    private long lastClickTime = 0;
    private TextView tv_fenzhangyonghu,tv_fenzhangzhifu,tv_fenzhanglicheng, tv_fenzhang_youhuiquan;
    private LinearLayout li_share;
    private String sharename,sharelicheng,fenzhangyouhuiquan;
    private double sharepay, fare;
    private int shareid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView();
        setTitle("换电录入（" + User.shared().getStation() + "）");
        setDataSource();
        showHideView();
//        requestShowCheckBox();
    }

    private void showHideView() {
        if (sharename == null || sharename.equals("")){
            li_share.setVisibility(View.GONE);
        }else {
            li_share.setVisibility(View.VISIBLE);
            tv_fenzhangyonghu.setText(sharename+"");
//            long fenzhangzhifu = Math.round(sharepay);
            tv_fenzhangzhifu.setText(sharepay+"");
            tv_fenzhanglicheng.setText(sharelicheng+"");
        }
    }

    @Override
    public int viewLayout() {
        return R.layout.activity_exchange_battery_info_confirm;
    }

    @Override
    public void initView() {
        tvAccountType = (TextView) findViewById(R.id.tv_account_type);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        tvRechargeAmount = (TextView) findViewById(R.id.tv_recharge_amount);
        tvTotalMiles = (TextView) findViewById(R.id.tv_total_miles);
        tvTripMiles = (TextView) findViewById(R.id.tv_trip_miles);
        tvChargeMiles = (TextView) findViewById(R.id.tv_charge_miles);
        tvTotalFee = (TextView) findViewById(R.id.tv_total_fee);
        tvCoupon = (TextView) findViewById(R.id.tv_coupon);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
//        btnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert("是否确认扣款?", new SGOnAlertClick() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int index) {
//                        if (index > 0) {
//                            btnConfirm.setEnabled(false);
//                            postConfirmPay(new SGFinishCallBack() {
//                                @Override
//                                public void onFinish(Boolean result) {
//                                    btnConfirm.setEnabled(true);
//                                }
//                            });
//                        }
//                    }
//                });
//            }
//        });
        btnConfirm.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                alert("是否确认扣款?", new SGOnAlertClick() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        long curClickTime = System.currentTimeMillis();
                        if((curClickTime - lastClickTime) >= 5000) {
                            // 超过点击间隔后再将lastClickTime重置为当前点击时间
                            lastClickTime = curClickTime;
                            if (index > 0) {
                                btnConfirm.setEnabled(false);
                                postConfirmPay(new SGFinishCallBack() {
                                    @Override
                                    public void onFinish(Boolean result) {
                                        btnConfirm.setEnabled(true);
                                    }
                                });
                            }
                        }else {
                            Toast toast = Toast.makeText(mContext, "点击过快", Toast.LENGTH_SHORT);
                            ToastUtil.showMyToast(toast, 3000);
                        }
                    }
                });
            }

            @Override
            public void onNoneClick(View view) {
                Toast toast = Toast.makeText(mContext, "点击过快", Toast.LENGTH_SHORT);
                ToastUtil.showMyToast(toast, 3000);
            }
        });
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonLeftPressed();
            }
        });
        tv_fenzhangyonghu = (TextView) findViewById(R.id.tv_fenzhangyonghu);
        tv_fenzhangzhifu = (TextView) findViewById(R.id.tv_fenzhangzhifu);
        tv_fenzhanglicheng = (TextView) findViewById(R.id.tv_fenzhanglicheng);
        li_share = (LinearLayout) findViewById(R.id.li_share);
        tv_fenzhang_youhuiquan = (TextView) findViewById(R.id.tv_youhuiquan);
    }


    private void setDataSource() {
        exchangeRecordId = getIntent().getStringExtra("exchangeRecordId");
        String driverName = getIntent().getStringExtra("driverName"); //用户名
        String accountBalance = getIntent().getStringExtra("accountBalance");//余额
        fare = getIntent().getDoubleExtra("fare", 0);//需支付
        String totalMiles = getIntent().getStringExtra("carMile");  //总里程
        Integer travelMile = getIntent().getIntExtra("travelMile", 0);    //行驶里程
        String chargeMile = getIntent().getStringExtra("chargeMile");//计费里程
        Float couponUse = Float.parseFloat(getIntent().getStringExtra("couponUse"));
        String realFare = getIntent().getStringExtra("realFare");
        String carplateNum = getIntent().getStringExtra("carplateNum");
        int accountType = Integer.parseInt(getIntent().getStringExtra("accountType"));
        sharename = getIntent().getStringExtra("sharename");
        sharepay = getIntent().getDoubleExtra("sharepay", 0);
        sharelicheng = getIntent().getStringExtra("sharelicheng");
        shareid = getIntent().getIntExtra("shareid", 0);
        fenzhangyouhuiquan = getIntent().getStringExtra("fenzhangyouhuiquan");
        tv_fenzhang_youhuiquan.setText(fenzhangyouhuiquan+"");
        if (accountType == 0) {
            tvAccountType.setText("对私账户计费");
        } else {
            tvAccountType.setText("对公账户计费");
        }
        tvUsername.setText("用户姓名：" + driverName + "\n" + carplateNum);
        tvBalance.setText(accountBalance);
        tvRechargeAmount.setText("需支付：" + realFare);
        tvTotalMiles.setText(totalMiles);
        tvTripMiles.setText("" + travelMile);
        tvChargeMiles.setText(chargeMile);
        String canUse = "（无）";
        if (couponUse - 0 > 0.0001) canUse = "（可用）";
        tvCoupon.setText(couponUse + canUse);
        tvTotalFee.setText(fare+"");
        if (travelMile < 0 || travelMile > 500) {
//            alert("里程数据异常！！");
            alert("里程数据异常请审核，是否继续换电？", "继续换电", new SGOnAlertClick() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    if (index == 0) finish();
                }
            });
        } else {
            btnConfirm.setVisibility(View.VISIBLE);
        }
    }

    /***********************************NET WORK**************************************/

    /**
     * 确认付款请求
     */
    private void postConfirmPay(final SGFinishCallBack finishCallBack) {
        showProgressHud("确认付款中...");
        URLParams params = new URLParams("recordId", exchangeRecordId);
        params.put("batteryS", getIntent().getStringExtra("batteryS"));
        params.put("shareRecordId", shareid);
        SGHTTPManager.POST(API.URL_EXCHANGE_CONFIRM_PAY, params, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                hideProgressHud();
                String status = "failure";
                if (isSuccess) status = "success";
                Intent changeSuccess = new Intent(mContext, ExchangeBatteryResultActivity.class);
                changeSuccess.putExtra("status", status);
                changeSuccess.putExtra("userInfo", userInfo);
                startActivity(changeSuccess);
                finishCallBack.onFinish(true);
            }
        });
    }

}
