package com.kulun.energynet.mine;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.bill.BillActivity;
import com.kulun.energynet.databinding.ActivityDetailBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.main.MainActivity;
import com.kulun.energynet.model.Activity;
import com.kulun.energynet.model.ActivityDetail;
import com.kulun.energynet.model.UseBind;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.popup.PromotionPopup;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.DateUtils;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.JsonSplice;
import com.kulun.energynet.utils.Utils;

import java.util.HashMap;

/**
 * created by xuedi on 2019/9/6
 */
public class MyActivityDetailActivity extends BaseActivity implements View.OnClickListener, ChongdianCarConfirm {
    private Activity promotions;
    private ActivityDetailBinding binding;
    private PromotionPopup promotionPopup;
    private UseBind useBind;
    private ActivityDetail activityDetail;

    //    private PayPopup payPopup;
//    private double money;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        promotions = (Activity) getIntent().getSerializableExtra("data");
        binding.header.left.setOnClickListener(view -> finish());
        binding.header.title.setText("活动详情");
        binding.tvName.setText(promotions.getName());
        binding.tvContent.setText(promotions.getRemark());
        binding.tvTime.setText(promotions.getStartTime() + "至" + promotions.getEndTime());
        binding.include.finish.setText("立即参加");
        if (promotions.getPhoto() != null && !promotions.getPhoto().equals("")) {
            Glide.with(MyActivityDetailActivity.this).load(promotions.getPhoto()).into(binding.image);
        }
        if (promotions.getType() == 7) {
            binding.include.finish.setVisibility(View.VISIBLE);
        } else {
            binding.include.finish.setVisibility(View.GONE);
        }
        promotionPopup = new PromotionPopup(MyActivityDetailActivity.this);
//        payPopup = new PayPopup(this);
        carSelectorDialogFragment = new CarSelectorDialogFragment();
        useBind = Utils.getusebind(false, null, this);
        if (useBind != null && useBind.getId() != 0) {
            promotionPopup.carplate.setText(useBind.getPlate_number() + "");
            loadPackageDetail(promotions.getId(), useBind.getId());//如果当前有绑定车辆  就用当前车辆请求
        }
        binding.include.finish.setOnClickListener(v -> {
            if (useBind == null){
                Utils.snackbar(MyActivityDetailActivity.this, "没有车不能参加活动");
                return;
            }
            if (useBind.getId() == 0){
                Utils.snackbar(MyActivityDetailActivity.this, "没有车不能参加活动");
                return;
            }
            joinActivity();
        });
        initProp();
//        initPayPopup();
    }

    private void initProp() {
        promotionPopup.re.setOnClickListener(this);
        promotionPopup.add.setOnClickListener(this);
        promotionPopup.minus.setOnClickListener(this);
        promotionPopup.buy.setOnClickListener(this);
        promotionPopup.now.setOnClickListener(this);
        promotionPopup.next.setOnClickListener(this);
    }

