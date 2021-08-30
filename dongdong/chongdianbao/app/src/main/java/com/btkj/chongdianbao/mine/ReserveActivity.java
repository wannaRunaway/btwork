package com.btkj.chongdianbao.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityReserveBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.model.ReserverModel;
import com.btkj.chongdianbao.model.User;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.BaseDialog;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.DateUtils;
import com.btkj.chongdianbao.utils.GsonUtils;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReserveActivity extends BaseActivity implements View.OnClickListener {
    private ActivityReserveBinding binding;
    private int page = 1;
    private List<ReserverModel.ContentBean> list = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reserve);
        binding.header.left.setOnClickListener(this);
        binding.header.title.setText("我的预约");
        adapter = new MyAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ReserveActivity.this));
        binding.recyclerView.setAdapter(adapter);
        binding.imgYuyueDelay.setOnClickListener(view -> {
            if (User.getInstance().isDelay()){
                Utils.snackbar(getApplicationContext(), ReserveActivity.this, "只能延迟一次哦");
                return;
            }
            BaseDialog.showDialog(ReserveActivity.this, "您是否需要延迟预约10分钟", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delayappoint();
                }
            });
        });
        binding.imgYuyueCancel.setOnClickListener(view -> {
            BaseDialog.showDialog(ReserveActivity.this, "您是否确定取消预约", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelappoint();
                }
            });
        });
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadData(true);
            }
        });
        binding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                loadData(false);
            }
        });
        loadData(true);
    }

    //pageNo     [int]	是	页数，默认1
    //pageSize   [int]	是	页/条、默认20
    private void loadData(boolean isRefresh) {
        String url = API.BASE_URL + API.YUYUEHISTORY;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(page));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, ReserveActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                ReserverModel model = GsonUtils.getInstance().fromJson(json, ReserverModel.class);
                if (isRefresh) {
                    list.clear();
                }
                list.addAll(model.getContent());
                adapter.notifyDataSetChanged();
                binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
            }
        },binding.smartRefresh);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                finishLoad();
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                ReserverModel model = GsonUtils.getInstance().fromJson(json, ReserverModel.class);
//                if (isRefresh) {
//                    list.clear();
//                }
//                list.addAll(model.getContent());
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                finishLoad();
//                Utils.snackbar(getApplicationContext(), ReserveActivity.this, API.net_error);
//                Utils.log(url, requestParams, null);
//            }
//        });
    }

    private void finishLoad() {
        binding.smartRefresh.finishRefresh();
        binding.smartRefresh.finishLoadMore();
    }

    //延迟预约
    private void delayappoint() {
        //id  [int]	是	预约id
        if (!User.getInstance().isYuyue()) {
            Utils.snackbar(getApplicationContext(), ReserveActivity.this, API.yueyue_code);
            return;
        }
        String url = API.BASE_URL + API.DELAYAPPOINT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(User.getInstance().getReserverId()));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, ReserveActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_success_delay));
                User.getInstance().setDelay(true);
                loadData(true);
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.setCookieStore(cookieStore);
//        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                ReserverDelayModel model = GsonUtils.getInstance().fromJson(json, ReserverDelayModel.class);
//                if (model.isSuccess()){
//                    binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_success_delay));
//                    User.getInstance().setDelay(true);
//                    loadData(true);
//                }else {
//                    Utils.snackbar(getApplicationContext(), ReserveActivity.this, model.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), ReserveActivity.this, API.net_error);
//                Utils.log(url, requestParams, "shibai");
//            }
//        });
    }

    //取消预约
    private void cancelappoint() {
        //id  [int]	是	预约id
        if (!User.getInstance().isYuyue()) {
            Utils.snackbar(getApplicationContext(), ReserveActivity.this, API.yueyue_code);
            return;
        }
        String url = API.BASE_URL + API.CANCELAPPOINT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(User.getInstance().getReserverId()));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, ReserveActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_delay));
                User.getInstance().setDelay(false);
                User.getInstance().setYuyue(false);
                binding.reBottom.setVisibility(View.GONE);
                loadData(true);
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.setCookieStore(cookieStore);
//        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                BaseObject model = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (model.isSuccess()) {
//                    binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_delay));
//                    User.getInstance().setDelay(false);
//                    User.getInstance().setYuyue(false);
//                    binding.reBottom.setVisibility(View.GONE);
//                    loadData(true);
//                } else {
//                    Utils.snackbar(getApplicationContext(), ReserveActivity.this, model.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), ReserveActivity.this, API.net_error);
//                Utils.log(url, requestParams, "shibai");
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            default:
                break;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ReserveActivity.this).inflate(R.layout.adapter_reserver, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ReserverModel.ContentBean contentBean = list.get(position);
            String status = "";
            switch (contentBean.getStatus()) {
                case -2:
                    status = "申请预约";
                    break;
                case -1:
                    status = "申请预约失败";
                    break;
                case 0:
                    status = "预约中";
                    break;
                case 1:
                    status = "取消预约";
                    break;
                case 2:
                    status = "预约超时";
                    break;
                case 3:
                    status = "换电完成";
                    break;
                default:
                    break;
            }
            if (status.endsWith("预约中")){
                holder.tv_status.setTextColor(getResources().getColor(R.color.text_color));
                holder.tv_time.setTextColor(getResources().getColor(R.color.text_color));
                holder.tv_station.setTextColor(getResources().getColor(R.color.text_color));
                holder.tv_time.setText("预约倒计时："+Utils.formatDateTime((contentBean.getEndTime()-System.currentTimeMillis())/1000));
            }else {
                holder.tv_status.setTextColor(getResources().getColor(R.color.black));
                holder.tv_time.setTextColor(getResources().getColor(R.color.black));
                holder.tv_station.setTextColor(getResources().getColor(R.color.black));
                holder.tv_time.setText("预约时间：" + DateUtils.stampToDate(contentBean.getCreateTime()));
            }
            holder.tv_status.setText(status);
            holder.tv_station.setText(contentBean.getStationName() + "   车牌号：" + contentBean.getPlateNo());
            String finalStatus = status;
            holder.re.setOnClickListener(view -> {
                if (finalStatus.endsWith("预约中")){
                    binding.reBottom.setVisibility(View.VISIBLE);
                }else {
                    binding.reBottom.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_station, tv_time, tv_status;
        private RelativeLayout re;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_station = itemView.findViewById(R.id.tv_station);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_status = itemView.findViewById(R.id.tv_status);
            re = itemView.findViewById(R.id.re);
        }
    }
}
