package com.botann.driverclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.ui.activity.promotions.PromotionsRecordActivity;
import com.botann.driverclient.utils.Constants;

public class PayResultActivity extends BaseActivity implements View.OnClickListener {

    private TextView mRechargeComplete;
    private TextView mToRechargeRecord;
    private TextView tv_recharge_info, tv_recharge_title;
    private Context mContext;
    private static PayResultActivity mInstance = null;
    private boolean isPackage = false;

    public static PayResultActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInstance = this;
        setContentView(R.layout.pay_result);
        isPackage = getIntent().getBooleanExtra("isPackage", false);
        bindView();
    }

    public void bindView() {
        mRechargeComplete = (TextView) this.findViewById(R.id.recharge_complete);
        mToRechargeRecord = (TextView) this.findViewById(R.id.to_recharge_record);
        tv_recharge_info = (TextView) findViewById(R.id.recharge_info);
        tv_recharge_title = (TextView)findViewById(R.id.recharge_top_title);
        mRechargeComplete.setOnClickListener(this);
        mToRechargeRecord.setOnClickListener(this);
        initView();
    }

    //充值成功、套餐活动支付成功
    private void initView() {
        if (isPackage){
            tv_recharge_title.setText("支付结果");
            tv_recharge_info.setText("支付成功");
            mToRechargeRecord.setText("查看活动记录");
        }else {
            tv_recharge_title.setText("充值结果");
            tv_recharge_info.setText("充值成功");
            mToRechargeRecord.setText("查看充值记录");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_complete:
                finishPage();
                break;
            case R.id.to_recharge_record:
                if (isPackage){ //跳转到活动记录界面
                    Intent intent = new Intent(PayResultActivity.this, PromotionsRecordActivity.class);
                    intent.putExtra("isPayResult", true);
                    startActivity(intent);
                }else {
                    Constants.whichFragment = 2;
                    Intent message2 = new Intent(MainApp.getInstance().getApplicationContext(), MainActivity.class);
                    message2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    MainApp.getInstance().getApplicationContext().startActivity(message2);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishPage();
    }

    //点击完成和右下角的返回按钮，直接返回主页面
    private void finishPage() {
        Constants.whichFragment = 0;
        Intent message = new Intent(MainApp.getInstance().getApplicationContext(), MainActivity.class);
        message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MainApp.getInstance().getApplicationContext().startActivity(message);
    }
}
