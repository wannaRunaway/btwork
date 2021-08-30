package com.botann.charing.pad.activity.Pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.botann.charging.pad.R;

import com.botann.charing.pad.activity.ActivityController;
import com.botann.charing.pad.activity.ExchangeBattery.ExchangeBatteryActivity;
import com.botann.charing.pad.activity.Main.MainActivity;
import com.botann.charing.pad.activity.BaseActivity;


/**
 * Created by mengchenyun on 2017/2/20.
 */

public class PayResultActivity extends BaseActivity implements View.OnClickListener{


    private boolean isFromChangeBattery;
    private boolean isPackage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("支付结果");
        isFromChangeBattery = getIntent().getBooleanExtra("isFromChangeBattery",false);
    }

    @Override
    public int viewLayout() {
        return R.layout.pay_result;
    }

    @Override
    public void initView() {
        String status = getIntent().getStringExtra("status");
        boolean isPaySuccess = status.equals("success");

        Button btnJump      = findButtonById(R.id.btn_jump);
        Button btnFinish    = findButtonById(R.id.btn_finish);
        Button btnNext      = findButtonById(R.id.btn_next);

        btnFinish.setOnClickListener(this);
        if (isFromChangeBattery) {
            btnJump.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        } else {
            btnNext.setOnClickListener(this);
            btnJump.setOnClickListener(this);
        }

        if(isPaySuccess) {
            String driverName = getIntent().getStringExtra("driverName");
            String payWay = getIntent().getStringExtra("payWay");
            String chargeAmount = getIntent().getStringExtra("chargeAmount");
            TextView tvRechargeInfo = (TextView) findViewById(R.id.tv_charge_info);
            TextView tvRechargeAmount = (TextView) findViewById(R.id.tv_recharge_amount);
            isPackage = getIntent().getBooleanExtra("isPackage", false);
            if (isPackage){
                tvRechargeInfo.setText("支付成功");
                tvRechargeAmount.setText("支付金额：" + chargeAmount + "元");
                btnJump.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
            }else {
                tvRechargeInfo.setText("充值成功");
                tvRechargeAmount.setText("充值金额：" + chargeAmount + "元");
                btnJump.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
            }
            TextView tvUsername = (TextView) findViewById(R.id.tv_driver_name);
            tvUsername.setText(driverName);
            TextView tvPayWay = (TextView) findViewById(R.id.tv_pay_way);
            tvPayWay.setText(payWay);
        } else {
            LinearLayout failureLayout = (LinearLayout) findViewById(R.id.failure_layout);
            failureLayout.setVisibility(View.VISIBLE);
            LinearLayout successLayout = (LinearLayout) findViewById(R.id.success_layout);
            successLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btn_jump:
                Intent toMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent toChangeBatteryActivity = new Intent(getApplicationContext(), ExchangeBatteryActivity.class);
                if (isFromChangeBattery) {
                    toChangeBatteryActivity.putExtra("clear",false);
                } else {
                    toChangeBatteryActivity.putExtra("clear",true);
                    toChangeBatteryActivity.putExtra("account",getIntent().getStringExtra("account"));
                }
                startActivity(toChangeBatteryActivity);
//                TaskStackBuilder.create(PayResultActivity.this)
//                        .addNextIntent(toMainActivity)
//                        // use this method if you want "intentOnTop" to have it's parent chain of activities added to the stack. Otherwise, more "addNextIntent" calls will do.
//                        .addNextIntent(toChangeBatteryActivity)
//                        .startActivities();
                break;
            default:
                if (isFromChangeBattery) {
                    ActivityController.finish(4);
                } else {
                    ActivityController.toFirstActivity();
                }
                break;
        }
    }

    //点击充值完成返回按钮，套餐购买完毕就直接返回主页面
    @Override
    protected void onButtonLeftPressed() {
        if (isPackage) {
            ActivityController.finish(4);
        }else {
            finish();
        }
    }
}
