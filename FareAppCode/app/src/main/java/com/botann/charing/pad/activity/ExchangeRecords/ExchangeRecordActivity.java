package com.botann.charing.pad.activity.ExchangeRecords;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.text.ParseException;
import java.util.Date;

import com.botann.charing.pad.base.Pager;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.callbacks.SGFinishCallBack;
import com.botann.charging.pad.R;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.model.ExchangeRecord;
import com.botann.charing.pad.activity.PagerActivity;

import com.botann.charing.pad.model.User;
import com.botann.charing.pad.components.zxing.QRScanActivity;
import com.botann.charing.pad.views.SelfDialog;
import com.botann.charing.pad.utils.DateUtil;
import com.botann.charing.pad.utils.FormatUtil;


/**
 * Created by mengchenyun on 2017/1/18.
 */

public class ExchangeRecordActivity extends PagerActivity {



    public String startDate;
    public String endDate;
    public String account;

    private SelfDialog selfDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mInstance = this;
        setTitle("换电记录（"+ User.shared().getStation()+"）");
        if(savedInstanceState != null){
            SelfDialog dialog = (SelfDialog) getFragmentManager()
                    .findFragmentByTag("selfDialog");
            if(dialog != null) {
                setDialogEvent(dialog);
            }
        }
        cellAdapter = new ExchangeRecordAdapter(listData);
        mRecyclerView.setAdapter(cellAdapter);
        mRecyclerView.refresh();
    }

    @Override
    public int viewLayout() {
        return R.layout.activity_exchange_record;
    }




    @Override
    public void initView() {

//        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
//        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
//        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                loadDatas(true, new SGFinishCallBack() {
//                    @Override
//                    public void onFinish(Boolean result) {
//                        cellAdapter.notifyDataSetChanged();
//                        currentPage = 2;
//                        mRecyclerView.refreshComplete();
//                        if (result) {
//                            Log.i("加载","no more true");
//                            mRecyclerView.setNoMore(true);
//                        }else {
//                            Log.i("加载","no more false");
//                            mRecyclerView.setNoMore(false);
//                        }
//                    }
//                });
//
//            }
//
//            @Override
//            public void onLoadMore() {
//                loadDatas(false, new SGFinishCallBack() {
//                    @Override
//                    public void onFinish(Boolean result) {
//                        cellAdapter.notifyDataSetChanged();
//                        mRecyclerView.loadMoreComplete();
//                        if (result) mRecyclerView.setNoMore(true);
//                        currentPage++;
//                    }
//                });
//            }
//        });
//        mRecyclerView.setAdapter(cellAdapter);


        selfDialog = new SelfDialog();

        // 历史查询
        Button btnRight = (Button) findViewById(R.id.btn_right);
        btnRight.setVisibility(Button.VISIBLE);
        btnRight.setText("历史查询");
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogEvent(selfDialog);
                selfDialog.show(getFragmentManager(), SelfDialog.kDefaultFlag);
            }
        });


    }


    private void setDialogEvent(final SelfDialog selfDialog) {
        Date date = new Date();
        String today = DateUtil.getFormatDate(date);
        selfDialog.setStartDateStr(today);
        selfDialog.setEndDateStr(today);
        selfDialog.setStartDateOnclickListener(new SelfDialog.onStartDateOnclickListener() {
            @Override
            public void onStartDateClick() {
                selfDialog.showDatePicker(0, ExchangeRecordActivity.this);
            }
        });
        selfDialog.setEndDateOnclickListener(new SelfDialog.onEndDateOnclickListener() {
            @Override
            public void onEndDateClick() {
                selfDialog.showDatePicker(1, ExchangeRecordActivity.this);
            }
        });
        selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                String startTime = selfDialog.getStartDateStr();
                String endTime = selfDialog.getEndDateStr();
                String account = FormatUtil.format(selfDialog.getInputStr(), "");
                try {
                    Date endDate = DateUtil.parseDate(endTime);
                    Date startDate = DateUtil.parseDate(startTime);
                    if (!endDate.after(new Date())) {
                        if (!startDate.after(endDate)) {
                            searchWithCondition(startTime,endTime,account);
                            selfDialog.dismiss();
                        } else {
                            toast("起始日期必须在截止日期之前");
                        }
                    } else {
                        toast("截止日期不能选择当天之后");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                selfDialog.dismiss();
            }
        });
        selfDialog.setOnScanOnClickListener(new SelfDialog.onScanOnClickListener() {
            @Override
            public void onScanClick() {
                Intent toScan = new Intent(getApplicationContext(), QRScanActivity.class);
                startActivityForResult(toScan, 1);
            }
        });
    }

    public void searchWithCondition (String startDate, String endDate, String account) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.account = account;
        mRecyclerView.refresh();
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
            SelfDialog dialog = (SelfDialog) getFragmentManager()
                    .findFragmentByTag(SelfDialog.kDefaultFlag);
            dialog.setInputStr(account);
        } else {
            toast("二维码识别失败！");
        }
    }

    public void loadDatas (final Boolean isRefresh,final SGFinishCallBack callBack) {
        int nowPage = currentPage;
        if (isRefresh) nowPage = 1;
        URLParams params = new URLParams();
        if (account !=null && account.length()>0) params.put("account",account);
        if (startDate !=null && startDate.length()>0) params.put("startTime",startDate);
        if (endDate !=null && endDate.length()>0) params.put("endTime",endDate);
        params.put("siteId",User.shared().getStationId());
        params.put("pageNo",nowPage);
        params.put("pageSize",pageSize);
        SGHTTPManager.POST(API.URL_EXCHANGE_RECORDS, params, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess){
                    if (isRefresh) listData.clear();
                    Pager pager = fetchModel.pagerOfContent(ExchangeRecord.class);
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
