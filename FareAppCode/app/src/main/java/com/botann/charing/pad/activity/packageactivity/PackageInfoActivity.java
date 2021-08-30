package com.botann.charing.pad.activity.packageactivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.databinding.DataBindingUtil;

import com.botann.charging.pad.R;
import com.botann.charging.pad.databinding.LayoutPackageInfoBinding;
import com.botann.charing.pad.activity.BaseActivity;
import com.botann.charing.pad.activity.Pay.PayQRCodeActivity;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.components.zxing.decode.Utils;
import com.botann.charing.pad.model.packageactivity.PackageActivityinfo;
import com.botann.charing.pad.model.packageactivity.PackageCarinfo;
import com.botann.charing.pad.model.packagedetail.PackageContent;
import com.botann.charing.pad.utils.DateUtils;
import com.botann.charing.pad.utils.ToastUtil;

/**
 * created by xuedi on 2019/3/14
 */
public class PackageInfoActivity extends BaseActivity implements View.OnClickListener {
    private LayoutPackageInfoBinding binding;
    private PackageActivityinfo packageActivityinfo;
    private int startYear, startMonth;
    private String account, overage, name;
    private Boolean isFromChangeBattery = false;
    private PackageCarinfo packageCarinfo;
    private Dialog payDialog;
    private int isStartThisMonth = 0; //点击的radiobutton,0这个月，1下个月
    private int isOnlyStartNextMonth = 0; //得到的月份，0这个月开始，1下个月开始

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_package_info);
        binding.btnLeft.setOnClickListener(this);
        binding.imageDecrease.setOnClickListener(this);
        binding.imageIncrease.setOnClickListener(this);
        binding.tvBuy.setOnClickListener(this);
        binding.rbMonthNext.setOnClickListener(this);
        binding.rbMonthThis.setOnClickListener(this);
        packageActivityinfo = (PackageActivityinfo) getIntent().getSerializableExtra("promotions");
        packageCarinfo = (PackageCarinfo) getIntent().getSerializableExtra("packageCarinfo");
        binding.tvTitle.setText(packageActivityinfo.getName() + " (" + DateUtils.stampToYear(packageActivityinfo.getStartTime()) + "~" + DateUtils.stampToYear(packageActivityinfo.getEndTime()) + ")");
        binding.tvUnitPrice.setText("￥" + packageActivityinfo.getPackagePrice());
        binding.tvCarnum.setText(getIntent().getStringExtra("carPlate"));
        account = getIntent().getStringExtra("account");
        if (account != null) {
            isFromChangeBattery = true;
            loadDriverInfo(account);
            loadPackageDetailInfo();
        }
        dialogInit();
        radioButtonInit();
    }

    //初始化radiobutton
    private void radioButtonInit() {
        if (packageCarinfo.isStartCurrMonth()) {
            binding.rbMonthThis.setEnabled(true);
            binding.rbMonthThis.setChecked(true);
            binding.rbMonthNext.setEnabled(true);
            isOnlyStartNextMonth = 0;
            clickThis();
        } else {
            binding.rbMonthThis.setEnabled(false);
            binding.rbMonthNext.setEnabled(true);
            binding.rbMonthNext.setChecked(true);
            isOnlyStartNextMonth = 1;
            clickNext();
        }
    }

    //微信和支付宝支付的选择框
    private void dialogInit() {
        payDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_normal, null);
        LinearLayout aliPay = (LinearLayout) view.findViewById(R.id.aliPayLayout);
        LinearLayout wxPay = (LinearLayout) view.findViewById(R.id.wxPayLayout);
        aliPay.setOnClickListener(this);
        wxPay.setOnClickListener(this);
        payDialog.setContentView(view);
        payDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    //点击按钮弹窗变化的view处理
    private int monthNum = 1;
    private void initView(int year, int month) {
        binding.tvMonthnum.setText(monthNum + "个月");
        binding.tvTime.setText(DateUtils.getFirstDayOfMonth(year, month)+ "~~" + getNextYearAndMonth(year, month, monthNum));
        binding.tvMoney.setText(packageActivityinfo.getPackagePrice() + "元x" + monthNum + "个月");
        binding.tvFinal.setText("￥" + packageActivityinfo.getPackagePrice() * monthNum);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.image_decrease://减小月份
                if (monthNum == 1) {
                    Toast.makeText(PackageInfoActivity.this, "购买周期不能再减小了", Toast.LENGTH_LONG).show();
                    return;
                }
                monthNum = monthNum - 1;
                initView(startYear, startMonth);
                break;
            case R.id.image_increase://增加月份
                if (monthNum + isStartThisMonth - isOnlyStartNextMonth >= packageActivityinfo.getMaxMonths()) {
                    Toast.makeText(PackageInfoActivity.this, "购买周期不能超过活动限制", Toast.LENGTH_LONG).show();
                    return;
                }
                monthNum = monthNum + 1;
                initView(startYear, startMonth);
                break;
            case R.id.rb_month_this://本月
                clickThis();
                break;
            case R.id.rb_month_next://下月
                clickNext();
                break;
            case R.id.tv_buy: //购买套餐
                chosePayment();
                break;
            case R.id.wxPayLayout:
                payDialog.dismiss();
                toPayRQCode(1);
                break;
            case R.id.aliPayLayout:
                payDialog.dismiss();
                toPayRQCode(0);
                break;
            default:
                break;
        }
    }
    //点击这个月的radiobutton
    private void clickThis() {
        int year = DateUtils.stampToYears(System.currentTimeMillis());
        int month = DateUtils.stampToMonth(System.currentTimeMillis());
        startYear = year;
        startMonth = month;
        monthNum = 1;
        initView(startYear, startMonth);
        binding.tvWarning.setVisibility(View.GONE);
        binding.tvBuy.setBackgroundDrawable(getResources().getDrawable(R.drawable.color_buypackage));
        binding.tvBuy.setClickable(true);
        isStartThisMonth = 0;
    }
    //点击下个月radiobutton
    private void clickNext() {
        monthNum = 1;
        if (monthNum+1-isOnlyStartNextMonth > packageActivityinfo.getMaxMonths()) {
            binding.tvWarning.setText("提示:购买周期超过了活动时间");
            binding.tvWarning.setVisibility(View.VISIBLE);
            binding.tvBuy.setBackgroundDrawable(getResources().getDrawable(R.drawable.promotion_bottom_dark));
            binding.tvBuy.setClickable(false);
            return;
        }
        binding.tvWarning.setVisibility(View.GONE);
        binding.tvBuy.setBackgroundDrawable(getResources().getDrawable(R.drawable.color_buypackage));
        binding.tvBuy.setClickable(true);
        int year_next = DateUtils.stampToYears(System.currentTimeMillis());
        int month_next = DateUtils.stampToMonth(System.currentTimeMillis());
        if (month_next > 11) {
            month_next = 1;
            year_next = year_next + 1;
        } else {
            month_next = month_next + 1;
        }
        startYear = year_next;
        startMonth = month_next;
        initView(startYear, startMonth);
        isStartThisMonth = 1;
    }

    //购买套餐付款界面
    private void chosePayment() {
        payDialog.show();
    }

    /*
        获取一段时间后的月份、年份
    */
    private String getNextYearAndMonth(int year, int month, int monthnum) {
        int yearnext = (month + monthnum - 1) / 12;
        int monthnext = (month + monthnum - 1) % 12;
        double a = (month + monthnum - 1) / 12.0;
        if (a <= 1.0) {
//            return year + "年" + (month + monthnum - 1) + "月底";
            return DateUtils.getLastDayOfMonth(year, month + monthnum - 1);
        } else {
            if (monthnext == 0) {
//                return year + yearnext + "年" + "12月底";
                return DateUtils.getLastDayOfMonth(year+yearnext, 12);
            }
            return DateUtils.getLastDayOfMonth(year+yearnext, monthnext);
        }
    }

    /**
     * 收款
     *
     * @param payIndex 1:alipay 2:wechatPay
     */
    private void toPayRQCode(int payIndex) {
        if (!account.isEmpty() && !overage.isEmpty()) {
            Intent showQRCode = new Intent(getApplicationContext(), PayQRCodeActivity.class);
            showQRCode.putExtra("account", account);
            showQRCode.putExtra("driverName", name);
            showQRCode.putExtra("chargeAmount", String.valueOf(packageActivityinfo.getPackagePrice() * monthNum));
            showQRCode.putExtra("totalAmount", overage);
            showQRCode.putExtra("payIndex", payIndex);
            showQRCode.putExtra("isFromChangeBattery", isFromChangeBattery);
            showQRCode.putExtra("isPackage", true);
            showQRCode.putExtra("activityName", packageActivityinfo.getName());
            showQRCode.putExtra("carplateNum", packageCarinfo.getCarNo());
            String stringMonth = String.valueOf(startMonth);
            if (stringMonth.length() < 2) {
                stringMonth = "0" + stringMonth;
            }
            showQRCode.putExtra("activityTime", binding.tvTime.getText().toString()+"");
            showQRCode.putExtra("tradeRemark", packageCarinfo.getCarTypeId() + "," + packageCarinfo.getCarId() + "," +
                    packageCarinfo.getCarNo() + "," + startYear + "-" + stringMonth + "-01" +
                    "," + monthNum + "," + packageActivityinfo.getId());
            startActivity(showQRCode);
        } else {
            ToastUtil.showToast(getApplicationContext(), "账号不能为空");
        }
    }

    //得到 余额和name
    private void loadDriverInfo(String account) {
        SGHTTPManager.POST(API.URL_DRIVER_INFO, new URLParams("account", account), new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess) {
                    name = fetchModel.getSting("name");
                    binding.tvDriverName.setText(name + "");
                    overage = fetchModel.getSting("balance");
                } else {
                    Toast.makeText(PackageInfoActivity.this, userInfo, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //得到套餐详情说明
    private void loadPackageDetailInfo() {
        URLParams urlParams = new URLParams();
        urlParams.put("activityId", packageActivityinfo.getId());
        SGHTTPManager.POST(API.URL_ACTIVITY_DETAIL, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess) {
                    PackageContent content = SGFetchModel.getGson().fromJson(fetchModel.getJsonObject().toString(), PackageContent.class);
                    binding.tvPackageDetail.setText(content.getRemark() + "");
                } else {
                    Toast.makeText(PackageInfoActivity.this, userInfo, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int viewLayout() {
        return R.layout.layout_package_info;
    }

    @Override
    public void initView() {
    }
}
