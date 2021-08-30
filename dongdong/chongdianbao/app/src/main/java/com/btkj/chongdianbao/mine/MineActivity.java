package com.btkj.chongdianbao.mine;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityMineBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.model.Informodel;
import com.btkj.chongdianbao.model.User;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.BaseDialog;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.FormatUtil;
import com.btkj.chongdianbao.utils.GsonUtils;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.QRCodeUtil;
import com.btkj.chongdianbao.utils.SharePref;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;


public class MineActivity extends BaseActivity implements View.OnClickListener {
    private ActivityMineBinding binding;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mine);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        bindClick();
        binding.tvName.setText(TextUtils.isEmpty(User.getInstance().getName()) ? "我" : User.getInstance().getName());
        binding.imgQrcode.setImageBitmap(QRCodeUtil.createQRCode(API.qrinfo+String.valueOf(User.getInstance().getAccountNo()), dip2px(MineActivity.this,60), dip2px(MineActivity.this,60)));
        if(User.getInstance().getBalance()>0.f){
            binding.tvYue.setText("余额：" + FormatUtil.format(User.getInstance().getBalance()));
        }else {
            binding.tvYue.setText("余额：0.00");
        }
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String s){
        if(!TextUtils.isEmpty(s)&&s.equals("rechargesuccess")){
            loadData();
        }
        if(!TextUtils.isEmpty(s)&&s.equals("readed")){
            binding.ivMsg.setImageResource(R.mipmap.icon_nomalmsg);
        }
    }


    private void finishRefresh() {
        binding.smartRefresh.finishRefresh();
        binding.smartRefresh.finishLoadMore();
    }

    private void loadData() {
        String url = API.BASE_URL + API.INFO;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, MineActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                Informodel informodel = GsonUtils.getInstance().fromJson(json, Informodel.class);
                User.getInstance().setId(informodel.getContent().getId());
                User.getInstance().setName(informodel.getContent().getName());
                User.getInstance().setPhone(informodel.getContent().getPhone());
                User.getInstance().setAccountNo(informodel.getContent().getAccountNo());
                User.getInstance().setBalance(informodel.getContent().getBalance());
                User.getInstance().setNoReaded(informodel.getContent().getNoReaded());
                if(informodel.getContent().getNoReaded()>0){
                    binding.ivMsg.setImageResource(R.mipmap.icon_redmsg);
                }else {
                    binding.ivMsg.setImageResource(R.mipmap.icon_nomalmsg);
                }
                binding.tvName.setText(User.getInstance().getName().isEmpty() ? "我" : User.getInstance().getName());
                binding.imgQrcode.setImageBitmap(QRCodeUtil.createQRCode(API.qrinfo+String.valueOf(User.getInstance().getAccountNo()), dip2px(MineActivity.this,60), dip2px(MineActivity.this,60)));
                if(User.getInstance().getBalance()>0.f){
                    binding.tvYue.setText("余额：" + FormatUtil.format(User.getInstance().getBalance()));
                }else {
                    binding.tvYue.setText("余额：0.00");
                }
            }
        }, binding.smartRefresh);
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void bindClick() {
        binding.imgLeft.setOnClickListener(this);
        binding.ivMsg.setOnClickListener(this);
        binding.imgRecharge.setOnClickListener(this);
        binding.reMycar.setOnClickListener(this);
        binding.reMyYuyue.setOnClickListener(this);
        binding.reMyInvite.setOnClickListener(this);
        binding.reYouhuiquan.setOnClickListener(this);
        binding.reXiaofeijilu.setOnClickListener(this);
        binding.reChongzhijilu.setOnClickListener(this);
        binding.reActivity.setOnClickListener(this);
        binding.reShezhi.setOnClickListener(this);
        binding.imgQrcode.setOnClickListener(this);
        binding.reExchange.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.img_left:
                finish();
                break;
            case R.id.iv_msg:
                intent = new Intent(MineActivity.this, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.img_recharge:
                intent = new Intent(MineActivity.this, RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.re_mycar:
                intent = new Intent(MineActivity.this, MyCarActivity.class);
                startActivity(intent);
                break;
            case R.id.re_my_yuyue:
                intent = new Intent(MineActivity.this, ReserveActivity.class);
                startActivity(intent);
                break;
            case R.id.re_my_invite:
                intent = new Intent(MineActivity.this, MyInviteActivity.class);
                startActivity(intent);
                break;
            case R.id.re_youhuiquan:
                intent = new Intent(MineActivity.this, CouponActivity.class);
                startActivity(intent);
                break;
            case R.id.re_exchange:
                int carId=(int) SharePref.get(MineActivity.this, API.carId, 0);
                if(carId!=0){
                    intent = new Intent(MineActivity.this, ExchangeActivity.class);
                    startActivity(intent);
                }else {
                    Utils.snackbar(getApplicationContext(),MineActivity.this,"请在首页选择您的车辆，才能进行换班打卡！");
                }
                break;
            case R.id.re_chongzhijilu:
                intent = new Intent(MineActivity.this, RechargeRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.re_xiaofeijilu:
                intent = new Intent(MineActivity.this, ConsumeActivity.class);
                startActivity(intent);
                break;
            case R.id.re_activity:
                intent = new Intent(MineActivity.this, MyActivityListActivity.class);
                startActivity(intent);
                break;
            case R.id.re_shezhi:
                intent = new Intent(MineActivity.this, SystemSettingActivity.class);
                //intent.putExtra(API.apppath, apkPath);
                startActivity(intent);
                break;
            case R.id.img_qrcode:
                BaseDialog.showImageDialog(MineActivity.this);
                break;
            default:
                break;
        }
    }
}