//    private void initPayPopup() {
//        payPopup.tv_weixing.setOnClickListener(this);
//        payPopup.tv_alipay.setOnClickListener(this);
//    }

    private void loadPackageDetail(int activityid, int bindId) {
        //    "activityId": 530,
        //    "bindId": 15679
        HashMap<String, String> map = new HashMap<>();
        map.put("activityId", String.valueOf(activityid));
        map.put("bindId", String.valueOf(bindId));
        new MyRequest().myRequest(API.activitydetail, true, map, false, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                activityDetail = GsonUtils.getInstance().fromJson(json, ActivityDetail.class);
                Utils.snackbar(MyActivityDetailActivity.this, activityDetail.getReason() + "");
                initRadioBUtton(activityDetail);
                if (!activityDetail.isFlag()) {  //false 表示不可以进行活动
                    promotionPopup.buy.setClickable(false);
                    promotionPopup.buy.setBackgroundColor(getResources().getColor(R.color.gray));
//                    promotionPopup.tv_precaution_add.setText(carContent.getReason() + "");
                } else {
                    promotionPopup.buy.setClickable(true);
                    promotionPopup.buy.setBackgroundColor(getResources().getColor(R.color.text_color));
//                    promotionPopup.tv_precaution_add.setText("");
                }
            }
        });
    }

    private int isStartThisMonth = 0; //点击的radiobutton,0这个月，1下个月
    private int isOnlyStartNextMonth = 0; //得到的月份，0这个月开始，1下个月开始

    //初始化radiobutton
    private void initRadioBUtton(ActivityDetail carContent) {
        if (carContent.isBuyCurrMonth()) {
            promotionPopup.now.setEnabled(true);
            promotionPopup.now.setSelected(true);
            promotionPopup.next.setEnabled(true);
            isOnlyStartNextMonth = 0;
            clickNow(carContent);
        } else {
            promotionPopup.now.setEnabled(false);
            promotionPopup.next.setEnabled(true);
            promotionPopup.next.setSelected(true);
            isOnlyStartNextMonth = 1;
            clickNext(carContent);
        }
    }

    private int startYear, startMonth;
    private int monthNum = 1;

    //点击这个月的button
    private void clickNow(ActivityDetail carContent) {
        int year = DateUtils.stampToYears(System.currentTimeMillis());
        int month = DateUtils.stampToMonth(System.currentTimeMillis());
        startYear = year;
        startMonth = month;
        monthNum = 1;
        isStartThisMonth = 0;
        initpopview(startYear, startMonth, carContent);
        if (!carContent.isFlag()) {  //false 表示不可以进行活动
            promotionPopup.buy.setClickable(false);
            promotionPopup.buy.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
            promotionPopup.buy.setClickable(true);
            promotionPopup.buy.setBackgroundColor(getResources().getColor(R.color.text_color));
        }
    }

    //点击下个月的radiobutton
    private void clickNext(ActivityDetail carContent) {
        monthNum = 1;
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
        isStartThisMonth = 1;
        initpopview(startYear, startMonth, carContent);
        if (!carContent.isFlag()) {  //false 表示不可以进行活动
            promotionPopup.buy.setClickable(false);
            promotionPopup.buy.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
            if (monthNum + 1 - isOnlyStartNextMonth > carContent.getMaxMonths()) {
                promotionPopup.buy.setBackgroundColor(getResources().getColor(R.color.gray));
                promotionPopup.buy.setClickable(false);
            } else {
                promotionPopup.buy.setClickable(true);
                promotionPopup.buy.setBackgroundColor(getResources().getColor(R.color.text_color));
            }
        }
    }

    private void initpopview(int year, int month, ActivityDetail carContent) {
        promotionPopup.monthnum.setText(monthNum + "个月");
        if (carContent != null && carContent.getCurrMonthDiscountPrice() != 0 && carContent.isBuyCurrMonth()) {
            if (monthNum == 1) {
                promotionPopup.info.setText(carContent.getCurrMonthDiscountPrice() + "元x" + monthNum + "个月");
                promotionPopup.money.setText(carContent.getCurrMonthDiscountPrice() * monthNum + "元");
            } else {
                int monthDay = monthNum - 1;
                promotionPopup.info.setText("(" + carContent.getCurrMonthDiscountPrice() + "x1) + (" + carContent.getPrice() + "x" + monthDay + ")=");
                double first = carContent.getCurrMonthDiscountPrice() * 1;
                double second = carContent.getPrice() * monthDay;
                double resoult = first + second;
                promotionPopup.money.setText(resoult + "元");
            }
            if (isStartThisMonth == 0){//点击的radiobutton,0这个月，1下个月
                promotionPopup.time.setText(DateUtils.timeToDate(carContent.getStartDate()) + "~~" + getNextYearAndMonth(year, month, monthNum));
            }else {
                promotionPopup.time.setText(DateUtils.getFirstDayOfMonth(year, month) + "~~" + getNextYearAndMonth(year, month, monthNum));
            }
        } else {
            promotionPopup.info.setText(carContent.getPrice() + "元x" + monthNum + "个月");
            promotionPopup.money.setText(carContent.getPrice() * monthNum + "元");
            promotionPopup.time.setText(DateUtils.getFirstDayOfMonth(year, month) + "~~" + getNextYearAndMonth(year, month, monthNum));
        }
    }

    private String getNextYearAndMonth(int year, int month, int monthnum) {
        int yearnext = (month + monthnum - 1) / 12;
        int monthnext = (month + monthnum - 1) % 12;
        double a = (month + monthnum - 1) / 12.0;
        if (a <= 1.0) {
            return year + "年" + (month + monthnum - 1) + "月底";
//            return DateUtils.getLastDayOfMonth(year, month + monthnum - 1);
        } else {
            if (monthnext == 0) {
                return year + yearnext + "年" + "12月底";
//                return DateUtils.getLastDayOfMonth(year + yearnext, 12);
            }
            return year + yearnext + "年" + monthnext + "月底";
//            return DateUtils.getLastDayOfMonth(year + yearnext, monthnext);
        }
    }

    private void joinActivity() {//参加活动
        promotionPopup.showPopupWindow();
    }

    private CarSelectorDialogFragment carSelectorDialogFragment;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re://车辆选择
