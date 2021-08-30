package com.btkj.millingmachine.rechargequery;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityAccountBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.model.VipInquire.VipInquireModel;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;

/**
 * created by xuedi on 2019/5/4
 */
public class RechargeAccountActivity extends BaseActivity implements View.OnClickListener, TimeFinishInterface {
    private ActivityAccountBinding binding;
    private TimeCount timeCount;
    private VipInquireModel vipInquireModel;
    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        binding.center.back.setOnClickListener(this);
        binding.account100.setOnClickListener(this);
        binding.account200.setOnClickListener(this);
        binding.account300.setOnClickListener(this);
        binding.chongzhi.setOnClickListener(this);
        binding.img0.setOnClickListener(this);
        binding.img1.setOnClickListener(this);
        binding.img2.setOnClickListener(this);
        binding.img3.setOnClickListener(this);
        binding.img4.setOnClickListener(this);
        binding.img5.setOnClickListener(this);
        binding.img6.setOnClickListener(this);
        binding.img7.setOnClickListener(this);
        binding.img8.setOnClickListener(this);
        binding.img9.setOnClickListener(this);
        binding.shanchu.setOnClickListener(this);
        binding.qingchuquanbu.setOnClickListener(this);
        vipInquireModel = (VipInquireModel) getIntent().getSerializableExtra("vip");
        binding.name.setText("会员姓名: "+vipInquireModel.getData().getName());
        binding.phone.setText("手机号码: "+vipInquireModel.getData().getPhone());
        binding.money.setText(" "+vipInquireModel.getData().getBalance());
        binding.discount.setText(" "+vipInquireModel.getData().getDiscount());
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(120000, 1000, binding.center.time, this);
        timeCount.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Utils.toMain(RechargeAccountActivity.this);
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
            case R.id.shanchu:
                if (!money.isEmpty()) {
                    money = money.substring(0, money.length() - 1);
                    binding.tvMoney.setText(money);
                }
                break;
            case R.id.qingchuquanbu:
                money = "";
                binding.tvMoney.setText(money);
                break;
            case R.id.account100:
                money = "100";
                binding.tvMoney.setText(money);
                break;
            case R.id.account200:
                money = "200";
                binding.tvMoney.setText(money);
                break;
            case R.id.account300:
                money = "300";
                binding.tvMoney.setText(money);
                break;
            case R.id.chongzhi:
                if (binding.tvMoney.getText().toString() == null || binding.tvMoney.getText().toString().isEmpty() || binding.tvMoney.getText().toString().equals("0")){
                    Utils.toast(RechargeAccountActivity.this, "请输入金额");
                    return;
                }
                Intent intent = new Intent(RechargeAccountActivity.this, RechargeWayActivity.class);
                intent.putExtra("carid", getIntent().getStringExtra("carid"));
                intent.putExtra("money", money);
                intent.putExtra("isCard", getIntent().getBooleanExtra("isCard", false));
                intent.putExtra("phone", getIntent().getStringExtra("phone"));
                intent.putExtra("code", getIntent().getStringExtra("code"));
//                intent.putExtra("vip", vipInquireModel);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    /**
     *   intent.putExtra("carid", String.valueOf(strlog));
     * //        intent.putExtra("money", money);
     *         intent.putExtra("isCard", iscard);
     *         intent.putExtra("phone", phone);
     *         intent.putExtra("code", binding.yanzhengma.getText().toString());
     *         intent.putExtra("vip", vipInquireModel);
     */

    private String money = "";
    private void selectInput(String value) {
        money = money + value;
        binding.tvMoney.setText(money);
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

    @Override
    public void timeFinish() {
        Utils.toMain(this);
    }

    //初始化头部和底部
    @Override
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null){
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
//            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("服务电话："+config.getServiceCall());
        }
    }
}
