package com.btkj.chongdianbao.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityMyCarBinding;
import com.btkj.chongdianbao.login.AddCarActivity;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.model.CarListModel;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.GsonUtils;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.SharePref;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCarActivity extends BaseActivity implements View.OnClickListener {
    private ActivityMyCarBinding binding;
    private MyAdapter adapter;
    private int pageNo = 1;
    private List<CarListModel.ContentBean> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_car);
        binding.header.left.setOnClickListener(this);
        binding.header.title.setText("我的车");
        binding.imgAddCar.setOnClickListener(this);
        adapter = new MyAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MyCarActivity.this));
        binding.recyclerView.setAdapter(adapter);
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                loadData(true);
            }
        });
        binding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo = pageNo + 1;
                loadData(false);
            }
        });
        loadData(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String s) {
        if ("refreshmycar".equals(s)) {
            pageNo = 1;
            loadData(true);
        }
    }

    private void loadData(boolean isRefresh) {
        /**
         * pageNo    [int]	是
         * pageSize  [int]	是
         */
        String url = API.BASE_URL + API.MYBINDCAR;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNo));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, MyCarActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                CarListModel model = GsonUtils.getInstance().fromJson(json, CarListModel.class);
                if (isRefresh) {
                    list.clear();
                }
                list.addAll(model.getContent());
                adapter.notifyDataSetChanged();
                binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
            }
        }, binding.smartRefresh);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                refreshFinish();
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                CarListModel model = GsonUtils.getInstance().fromJson(json, CarListModel.class);
//                if (isRefresh) {
//                    list.clear();
//                }
//                list.addAll(model.getContent());
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                refreshFinish();
//                Utils.log(url, requestParams, "失败");
//                Utils.snackbar(getApplicationContext(), MyCarActivity.this, API.net_error);
//            }
//        });
    }

    private void refreshFinish() {
        binding.smartRefresh.finishRefresh();
        binding.smartRefresh.finishLoadMore();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.img_add_car:
                Intent intent = new Intent(MyCarActivity.this, AddCarActivity.class);
                intent.putExtra(API.islogin, false);
                startActivity(intent);
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MyCarActivity.this).inflate(R.layout.adapter_mycar, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            MyViewHolder holder1 = holder;
            holder1.plateNom.setText("车牌号：" + list.get(position).getPlateNo());
            holder1.carType.setText("车型：" + list.get(position).getCarTypeName());
            holder1.re_group.setOnClickListener(view -> {
                new AlertDialog.Builder(MyCarActivity.this).setMessage("您是否删除这辆车").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        cancelCar(list.get(position).getBindId(), list.get(position).getPlateNo());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    //bindId[int]	是	绑定ID
    private void cancelCar(int bindId, String plateNo) {
        String url = API.BASE_URL + API.DELETECAR;
        RequestParams requestParams = new RequestParams();
        Map<String,String> map = new HashMap<>();
        map.put("bindId", String.valueOf(bindId));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, MyCarActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                String plate = (String) SharePref.get(MyCarActivity.this, API.plateNo, "");
                if (plate.equals(plateNo)){
                    SharePref.put(MyCarActivity.this, API.plateNo, "");
                    SharePref.put(MyCarActivity.this, API.bindId, 0);
                    SharePref.put(MyCarActivity.this, API.carId, 0);
                    SharePref.put(MyCarActivity.this, API.carType, 0);
                    SharePref.put(MyCarActivity.this, API.batterycount, 0);
                }
                loadData(true);
            }
        });
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        PersistentCookieStore cookieStore = new PersistentCookieStore(MyCarActivity.this);
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (baseObject.isSuccess()){
//                    loadData(true);
//                }else {
//                    Utils.snackbar(getApplicationContext(), MyCarActivity.this, baseObject.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), MyCarActivity.this, API.net_error);
//                Utils.log(url, requestParams, null);
//            }
//        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView plateNom, carType;
        RelativeLayout re_group;

        public MyViewHolder(View itemView) {
            super(itemView);
            plateNom = itemView.findViewById(R.id.tv_car_plate);
            carType = itemView.findViewById(R.id.tv_car_type);
            re_group = itemView.findViewById(R.id.re_group);
        }
    }
}
