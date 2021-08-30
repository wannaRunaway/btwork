package com.btkj.millingmachine.rechargequery;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityRechargeQueryBinding;
import com.btkj.millingmachine.databinding.DialogPayAmountBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.SoundPlayUtils;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;

/**
 * created by xuedi on 2019/5/4
 */
public class RechargeQueryActivity extends BaseActivity implements View.OnClickListener, TimeFinishInterface {
    private ActivityRechargeQueryBinding binding;
    private DialogPayAmountBinding dialogbinding;
    private AlertDialog dialog;
    private TimeCount timeCount;

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recharge_query);
        binding.layoutBack.imgBack.setOnClickListener(this);
        binding.account100.setOnClickListener(this);
        binding.account200.setOnClickListener(this);
        binding.account500.setOnClickListener(this);
        binding.accountOthers.setOnClickListener(this);
        binding.balanceInquire.setOnClickListener(this);
        initDialog();
        SoundPlayUtils.play(4);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(60000, 1000, binding.includeHeader.tvTime, this);
        timeCount.start();
    }

    private String phone = "";

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.account100:
                intent = new Intent(this, RechargeCardPhoneActivity.class);
                intent.putExtra("money", String.valueOf(100));
                startActivity(intent);
                break;
            case R.id.account200:
                intent = new Intent(this, RechargeCardPhoneActivity.class);
                intent.putExtra("money", String.valueOf(200));
                startActivity(intent);
                break;
            case R.id.account500:
                intent = new Intent(this, RechargeCardPhoneActivity.class);
                intent.putExtra("money", String.valueOf(500));
                startActivity(intent);
                break;
            case R.id.account_others:
                dialog.show();
                timeCount.cancel();
                timeCount = new TimeCount(60000, 1000, binding.includeHeader.tvTime, this);
                timeCount.start();
                SoundPlayUtils.play(7);
                break;
            case R.id.balance_inquire:
                intent = new Intent(this, AccountInquireActivity.class);
                startActivity(intent);
                break;
            case R.id.img0:
                selectInput("0");
                break;
            case R.id.img1:
                selectInput("1");
                break;
            case R.id.img2:
                selectInput("2");
                break;
            case R.id.img3:
                selectInput("3");
                break;
            case R.id.img4:
                selectInput("4");
                break;
            case R.id.img5:
                selectInput("5");
                break;
            case R.id.img6:
                selectInput("6");
                break;
            case R.id.img7:
                selectInput("7");
                break;
            case R.id.img8:
                selectInput("8");
                break;
            case R.id.img9:
                selectInput("9");
                break;
            case R.id.img_clear:
                phone = "";
                dialogbinding.tvPhone.setText(phone);
                break;
            case R.id.img_confirm:
                intent = new Intent(this, RechargeCardPhoneActivity.class);
                intent.putExtra("money", String.valueOf(Integer.parseInt(phone)));
                startActivity(intent);
                break;
            case R.id.img_close:
                if (!phone.isEmpty()) {
                    phone = phone.substring(0, phone.length() - 1);
                    dialogbinding.tvPhone.setText(phone);
                }
                break;
            default:
                break;
        }
    }

    private void selectInput(String value) {
        phone = phone + value;
        dialogbinding.tvPhone.setText(phone);
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pay_amount, null);
        dialogbinding = DataBindingUtil.bind(view);
        dialogbinding.img0.setOnClickListener(this);
        dialogbinding.img1.setOnClickListener(this);
        dialogbinding.img2.setOnClickListener(this);
        dialogbinding.img3.setOnClickListener(this);
        dialogbinding.img4.setOnClickListener(this);
        dialogbinding.img5.setOnClickListener(this);
        dialogbinding.img6.setOnClickListener(this);
        dialogbinding.img7.setOnClickListener(this);
        dialogbinding.img8.setOnClickListener(this);
        dialogbinding.img9.setOnClickListener(this);
        dialogbinding.imgClear.setOnClickListener(this);
        dialogbinding.imgConfirm.setOnClickListener(this);
        dialogbinding.tvPhone.setOnClickListener(this);
        dialogbinding.imgClose.setOnClickListener(this);
        builder.setView(view);
        dialog = builder.create();
    }

    @Override
    public void timeFinish() {
        Utils.toMain(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeCount.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
    }

    //初始化头部和底部
    @Override
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null) {
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.includeHeader.imgTopIcon);
            }
            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.layoutBottom.tvPhone.setText("客服电话：" + config.getServiceCall());
        }
    }
}
