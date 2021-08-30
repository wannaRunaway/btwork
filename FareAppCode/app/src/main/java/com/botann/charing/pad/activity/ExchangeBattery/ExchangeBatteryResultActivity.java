package com.botann.charing.pad.activity.ExchangeBattery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.botann.charging.pad.R;

import com.botann.charing.pad.activity.BaseActivity;


/**
 * Created by mengchenyun on 2017/2/20.
 */

public class ExchangeBatteryResultActivity extends BaseActivity
{
    private ImageView ivResult;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("换电支付结果");
    }

    @Override
    public int viewLayout() {
        return R.layout.activity_exchange_battery_result;
    }


    @Override
    public void initView()
    {
        ivResult = (ImageView) findViewById(R.id.iv_result);
        tvResult = (TextView) findViewById(R.id.tv_result);
        String status = getIntent().getStringExtra("status");
        if(status.equals("success"))
        {
            ivResult.setImageResource(R.drawable.ic_check);
            tvResult.setText("交易成功");

        }
        else
        {
            ivResult.setImageResource(R.drawable.ic_error);
            tvResult.setText(getIntent().getStringExtra("userInfo"));
        }
        setupBackBtn();
    }

    @Override
    protected void onButtonLeftPressed() {
        Intent intent = new Intent(getApplicationContext(), ExchangeBatteryActivity.class);
        intent.putExtra("clear",true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
