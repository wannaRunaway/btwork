package com.botann.driverclient.wxapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.ui.activity.BaseActivity;
import com.botann.driverclient.ui.activity.MainActivity;
import com.botann.driverclient.utils.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements View.OnClickListener,IWXAPIEventHandler {

    private TextView mRechargeComplete;
    private TextView mToRechargeRecord;
    private TextView mRechargeInfo;
    private TextView mRechargeBack;

    private Context mContext;
    private static WXPayEntryActivity mInstance = null;

    private IWXAPI api;

    public static WXPayEntryActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInstance = this;
        setContentView(R.layout.pay_result);
        bindView();

        api = WXAPIFactory.createWXAPI(this, Constants.wx_appid);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    public void bindView() {
        mRechargeBack = (TextView) this.findViewById(R.id.recharge_back);
        mRechargeComplete = (TextView) this.findViewById(R.id.recharge_complete);
        mToRechargeRecord = (TextView) this.findViewById(R.id.to_recharge_record);
        mRechargeInfo = (TextView) this.findViewById(R.id.recharge_info);

        mRechargeBack.setOnClickListener(this);
        mRechargeComplete.setOnClickListener(this);
        mToRechargeRecord.setOnClickListener(this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        System.out.println(baseResp.errCode);
        if(baseResp.errCode == 0) {
            //
        } else {
            rechargeFail(baseResp.errCode);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_complete:
                Constants.whichFragment = 0;
                toMainActivity();
                break;

            case R.id.to_recharge_record:
                Constants.whichFragment = 2;
                toMainActivity();
                break;

            case R.id.recharge_back:
                finish();
                break;

            default:
                break;
        }

    }

    public void rechargeFail(Integer errCode) {
        mRechargeBack.setVisibility(View.VISIBLE);
        mRechargeBack.setClickable(true);
        if(errCode == -1) {
            mRechargeInfo.setText("充值失败");
            Drawable drawable = getResources().getDrawable(R.drawable.recharge_fail);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mRechargeInfo.setCompoundDrawables(drawable, null, null, null);
            mToRechargeRecord.setText("请联系各站点，或去站点进行充值");
            mToRechargeRecord.setVisibility(View.VISIBLE);
            mToRechargeRecord.setClickable(false);
        } else if(errCode == -2) {
            mRechargeInfo.setText("用户取消充值");
            Drawable drawable = getResources().getDrawable(R.drawable.recharge_fail);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mRechargeInfo.setCompoundDrawables(drawable, null, null, null);
            mToRechargeRecord.setVisibility(View.INVISIBLE);
            mToRechargeRecord.setClickable(false);
        }
    }

    public void toMainActivity() {
        Intent message = new Intent(MainApp.getInstance().getApplicationContext(), MainActivity.class);
        message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MainApp.getInstance().getApplicationContext().startActivity(message);
    }

}
