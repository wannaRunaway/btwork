package com.botann.charing.pad.activity.malfunctionrecord;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.botann.charging.pad.R;
import com.botann.charging.pad.databinding.ActivityRecordBinding;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.model.repairlist.RepairData;
import com.botann.charing.pad.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * created by xuedi on 2018/11/1
 * 维修记录详情
 */
public class RepairRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private RepairData repairData;
    private ActivityRecordBinding binding;
    private CustomDatePicker customDatePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_record);
        repairData = (RepairData) getIntent().getSerializableExtra("repairData");
        binding.btnLeft.setOnClickListener(this);
        binding.tvTime.setOnClickListener(this);
        binding.tvTime.setText(getDate());
        binding.tvMainTitle.setText("维修记录详情");
        binding.submit.setOnClickListener(this);
        initView();
    }

    private String getDate() {
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateformat.format(date);
    }

    /**
     * textView显示初始化
     */
    private void initView() {
        //1 已报修   2已维修   3已完成\已修复     4未修复
        if (repairData == null){
            Toast.makeText(RepairRecordActivity.this, "网络请求异常", Toast.LENGTH_LONG).show();
            return;
        }
        if (repairData.getStatus() == 2) {
            binding.liRepairdetail.setVisibility(View.VISIBLE);
            binding.liWeixiuQueren.setVisibility(View.VISIBLE);
            binding.submit.setVisibility(View.VISIBLE);
        } else if (repairData.getStatus() == 3) {
            binding.liRepairdetail.setVisibility(View.VISIBLE);
        }
        binding.tvFixed.setOnClickListener(this);
        binding.tvUnfix.setOnClickListener(this);
        binding.tvFixed.setSelected(true);
        if (repairData.getIdNumber().indexOf(";")!=-1){
            String s1 = repairData.getIdNumber().split(";")[0];
            String s2 = repairData.getIdNumber().split(";")[1];
            binding.tvJiankongpinCode.setText(s1+"");
            binding.tvJiankongpinBanben.setText(s2+"");
        }else {
            binding.tvJiankongpinCode.setText("");
            binding.tvJiankongpinBanben.setText("");
        }
        binding.tvRepairDate.setText(DateUtils.stampToDate(repairData.getFaultTime())+"");
        binding.tvRepairSuperson.setText(repairData.getFaultReportPersion()+"");
//        String siteName = getIntent().getStringExtra("stationName");
        binding.tvRepairSite.setText(repairData.getSiteName()+"");
//        binding.tvRepairPart.setText(repairData.getFaultClassificationZero());
        if (isEmpty(repairData.getDemageShelfNumber())) {
            binding.liShelf.setVisibility(View.GONE);
        } else {
            binding.tvRepairShelfcode.setText(repairData.getDemageShelfNumber()+"");
            binding.liShelf.setVisibility(View.VISIBLE);
        }
        if (isEmpty(repairData.getDemageWarehouseNumber())) {
            binding.liWarhouse.setVisibility(View.GONE);
        } else {
            binding.tvRepairWarhousecode.setText(repairData.getDemageWarehouseNumber()+"");
            binding.liWarhouse.setVisibility(View.VISIBLE);
        }
//        if (isEmpty(repairData.getIdNumber())){
//            binding.liId.setVisibility(View.GONE);
//        }else {
//            binding.tvRepairIDcode.setText(repairData.getIdNumber());
//            binding.liId.setVisibility(View.VISIBLE);
//        }
        if (isEmpty(repairData.getDemageCode())) {
            binding.liCoding.setVisibility(View.GONE);
        } else {
            binding.tvRepairCoding.setText(repairData.getDemageCode()+"");
            binding.liCoding.setVisibility(View.VISIBLE);
        }
        if (isEmpty(repairData.getSuperDemagePosition())) {
            binding.liSuperpart.setVisibility(View.GONE);
        } else {
            binding.tvRepairSuperpart.setText(repairData.getSuperDemagePosition()+"");
            binding.liSuperpart.setVisibility(View.VISIBLE);
        }
        if (repairData.getStatus() == 1) {
            binding.liRepairdetail.setVisibility(View.GONE);
        } else {
            binding.liRepairdetail.setVisibility(View.VISIBLE);
        }
        binding.tvRepairPhen.setText(repairData.getFaultPhenomenon()+"");
        binding.tvRepairLevel.setText(repairData.getFaultLevelString()+"");
        binding.tvRepairRedate.setText(DateUtils.stampToDate(repairData.getDealRepairTime())+"");
        binding.tvRepairPerson.setText(repairData.getRepairPersion()+"");
        binding.tvRepairContent.setText(repairData.getRepairContent()+"");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String now = sdf.format(new Date());
//                currentDate.setText(now.split(" ")[0]);
//        currentTime.setTextColor(getResources().getColor(R.color.color2));
        binding.tvTime.setText(now);
        Log.e("currentTime", now);
//                customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
//                    @Override
//                    public void handle(String time) { // 回调接口，获得选中的时间
//                        currentDate.setText(time.split(" ")[0]);
//                    }
//                }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
//                customDatePicker1.showSpecificTime(false); // 不显示时和分
//                customDatePicker1.setIsLoop(false); // 不允许循环滚动
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
//                        currentTime.setTextColor(getResources().getColor(R.color.color6));
                binding.tvTime.setText(time+":00");
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动

    }

    private boolean isEmpty(String string) {
        if (string == null || string.equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.submit:
                submit();
                break;
            case R.id.tv_fixed:
                binding.tvFixed.setSelected(true);
                binding.tvUnfix.setSelected(false);
                binding.tvFixed.setTextColor(getResources().getColor(R.color.white));
                binding.tvUnfix.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.tv_unfix:
                binding.tvFixed.setSelected(false);
                binding.tvUnfix.setSelected(true);
                binding.tvFixed.setTextColor(getResources().getColor(R.color.black));
                binding.tvUnfix.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_time:
//                SelectData selectData = new SelectData(RepairRecordActivity.this, true);
//                selectData.showAtLocation(binding.tvTime, Gravity.BOTTOM, 0, 0);
//                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
//                    @Override
//                    public void onClick(String year, String month, String day, String hour, String minute) {
//                        binding.tvTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute+":00");
//                    }
//                });
                customDatePicker.show(binding.tvTime.getText().toString());
                break;
            default:
                break;
        }
    }

    private void submit() {
        //id
        //[int]	是	维修id
        //confirmRepairTimeStr
        //[string]	是	确认时间	展开
        //faultConfirmPersion
        //[string]	是	确认人员
        //status
        //[int]	是	确认状态，3已修复，4未修复
        URLParams urlParams = new URLParams();
        urlParams.put("id", repairData.getId());
        if (binding.tvTime.getText().toString().equals("")){
            Toast.makeText(RepairRecordActivity.this, "确认时间不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (binding.tvPerson.getText().toString().equals("")){
            Toast.makeText(RepairRecordActivity.this, "确认人员不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        urlParams.put("confirmRepairTimeStr", binding.tvTime.getText().toString());
        urlParams.put("faultConfirmPersion", binding.tvPerson.getText().toString());
        if (binding.tvFixed.isSelected()){
            urlParams.put("status", 3);
        }else {
            urlParams.put("status", 4);
        }
        SGHTTPManager.POST(API.URL_REPAIR_CONFIRM, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess){
                    Toast.makeText(RepairRecordActivity.this, "上传完成", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                   Toast.makeText(RepairRecordActivity.this, userInfo, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
