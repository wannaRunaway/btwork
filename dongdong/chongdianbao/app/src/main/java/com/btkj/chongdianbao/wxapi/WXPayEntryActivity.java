package com.btkj.chongdianbao.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.btkj.chongdianbao.model.User;
import com.btkj.chongdianbao.utils.Utils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, User.getInstance().getWxappid());
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
                EventBus.getDefault().post("rechargesuccess");
                Utils.snackbar(getApplicationContext(), WXPayEntryActivity.this, "支付成功");
                finish();
                break;
            case -1://其他错误
            case -2:////用户取消支付后的界面
                Utils.snackbar(getApplicationContext(), WXPayEntryActivity.this, "支付失败");
                finish();
                break;
            default:
                finish();
                break;
        }
    }

}