//                Bundle bundle = new Bundle();
//                carSelectorDialogFragment.setArguments(bundle);
                carSelectorDialogFragment.show(getSupportFragmentManager(), "data");
                break;
            case R.id.add:
                if (monthNum + isStartThisMonth - isOnlyStartNextMonth >= activityDetail.getMaxMonths()) {
//                    Utils.snackbar(this, "购买周期不能超过活动限制", Toast.LENGTH_LONG).show();
                    return;
                }
                if (monthNum >= activityDetail.getMaxMonths()) {
                    Toast.makeText(this, "购买周期不能超过" + activityDetail.getMaxMonths() + "个月", Toast.LENGTH_LONG).show();
                    return;
                }
                monthNum = monthNum + 1;
                initpopview(startYear, startMonth, activityDetail);
                break;
            case R.id.minus:
                if (monthNum == 1) {
                    Toast.makeText(this, "购买周期不能再减小了", Toast.LENGTH_LONG).show();
                    return;
                }
                monthNum = monthNum - 1;
                initpopview(startYear, startMonth, activityDetail);
                break;
            case R.id.buy:
                if (useBind == null || useBind.getId() == 0){
                    Utils.snackbar(MyActivityDetailActivity.this, "购买活动套餐必须绑定车辆哦");
                    return;
                }
                buypackage();
                break;
            case R.id.now:
                clickNow(activityDetail);
                promotionPopup.now.setSelected(true);
                promotionPopup.next.setSelected(false);
                break;
            case R.id.next:
                clickNext(activityDetail);
                promotionPopup.now.setSelected(false);
                promotionPopup.next.setSelected(true);
                break;
            default:
                break;
        }
    }

    private void buypackage() {//开始购买套餐
        String money = promotionPopup.money.getText().toString();
        String amount = money.replaceAll("元", "");
        if (amount == null && amount.equals("")) {
            Utils.snackbar(MyActivityDetailActivity.this, "当前购买金额为0");
            return;
        }
        String month;
        if (startMonth < 10) {
            month = "0" + startMonth;
        } else {
            month = String.valueOf(startMonth);
        }
//        private int isStartThisMonth = 0; //点击的radiobutton,0这个月，1下个月
        String startDate;
        if(isStartThisMonth == 0){
            startDate = DateUtils.timeToDate(activityDetail.getStartDate());
        }else {
            startDate = startYear + "-" + month + "-01";
        }
        //    private int isStartThisMonth = 0; //点击的radiobutton,0这个月，1下个月
        boolean isbuycurrmonth;
        if (isStartThisMonth == 0){
            isbuycurrmonth = true;
        }else {
            isbuycurrmonth = false;
        }
        String json = JsonSplice.leftparent + JsonSplice.yin + "activityId" + JsonSplice.yinandmao + promotions.getId() + JsonSplice.dou +
                JsonSplice.yin + "bindId" + JsonSplice.yinandmao + useBind.getId() + JsonSplice.dou +
                JsonSplice.yin + "buyMonths" + JsonSplice.yinandmao + monthNum + JsonSplice.dou +
                JsonSplice.yin + "buyCurrMonth" + JsonSplice.yinandmao + isbuycurrmonth + JsonSplice.dou +
                JsonSplice.yin + "startDate" + JsonSplice.yinandmao + JsonSplice.yin + startDate + JsonSplice.yinanddou +
                JsonSplice.yin + "amount" + JsonSplice.yinandmao + amount + JsonSplice.rightparent;
        new MyRequest().spliceJson(API.buypackage, true, json, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                Utils.snackbar(MyActivityDetailActivity.this, "购买套餐成功");
                promotionPopup.dismiss();
                Intent intent = new Intent(MyActivityDetailActivity.this, BillActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void click(String plateNum, int bindId) {
        promotionPopup.carplate.setText(plateNum + "");
        promotionPopup.next.setSelected(false);
        promotionPopup.now.setSelected(false);
        loadPackageDetail(promotions.getId(), bindId);
    }
}
