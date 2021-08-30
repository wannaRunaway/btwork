package com.botann.charing.pad.activity.Pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.URLParams;
import com.botann.charging.pad.R;


import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.components.zxing.QRScanActivity;
import com.botann.charing.pad.activity.BaseActivity;
import com.botann.charing.pad.utils.DateUtils;
import com.botann.charing.pad.utils.ToastUtil;


/**
 * Created by mengchenyun on 2016/11/5.
 */

public class PaymentActivity extends BaseActivity {


    private EditText etAccount;
    private TextView txDriverName;
    private TextView txOverage;
    private EditText etMoney;
    private Button btnPayOne;
    private Button btnPayTwo;
    private Button btnPayThree;
    private Button btnPayFour;
    private Button btnManual;
    private Button btnScan;

    private Boolean isFromChangeBattery = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setTitle("账号充值");

    }

    @Override
    public int viewLayout() {
        return R.layout.payment_main;
    }

    @Override
    public void initView() {
        etAccount = (EditText) findViewById(R.id.et_account);
        btnManual = (Button) findViewById(R.id.btn_manual);
        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 获取按钮
                String account = etAccount.getText().toString();
                if(account.isEmpty()) {
                    toast( "帐号不能为空！");
                } else {
                    loadDriverInfo(account);
                }
            }
        });
//        etAccount.setText("2018041700000001");
        String account = getIntent().getStringExtra("account");
        if (account != null) {
            isFromChangeBattery = true;
            etAccount.setText(account);
            loadDriverInfo(account);
        }
        txDriverName = (TextView) findViewById(R.id.txDriverName);
        txOverage = (TextView) findViewById(R.id.txOverage); // 当前余额
        etMoney = (EditText) findViewById(R.id.etMoney);
        btnScan = (Button) findViewById(R.id.btn_scan);
        btnPayOne = (Button) findViewById(R.id.btnPayOne);
        btnPayOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMoney.setText("100");
            }
        });
        btnPayTwo = (Button) findViewById(R.id.btnPayTwo);
        btnPayTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMoney.setText("200");
            }
        });
        btnPayThree = (Button) findViewById(R.id.btnPayThree);
        btnPayThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMoney.setText("300");
            }
        });
        btnPayFour = (Button) findViewById(R.id.btnPayFour);
        btnPayFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMoney.setText("500");
            }
        });


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toScan = new Intent(getApplicationContext(), QRScanActivity.class);
                toScan.putExtra("type", "carNo");
                startActivityForResult(toScan, 1);
            }
        });
        setupBackBtn();
    }

    private void loadDriverInfo(String account) {
        btnManual.setEnabled(false);
        SGHTTPManager.POST(API.URL_DRIVER_INFO, new URLParams("account", account), new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                btnManual.setEnabled(true);
                if (isSuccess) {
                    txDriverName.setText(fetchModel.getSting("name"));
                    txOverage.setText(fetchModel.getSting("balance"));
                } else {
                    toast(userInfo);
                }
            }
        });
    }

    /**
     * 收款
     * @param payIndex 1:alipay 2:wechatPay
     */
    private void toPayRQCode(int payIndex){
        String totalAmount = etMoney.getText().toString();
        double money = -1;
        try {
            money = Double.parseDouble(totalAmount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (money < 100) {
            Toast.makeText(getApplicationContext(), "金额必须大于或等于100！", Toast.LENGTH_LONG).show();
        } else {
            String overage = txOverage.getText().toString();
            String account = etAccount.getText().toString();
            if (!account.isEmpty() && !overage.isEmpty()) {
                Intent showQRCode = new Intent(getApplicationContext(), PayQRCodeActivity.class);
                showQRCode.putExtra("account", etAccount.getText().toString());
                showQRCode.putExtra("driverName", txDriverName.getText().toString());
                showQRCode.putExtra("chargeAmount", etMoney.getText().toString());
                showQRCode.putExtra("totalAmount", overage);
//                SharedPreferences preferences = SharedPreferencesUtil.getSharedPreferences(mContext);
//                String station = preferences.getString(MainActivity.BatteryStorageStationKey,"");
                showQRCode.putExtra("payIndex",payIndex);
                showQRCode.putExtra("isFromChangeBattery",isFromChangeBattery);
                showQRCode.putExtra("isPackage", false);
                showQRCode.putExtra("tradeRemark", "");
                startActivity(showQRCode);
            } else {
                ToastUtil.showToast(getApplicationContext(), "账号不能为空");
            }
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public void onClickPay(View v){
        int payCode = 0;
        if (v.getId() == R.id.wxPayLayout) payCode = 1;
        toPayRQCode(payCode);
    }

    /**
     * 二维码识别结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String account = data.getStringExtra(QRScanActivity.kValue);
            etAccount.setText(account);
            loadDriverInfo(account);
        } else {
            toast("二维码信息识别失败！");
        }
    }


}
