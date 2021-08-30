package com.botann.charing.pad.activity.malfunctionrecord;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.botann.charging.pad.R;
import com.botann.charging.pad.databinding.ActivityMalfunctionrepairBinding;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.OnMultiClickListener;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.components.zxing.decode.Utils;
import com.botann.charing.pad.model.ExchangeSite;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.utils.GsonUtils;
import com.botann.charing.pad.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * created by xuedi on 2018/10/31
 * 故障维修
 */
public class MalfunctionRepairActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ActivityMalfunctionrepairBinding binding;
    private String superpart = "";
    private int stationID, level;
    private List<ExchangeSite> stationlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_malfunctionrepair);
        binding.btnLeft.setOnClickListener(this);
        binding.tvDateChoose.setOnClickListener(this);
        binding.tvDateChoose.setText(getDate());
        binding.tvMainTitle.setText("故障维修");
        binding.setType(1);
        String stations = getIntent().getStringExtra("station");
        stationlist = GsonUtils.getInstance().fromJson(stations, new TypeToken<List<ExchangeSite>>() {
        }.getType());
        initSpinner(stationlist);
        binding.btnRight.setOnClickListener(new OnMultiClickListener() {
            @Override
            protected void onMultiClick(View v) {
                if (binding.etMalfunctionpersonChoose.getText().toString().equals("")){
                    Toast.makeText(MalfunctionRepairActivity.this, "故障上报人不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (binding.etPhenChoose.getText().toString().equals("")){
                    Toast.makeText(MalfunctionRepairActivity.this, "故障现象不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (binding.tvDateChoose.getText().toString().equals("")){
                    Toast.makeText(MalfunctionRepairActivity.this, "保修时间不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (binding.etJiankongpingCode.getText().toString().equals("")){
                    Toast.makeText(MalfunctionRepairActivity.this, "监控屏编号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (binding.etJiankongpingBanben.getText().toString().equals("")){
                    Toast.makeText(MalfunctionRepairActivity.this, "监控屏版本号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (binding.etCodeShebei.getText().toString().equals("")){
                    Toast.makeText(MalfunctionRepairActivity.this, "设备编号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (binding.etCangweiBianhao.getText().toString().equals("")){
                    Toast.makeText(MalfunctionRepairActivity.this, "仓位编号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (binding.etGuzhangdaima.getText().toString().equals("")){
                    Toast.makeText(MalfunctionRepairActivity.this, "故障代码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                URLParams urlParams = new URLParams();
                urlParams.put("siteId", stationID);
                urlParams.put("faultClassificationZero", "");
                urlParams.put("faultLevel", level);
                urlParams.put("faultPhenomenon", binding.etPhenChoose.getText().toString());
                urlParams.put("faultReportPersion", binding.etMalfunctionpersonChoose.getText().toString());
                urlParams.put("operator", User.shared().getUsername());
                urlParams.put("faultTimeStr", binding.tvDateChoose.getText().toString()+":00");
                urlParams.put("IdNumber", binding.etJiankongpingCode.getText().toString()+";"+binding.etJiankongpingBanben.getText().toString());
                if (binding.getType() == 1) {
                    /**
                     * 选择充电架
                     */
                    urlParams.put("demageShelfNumber", binding.etCodeShebei.getText().toString());
                    urlParams.put("demageWarehouseNumber", binding.etCangweiBianhao.getText().toString());
//                    urlParams.put("IdNumber", idcode);
                    urlParams.put("demageCode", binding.etGuzhangdaima.getText().toString());
                    urlParams.put("superDemagePosition", "");
                } else if (binding.getType() == 2) {
                    /**
                     * 超级站自动化
                     */
                    urlParams.put("demageShelfNumber", "");
                    urlParams.put("demageWarehouseNumber", "");
//                    urlParams.put("IdNumber", "");
                    urlParams.put("demageCode", "");
                    urlParams.put("superDemagePosition", superpart);
                } else {
                    urlParams.put("demageShelfNumber", "");
                    urlParams.put("demageWarehouseNumber", "");
//                    urlParams.put("IdNumber", "");
                    urlParams.put("demageCode", "");
//                    urlParams.put("superDemagePosition", "");
                }
                binding.btnRight.setEnabled(false);
                SGHTTPManager.POST(API.URL_REPAIR_ADD, urlParams, new SGHTTPManager.SGRequestCallBack() {
                    @Override
                    public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                        Toast toast;
                        if (isSuccess) {
                            toast = Toast.makeText(MalfunctionRepairActivity.this, "提交成功", Toast.LENGTH_LONG);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    binding.btnRight.setEnabled(true);
                                }
                            }, 1000);
                        } else {
                            toast = Toast.makeText(MalfunctionRepairActivity.this, userInfo, Toast.LENGTH_LONG);
                            binding.btnRight.setEnabled(true);
                        }
                        ToastUtil.showMyToast(toast, 3000);
                    }
                });
            }

            @Override
            protected void onNoneClick(View v) {
                Toast toast = Toast.makeText(MalfunctionRepairActivity.this, "点击过快", Toast.LENGTH_SHORT);
                ToastUtil.showMyToast(toast, 3000);
            }
        });
    }

    /**
     * spinner控件的初始化
     *
     * @param stations
     */
    private CustomDatePicker customDatePicker;
    private void initSpinner(List<ExchangeSite> stations) {
        /**
         * spinner对应的数据源的选择
         */
//        String[] part = {"充电架", "超级站自动化", "换电叉车", "点灯", "风扇", "地胶", "其他"};
//        String[] code = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "无"};
//        String[] warehouse = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "无"};
//        String[] IDcode = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "无"};
//        String[] malcoding = {"E01", "E02", "E03", "E04", "E05", "E06", "E07", "E08", "E09", "E10", "E11", "E20", "E21", "E22", "E23", "E24",
//                "E30", "E31", "E33", "E34", "E50", "E51", "E52", "E58", "E70", "E71", "E72", "E73", "无"};
        String[] supermarlpart = {"供电柜", "轨道1", "轨道2", "轨道3", "助力臂1", "助力臂2", "助力臂3", "助力臂4", "助力臂5", "助力臂6", "举升机1", "举升机2", "举升机3", "堆垛小车", "其他部位"};
        String[] level = {"一级", "二级", "三级"};
        /**
         * 站点
         */
        ArrayList<String> namelist = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++) {
            namelist.add(stations.get(i).getName());
        }
        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, namelist);
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStationChoose.setAdapter(stationAdapter);
        binding.spinnerStationChoose.setOnItemSelectedListener(this);
        for (int i = 0; i < namelist.size(); i++) {
            if (User.shared().getStation().equals(namelist.get(i))) {
                binding.spinnerStationChoose.setSelection(i);
            }
        }
        /**
         * 超级故障部位
         */
        ArrayAdapter<String> superpartAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, supermarlpart);
        superpartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerMalfunctionsuperpartChoose.setAdapter(superpartAdapter);
        binding.spinnerMalfunctionsuperpartChoose.setOnItemSelectedListener(this);
        /**
         * 故障级别
         */
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, level);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerMalfunctionlevelChoose.setAdapter(levelAdapter);
        binding.spinnerMalfunctionlevelChoose.setOnItemSelectedListener(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String now = sdf.format(new Date());
        binding.tvDateChoose.setText(now);
        Log.e("currentTime", now);
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                binding.tvDateChoose.setText(time+":00");
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
    }

    private String getDate() {
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateformat.format(date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_date_choose:
                dataSelected();
                break;
            default:
                break;
        }
    }
    /**
     * 时间选择器
     */
    private void dataSelected() {
//        SelectData selectData = new SelectData(MalfunctionRepairActivity.this, true);
//        selectData.showAtLocation(binding.tvDateChoose, Gravity.BOTTOM, 0, 0);
//        selectData.setDateClickListener(new SelectData.OnDateClickListener() {
//            @Override
//            public void onClick(String year, String month, String day, String hour, String minute) {
//                Utils.log(hour+"ss"+minute);
//                binding.tvDateChoose.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
//            }
//        });
        customDatePicker.show(binding.tvDateChoose.getText().toString());
    }

    /**
     * spinner点击选择
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_station_choose:
                for (int j = 0; j < stationlist.size(); j++) {
                    if (adapterView.getSelectedItem().equals(stationlist.get(j).getName())) {
                        stationID = stationlist.get(j).getId();
                    }
                }
                break;
            case R.id.spinner_malfunctionsuperpart_choose:
                superpart = (String) adapterView.getSelectedItem();
                break;
            case R.id.spinner_malfunctionlevel_choose:
                switch ((String)adapterView.getSelectedItem()) {
                    case "一级":
                        level = 1;
                        break;
                    case "二级":
                        level = 2;
                        break;
                    case "三级":
                        level = 3;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * spinner点击外部无选择内容
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
