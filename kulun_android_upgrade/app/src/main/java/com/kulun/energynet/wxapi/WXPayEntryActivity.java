package com.kulun.energynet.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.kulun.energynet.bill.BillActivity;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.utils.Utils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, UserLogin.getInstance().getWxappid());
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode){
            case 0://支付成功
//                EventBus.getDefault().post("rechargesuccess");
                Utils.snackbar(WXPayEntryActivity.this, "支付成功");
                Intent intent = new Intent(WXPayEntryActivity.this, BillActivity.class);
                startActivity(intent);
                finish();
                break;
            case -1://其他错误
            case -2:////用户取消支付后的界面
                Utils.snackbar(WXPayEntryActivity.this, "支付失败");
                finish();
                break;
            default:
                finish();
                break;
        }
    }

}
