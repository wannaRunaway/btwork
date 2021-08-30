package com.botann.charing.pad.activity.malfunctionrecord;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.botann.charging.pad.R;
import com.botann.charging.pad.databinding.ActivityMalfunctionrecordBinding;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.components.zxing.decode.Utils;
import com.botann.charing.pad.model.ExchangeSite;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.model.repairlist.RepairData;
import com.botann.charing.pad.model.repairlist.RepairList;
import com.botann.charing.pad.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * created by xuedi on 2018/10/31
 * 故障记录
 */
public class MalfunctionRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMalfunctionrecordBinding binding;
    private int pageNo = 1;
    private View view;
    private TextView tv_start, tv_stop, tv_determin;
    private RecordAdapter adapter;
    private List<RepairData> repairDataList = new ArrayList<>();
    private List<ExchangeSite> stationlist;
    private CustomDatePicker customDatePicker1, customDatePicker2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_malfunctionrecord);
        view = LayoutInflater.from(this).inflate(R.layout.dialog_datemal, null, false);
        tv_start = (TextView) view.findViewById(R.id.tv_starttime_input);
        tv_stop = (TextView) view.findViewById(R.id.tv_stoptime_input);
        tv_start.setOnClickListener(this);
        tv_stop.setOnClickListener(this);
        tv_determin = (TextView) view.findViewById(R.id.tv_determine);
        binding.btnLeft.setOnClickListener(this);
        binding.tvDateStart.setOnClickListener(this);
        binding.tvDateEnd.setOnClickListener(this);
        binding.startClear.setOnClickListener(this);
        binding.endClear.setOnClickListener(this);
        tv_determin.setOnClickListener(this);
        binding.tvMainTitle.setText("故障记录");
        String stations = getIntent().getStringExtra("station");
        stationlist = GsonUtils.getInstance().fromJson(stations, new TypeToken<List<ExchangeSite>>() {
        }.getType());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordAdapter(repairDataList, MalfunctionRecordActivity.this, new RecordInter() {
            @Override
            public void recordClick(RepairData repairData) {
                Intent intent = new Intent();
                intent.putExtra("repairData", repairData);
//                for (int i = 0; i < stationlist.size(); i++) {
//                    if (repairData.getSiteId() == stationlist.get(i).getId()){
//                        intent.putExtra("stationName", stationlist.get(i).getName());
//                    }
//                }
                intent.setClass(MalfunctionRecordActivity.this, RepairRecordActivity.class);
                startActivity(intent);
            }
        });
        binding.recyclerView.setAdapter(adapter);
        initPicker();

    }

    private void initPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String now = sdf.format(new Date());
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
//                        currentTime.setTextColor(getResources().getColor(R.color.color6));
                binding.tvDateStart.setText(time+":00");
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动
        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
//                        currentTime.setTextColor(getResources().getColor(R.color.color6));
                binding.tvDateEnd.setText(time+":00");
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }

    @Override
    protected void onStart() {
        super.onStart();
        smartRefresh();
    }

    /**
     * Integer siteId（站点ID，必填）;
     * Integer pageNo（页数，非必填，默认1）;
     * Integer pageSize（每页数量，非必填，默认20）;
     * Date startTimeStr（开始时间，非必填）;
     * Date endTimeStr（结束时间，非必填）;
     */
    private void smartRefresh() {
        binding.smartRefresh.autoRefresh();
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                if (binding.tvDateStart.getText().toString().indexOf("日期")==-1 && binding.tvDateEnd.getText().toString().indexOf("日期")==-1){
                    loadData(true, binding.tvDateStart.getText().toString(), binding.tvDateEnd.getText().toString());
                }else {
                    loadData(true, null, null);
                }
            }
        });
        binding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                if (binding.etDateChoose.getText().toString().indexOf("至")!=-1){
//                    String[] splite = binding.etDateChoose.getText().toString().split("至");
//                    loadData(false, splite[0]+" 00:00:00", splite[1]+" 23:59:59");
                if (binding.tvDateStart.getText().toString().indexOf("日期")==-1 && binding.tvDateEnd.getText().toString().indexOf("日期")==-1){
                    loadData(false, binding.tvDateStart.getText().toString(), binding.tvDateEnd.getText().toString());
                }else {
                    loadData(false, null, null);
                }
            }
        });
    }

    private void loadData(final boolean isRefresh, String startTime, String endTime) {
        URLParams urlParams = new URLParams();
        urlParams.put("siteId", User.shared().getStationId());
        urlParams.put("pageNo", pageNo);
//        urlParams.put("pageSize", 5);
        if (startTime!= null && !startTime.equals("")){
            urlParams.put("startTimeStr", startTime);
        }
        if (endTime != null && !startTime.equals("")){
            urlParams.put("endTimeStr", endTime);
        }
        pageNo = pageNo + 1;
        SGHTTPManager.POST(API.URL_REPAIR_LIST, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                binding.smartRefresh.finishRefresh();
                binding.smartRefresh.finishLoadMore();
                if (isSuccess) {
                    RepairList repairList = SGFetchModel.getGson().fromJson(fetchModel.getJsonObject().toString(), RepairList.class);
                    if (isRefresh) {
                        repairDataList.clear();
                    }
                    repairDataList.addAll(repairList.getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MalfunctionRecordActivity.this, userInfo, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_date_start:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                String now = sdf.format(new Date());
                customDatePicker1.show(now);
                Utils.log("点击开始日期");
                break;
            case R.id.tv_date_end:
                SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                String nows = sdfs.format(new Date());
                customDatePicker2.show(nows);
                Utils.log("点击结束日期");
                break;
            case R.id.start_clear:
                binding.tvDateStart.setText("开始日期");
                break;
            case R.id.end_clear:
                binding.tvDateEnd.setText("结束日期");
                break;
            default:
                break;
        }
    }

    public void showBottomDialog() {
//        SelectData selectData = new SelectData(context, false);
//        selectData.showAtLocation(textView, Gravity.BOTTOM, 0, 0);
//        selectData.setDateClickListener(new SelectData.OnDateClickListener() {
//            @Override
//            public void onClick(String year, String month, String day, String hour, String minute) {
//                textView.setText(year + "-" + month + "-" + day);
//            }
//        });
//        customDatePicker.show(binding.etDateChoose.getText().toString());
    }
}
