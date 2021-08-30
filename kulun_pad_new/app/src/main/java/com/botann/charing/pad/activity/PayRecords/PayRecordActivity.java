package com.botann.charing.pad.activity.PayRecords;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.botann.charing.pad.base.Pager;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.callbacks.SGFinishCallBack;
import com.botann.charging.pad.R;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.activity.PagerActivity;

import com.botann.charing.pad.components.zxing.QRScanActivity;
import com.botann.charing.pad.model.PayRecord;

/**
 * Created by mengchenyun on 2017/1/18.
 */

public class PayRecordActivity extends PagerActivity {

    private EditText etAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("充值记录");
        cellAdapter = new PayRecordAdapter(listData);
        mRecyclerView.setAdapter(cellAdapter);
        mRecyclerView.refresh();
    }

    @Override
    public int viewLayout() {
        return R.layout.activity_pay_record;
    }


    @Override
    public void initView() {

        Button btnScan = (Button) findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toScan = new Intent(getApplicationContext(), QRScanActivity.class);
                startActivityForResult(toScan, 1);
            }
        });
        etAccount = (EditText) findViewById(R.id.et_account);
        Button btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.refresh();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String account = data.getStringExtra(QRScanActivity.kValue);
            etAccount.setText(account);
            mRecyclerView.refresh();
        } else {
            toast("二维码识别失败！");
        }
    }


    public void loadDatas (final Boolean isRefresh,final SGFinishCallBack callBack) {
        int nowPage = currentPage;
        if (isRefresh) nowPage = 1;
        URLParams params = new URLParams();
        if (etAccount.getText().length()>0) params.put("account",etAccount.getText());
        params.put("pageNo",nowPage);
        params.put("pageSize",pageSize);
        params.put("siteId", User.shared().getStationId());
        SGHTTPManager.POST(API.URL_PAY_RECORDS, params, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess){
                    if (isRefresh) listData.clear();
                    Pager pager = fetchModel.pagerOfContent(PayRecord.class);
                    listData.addAll(pager.list);
                    callBack.onFinish(pager.list.size()<pageSize);
                }else {
                    toast(userInfo);
                    callBack.onFinish(true);
                }
            }
        });
    }



}
