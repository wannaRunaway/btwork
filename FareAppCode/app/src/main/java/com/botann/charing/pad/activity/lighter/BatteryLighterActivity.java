package com.botann.charing.pad.activity.lighter;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.botann.charging.pad.R;
import com.botann.charging.pad.databinding.ActivityBatteryLightBinding;
import com.botann.charing.pad.activity.malfunctionrecord.CustomDatePicker;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.OnMultiClickListener;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.components.zxing.decode.Utils;
import com.botann.charing.pad.model.ExchangeSite;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.model.batterylighter.BatteryLighter;
import com.botann.charing.pad.model.batterylighter.BatteryLighterDetail;
import com.botann.charing.pad.utils.DateUtils;
import com.botann.charing.pad.utils.GsonUtils;
import com.botann.charing.pad.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * created by xuedi on 2018/11/28
 * 电池驳运
 * 1) 直接进入空白显示
 * 2）从其他地方跳转显示
 */
public class BatteryLighterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ActivityBatteryLightBinding binding;
    private List<ExchangeSite> stationlist;
    private String transferStationName = User.shared().getStation();
    private int transferStationId;
    private String acceptStationName = "";
    private int acceptStationId;
    private int spinnerNum1, spinnerNum2, spinnerNum3, spinnerNum4, spinnerNum5, spinnerNum6;
    private String num1, num2, num3, num4, num5, num6;
    private BatteryLighter batteryLighter;
    private CustomDatePicker customDatePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_battery_light);
        String stations = getIntent().getStringExtra("station");
        batteryLighter = (BatteryLighter) getIntent().getSerializableExtra("batteryLighter");
        stationlist = GsonUtils.getInstance().fromJson(stations, new TypeToken<List<ExchangeSite>>() {
        }.getType());
        initView();
        Spinner[] spinners = new Spinner[]{binding.spinnerNum1, binding.spinnerNum2, binding.spinnerNum3, binding.spinnerNum4, binding.spinnerNum5, binding.spinnerNum6};
        if (batteryLighter != null) {
            /*
            不为null是查询界面过来的
             */
            binding.tvTransferTime.setText(DateUtils.stampToDate(batteryLighter.getTransferTime()));
            binding.etCarplate.setText(batteryLighter.getCarNumber());
            binding.etRemarks.setText(batteryLighter.getRemark());
            modifyTransferAndAcceptSpinner();
            modifyBatteryTypeDetails(spinners);
        } else {
            /**
             * 为null是初次界面初始化
             */
            initTransferAndAcceptSpinner();
            for (int i = 0; i < spinners.length; i++) {
                initBatteryElectSpinner(spinners[i]);
            }
        }
    }

    /**
     * 修改6个电池类型的数量和spinner
     *
     * @param spinners
     */
    private void modifyBatteryTypeDetails(Spinner[] spinners) {
        /**
         * 电池数量初始化
         */
        binding.etNum1.setText(getDetails().get(0).getNum());
        binding.etNum2.setText(getDetails().get(1).getNum());
        binding.etNum3.setText(getDetails().get(2).getNum());
        binding.etNum4.setText(getDetails().get(3).getNum());
        binding.etNum5.setText(getDetails().get(4).getNum());
        binding.etNum6.setText(getDetails().get(5).getNum());
        /**
         * 电池spinner初始化
         */
        for (int i = 0; i < spinners.length; i++) {
            modifyBatteryElectSpinner(spinners[i], getDetails().get(i).getElect().equals("0") ? 1 : 0);
        }
    }

    /**
     * 电池类型列表
     */
    private ArrayList<BatteryLighterDetail> getDetails() {
        ArrayList<BatteryLighterDetail> list = new ArrayList<>();
        String str[] = batteryLighter.getBatteryDetail().split(";");
        for (int i = 0; i < str.length; i++) {
            String string[] = str[i].split(",");
            BatteryLighterDetail batteryLighterDetail = new BatteryLighterDetail();
            batteryLighterDetail.setType(string[0]);
            batteryLighterDetail.setNum(string[1]);
            batteryLighterDetail.setElect(string[2]);
            list.add(batteryLighterDetail);
        }
        return list;
    }

    /**
     * 修改两个站点spinner初始化
     */
    private void modifyTransferAndAcceptSpinner() {
        /**
         * 移交站点spinner
         */
        ArrayList<String> transferList = new ArrayList<>();
        for (int i = 0; i < stationlist.size(); i++) {
            transferList.add(stationlist.get(i).getName());
        }
        ArrayAdapter<String> transferAdaqpter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, transferList);
        transferAdaqpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTransfer.setAdapter(transferAdaqpter);
        binding.spinnerTransfer.setOnItemSelectedListener(this);
        for (int i = 0; i < transferList.size(); i++) {
            if (batteryLighter.getTransferSiteName().equals(transferList.get(i))) {
                binding.spinnerTransfer.setSelection(i);
            }
        }
        /**
         * 接受站点spinner
         */
        ArrayAdapter<String> acceptAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, transferList);
        acceptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAccept.setAdapter(acceptAdapter);
        binding.spinnerAccept.setOnItemSelectedListener(this);
        for (int i = 0; i < transferList.size(); i++) {
            if (batteryLighter.getReceiveSiteName().equals(transferList.get(i))) {
                binding.spinnerAccept.setSelection(i);
            }
        }
    }

    /**
     * 两个站点spinner的初始化
     */
    private void initTransferAndAcceptSpinner() {
        /**
         * 移交站点spinner
         */
        ArrayList<String> transferList = new ArrayList<>();
        for (int i = 0; i < stationlist.size(); i++) {
            transferList.add(stationlist.get(i).getName());
        }
        ArrayAdapter<String> transferAdaqpter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, transferList);
        transferAdaqpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTransfer.setAdapter(transferAdaqpter);
        binding.spinnerTransfer.setOnItemSelectedListener(this);
        for (int i = 0; i < transferList.size(); i++) {
            if (User.shared().getStation().equals(transferList.get(i))) {
                binding.spinnerTransfer.setSelection(i);
            }
        }
        /**
         * 接受站点spinner
         */
        ArrayList<String> acceptList = new ArrayList<>();
        acceptList.add(0, acceptStationString);
        acceptList.addAll(transferList);
        ArrayAdapter<String> acceptAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, acceptList);
        acceptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAccept.setAdapter(acceptAdapter);
        binding.spinnerAccept.setOnItemSelectedListener(this);
    }

    private static String acceptStationString = "请选择接收站点";

    /**
     * 6个电池电量spinner初始化
     */
    private void initBatteryElectSpinner(Spinner spinner) {
        String[] items = new String[]{"满电", "空电"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * 修改6个电池电量spinner初始化
     */
    private void modifyBatteryElectSpinner(Spinner spinner, int position) {
        String[] items = new String[]{"满电", "空电"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(position);
    }

    /**
     * 移交时间、顶层条目的初始化
     */
    private void initView() {
        binding.tvMainTitle.setText("电池驳运(" + User.shared().getStation() + ")");
        binding.btnLeft.setOnClickListener(this);
        binding.tvTransferTime.setOnClickListener(this);
        binding.btnRight.setOnClickListener(new OnMultiClickListener() {
            @Override
            protected void onMultiClick(View v) {
                if (binding.tvTransferTime.getText().toString().equals("")) { //移交时间为null
                    Toast toast = Toast.makeText(BatteryLighterActivity.this, "请填写移交时间", Toast.LENGTH_LONG);
                    ToastUtil.showMyToast(toast, 3000);
                    return;
                }
                if (acceptStationName.equals(acceptStationString) || acceptStationId == 0) {
                    Toast toast = Toast.makeText(BatteryLighterActivity.this, "请选择接收站点", Toast.LENGTH_LONG);
                    ToastUtil.showMyToast(toast, 3000);
                    return;
                }
                if (binding.etCarplate.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(BatteryLighterActivity.this, "请填写运输车牌", Toast.LENGTH_LONG);
                    ToastUtil.showMyToast(toast, 3000);
                    return;
                }
                num1 = binding.etNum1.getText().toString().equals("")? "0" : binding.etNum1.getText().toString();
                num2 = binding.etNum2.getText().toString().equals("")? "0" : binding.etNum2.getText().toString();
                num3 = binding.etNum3.getText().toString().equals("")? "0" : binding.etNum3.getText().toString();
                num4 = binding.etNum4.getText().toString().equals("")? "0" : binding.etNum4.getText().toString();
                num5 = binding.etNum5.getText().toString().equals("")? "0" : binding.etNum5.getText().toString();
                num6 = binding.etNum6.getText().toString().equals("")? "0" : binding.etNum6.getText().toString();
                uploadData();
            }

            @Override
            protected void onNoneClick(View v) {
                Toast toast = Toast.makeText(BatteryLighterActivity.this, "点击过快", Toast.LENGTH_SHORT);
                ToastUtil.showMyToast(toast, 3000);
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String now = sdf.format(new Date());
//                currentDate.setText(now.split(" ")[0]);
//        currentTime.setTextColor(getResources().getColor(R.color.color2));
        binding.tvTransferTime.setText(now);
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
                binding.tvTransferTime.setText(time+":00");
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_transfer_time:
//                SelectData selectData = new SelectData(BatteryLighterActivity.this, true);
//                selectData.showAtLocation(binding.tvTransferTime, Gravity.BOTTOM, 0, 0);
//                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
//                    @Override
//                    public void onClick(String year, String month, String day, String hour, String minute) {
//                        binding.tvTransferTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
//                    }
//                });
                customDatePicker.show(binding.tvTransferTime.getText().toString());
                break;
            default:
                break;
        }
    }

    /**
     * 上传数据
     */
    private void uploadData() {
        URLParams urlParams = new URLParams();
        String url = API.URL_LIGHTER;
        if (batteryLighter!= null){
            url = API.URL_UPDATE_TRANSFER;
            urlParams.put("id", batteryLighter.getId());
        }
        urlParams.put("transferSiteId", transferStationId);
        urlParams.put("transferSiteName", transferStationName);
        urlParams.put("transferAdminUserId", User.shared().getId());
        urlParams.put("transferAdminUserName", User.shared().getUsername());
        urlParams.put("transferTimeStr", binding.tvTransferTime.getText().toString()+":00");
        urlParams.put("receiveSiteId", acceptStationId);
        urlParams.put("receiveSiteName", acceptStationName);
        urlParams.put("carNumber", binding.etCarplate.getText().toString());
        urlParams.put("remark", binding.etRemarks.getText().toString());
        String batteryDetail = 1 + "," + num1 + "," + spinnerNum1 + ";" +
                2 + "," + num2 + "," + spinnerNum2 + ";" +
                3 + "," + num3 + "," + spinnerNum3 + ";" +
                4 + "," + num4 + "," + spinnerNum4 + ";" +
                5 + "," + num5 + "," + spinnerNum5 + ";" +
                6 + "," + num6 + "," + spinnerNum6;
        urlParams.put("batteryDetail", batteryDetail);
        binding.btnRight.setEnabled(false);
        SGHTTPManager.POST(url, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                Toast toast;
                if (isSuccess) {
                    toast = Toast.makeText(BatteryLighterActivity.this, "提交成功", Toast.LENGTH_LONG);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            binding.btnRight.setEnabled(true);
                        }
                    }, 1000);
                } else {
                    toast = Toast.makeText(BatteryLighterActivity.this, userInfo, Toast.LENGTH_LONG);
                    binding.btnRight.setEnabled(true);
                }
                ToastUtil.showMyToast(toast, 3000);
            }
        });
    }

    /**
     * spinner 条目选择监听
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_transfer: //移交站点
                transferStationName = (String) adapterView.getSelectedItem();
                for (int j = 0; j < stationlist.size(); j++) {
                    if (transferStationName.equals(stationlist.get(j).getName())) {
                        transferStationId = stationlist.get(j).getId();
                    }
                }
                break;
            case R.id.spinner_accept:  //接收站点
                acceptStationName = (String) adapterView.getSelectedItem();
                for (int j = 0; j < stationlist.size(); j++) {
                    if (acceptStationName.equals(stationlist.get(j).getName())) {
                        acceptStationId = stationlist.get(j).getId();
                    }
                }
                break;
            case R.id.spinner_num1:
                spinnerNum1 = ((String) adapterView.getSelectedItem()).equals("满电") ? 1 : 0;
                break;
            case R.id.spinner_num2:
                spinnerNum2 = ((String) adapterView.getSelectedItem()).equals("满电") ? 1 : 0;
                break;
            case R.id.spinner_num3:
                spinnerNum3 = ((String) adapterView.getSelectedItem()).equals("满电") ? 1 : 0;
                break;
            case R.id.spinner_num4:
                spinnerNum4 = ((String) adapterView.getSelectedItem()).equals("满电") ? 1 : 0;
                break;
            case R.id.spinner_num5:
                spinnerNum5 = ((String) adapterView.getSelectedItem()).equals("满电") ? 1 : 0;
                break;
            case R.id.spinner_num6:
                spinnerNum6 = ((String) adapterView.getSelectedItem()).equals("满电") ? 1 : 0;
                break;
            default:
                break;
        }
    }

    /**
     * spinner 点击外部监听
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
