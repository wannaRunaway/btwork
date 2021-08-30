package com.kulun.energynet.mine;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.bill.BillActivity;
import com.kulun.energynet.databinding.ActivityMineBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.model.UseBind;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.BaseDialog;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.Utils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public class MineActivity extends BaseActivity implements View.OnClickListener {
    private ActivityMineBinding binding;
    private UseBind useBind;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mine);
        bindClick();
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        new MyRequest().myRequest(API.INFO, false, null, true, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                //        "name": "王霞",
                //        "balance": 700.14
                Utils.shareprefload(json, MineActivity.this);
                useBind = Utils.getusebind(true, json, MineActivity.this);
                if (useBind !=null && useBind.getId() != 0) {
                    binding.reCar.setVisibility(View.VISIBLE);
                    binding.name.setText(useBind.getPlate_number() + "");
                    if (useBind.getBind_status()==1){
                        Drawable drawable = getResources().getDrawable(R.mipmap.sign_binding);
                        drawable.setBounds(0, 0, 100, 50);
                        binding.name.setCompoundDrawables(null,null,drawable,null);
                    }else if (useBind.getBind_status()==3){
                        Drawable drawable = getResources().getDrawable(R.mipmap.sign_lock);
                        drawable.setBounds(0, 0, 100, 50);
                        binding.name.setCompoundDrawables(null,null,drawable,null);
                    }
                    binding.t1.setText(useBind.getSoc() + "%");
                    binding.t2.setText(useBind.getCar_type());
                    binding.t3.setText(useBind.isBattery_status() ? "正常" : "不正常");
                } else {
                    binding.reCar.setVisibility(View.INVISIBLE);
                }
                if (json != null) {
                    UserLogin userLogin = GsonUtils.getInstance().fromJson(json, UserLogin.class);
                    if (userLogin.getUnread() > 0){
                        binding.ivMsg.setImageDrawable(getResources().getDrawable(R.mipmap.mine_message_news));
                    }else {
                        binding.ivMsg.setImageDrawable(getResources().getDrawable(R.mipmap.mine_message_normal));
                    }
                    if (userLogin.getName() == null || userLogin.getName().equals("")) {
                        binding.tvName.setText("我");
                    }else {
                        binding.tvName.setText(userLogin.getName()+"");
                    }
                    if (userLogin.getBalance() > 0) {
                        binding.tvYue.setText("余额：" + userLogin.getBalance());
                    } else {
                        binding.tvYue.setText("余额：0.00");
                    }
                    BaseDialog.createQRCodeWithLogo(binding.imgQrcode, MineActivity.this, Utils.getAccount(MineActivity.this), 80);
                }
            }
        });
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void bindClick() {
        binding.imgQrcode.setOnClickListener(this);
        binding.imgLeft.setOnClickListener(this);
        binding.ivMsg.setOnClickListener(this);
        binding.recharge.setOnClickListener(this);
        binding.bill.setOnClickListener(this);
        binding.binding.setOnClickListener(this);
        binding.activity.setOnClickListener(this);
        binding.coupon.setOnClickListener(this);
        binding.reservation.setOnClickListener(this);
        binding.clock.setOnClickListener(this);
        binding.setting.setOnClickListener(this);
        binding.customer.setOnClickListener(this);
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
            case R.id.recharge:
                intent = new Intent(MineActivity.this, RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.binding:
                intent = new Intent(MineActivity.this, MyCarActivity.class);
                startActivity(intent);
                break;
            case R.id.reservation:
                intent = new Intent(MineActivity.this, ReserveActivity.class);
                startActivity(intent);
                break;
            case R.id.coupon:
                intent = new Intent(MineActivity.this, CouponActivity.class);
                startActivity(intent);
                break;
            case R.id.clock:
                if (Utils.usebindisNotexist(useBind)) {
                    Utils.snackbar(MineActivity.this, "抱歉，此功能只适用于出租车");
                    return;
                }
                if (useBind.getBusiness_type()!=5){//5是出租车司机
                    Utils.snackbar(MineActivity.this, "抱歉，此功能只适用于出租车");
                    return;
                }
                intent = new Intent(MineActivity.this, ExchangeActivity.class);
                startActivity(intent);
                break;
            case R.id.bill:
                intent = new Intent(MineActivity.this, BillActivity.class);
                startActivity(intent);
                break;
            case R.id.activity:
                intent = new Intent(MineActivity.this, MyActivityListActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                intent = new Intent(MineActivity.this, SystemSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.img_qrcode:
                String carplate = "";
                if (Utils.usebindisNotexist(useBind)){
                    carplate = "";
                }else {
                    carplate = useBind.getPlate_number();
                }
                BaseDialog.showQrDialog(MineActivity.this, carplate);
                break;
            case R.id.customer:
                Utils.loadkefu(MineActivity.this, binding.customer);
                break;
            default:
                break;
        }
    }
}
