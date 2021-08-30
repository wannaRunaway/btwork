package com.btkj.chongdianbao.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityChoseCarplateBinding;
import com.btkj.chongdianbao.mine.MyCarActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChoseCarplateActivity extends BaseActivity {
    private ActivityChoseCarplateBinding binding;
    private int pageNo = 1;
    private List<CarListModel.ContentBean> list = new ArrayList<>();
    private MyAdapter adapter;
    private String carPlate;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chose_carplate);
        binding.right.setOnClickListener(view -> {
                    Intent intent = new Intent(ChoseCarplateActivity.this, MyCarActivity.class);
                    startActivity(intent);
                }
        );
        binding.left.setOnClickListener(view -> finish());
        adapter = new MyAdapter(list);
        binding.gridview.setAdapter(adapter);
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
        if(getIntent()!=null){
            carPlate=getIntent().getStringExtra("carPlate");
        }
//        binding.smartRefresh.autoRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(true);
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
        new MyRequest().mysmartrequest(url, requestParams, ChoseCarplateActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                CarListModel model = GsonUtils.getInstance().fromJson(json, CarListModel.class);
                if (isRefresh) {
                    list.clear();
                }
                list.addAll(model.getContent());
                adapter.notifyDataSetChanged();
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
//                Utils.snackbar(getApplicationContext(), ChoseCarplateActivity.this, API.net_error);
//            }
//        });
    }

    private void refreshFinish() {
        binding.smartRefresh.finishRefresh();
        binding.smartRefresh.finishLoadMore();
    }

    private class MyAdapter extends BaseAdapter {
        private List<CarListModel.ContentBean> list;

        public MyAdapter(List<CarListModel.ContentBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size() + 1;
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (i == list.size()) {
                View myView2 = LayoutInflater.from(ChoseCarplateActivity.this).inflate(R.layout.gridview_item2, null);
                ImageView imageView = myView2.findViewById(R.id.image);
                imageView.setOnClickListener(view1 -> {
                    Intent intent = new Intent(ChoseCarplateActivity.this, MyCarActivity.class);
                    startActivity(intent);
                });
                return myView2;
            }
            View myView = LayoutInflater.from(ChoseCarplateActivity.this).inflate(R.layout.gridview_item, null);
            TextView carplate = myView.findViewById(R.id.carplate);
            CarListModel.ContentBean contentBean = list.get(i);
            carplate.setText(contentBean.getPlateNo());
            if(!TextUtils.isEmpty(contentBean.getPlateNo())&&!TextUtils.isEmpty(carPlate)&&contentBean.getPlateNo().equals(carPlate)){
                carplate.setBackgroundResource(R.mipmap.select_car);
            }else {
                carplate.setBackgroundResource(R.mipmap.select_car_normal);
            }
            carplate.setOnClickListener(view1 -> {
                carplate.setBackgroundResource(R.mipmap.select_car);
                Intent intent = new Intent();
                intent.putExtra(API.bindId, contentBean.getBindId());
                intent.putExtra(API.plateNo, contentBean.getPlateNo());
                intent.putExtra(API.carType, contentBean.getKwh());
                intent.putExtra(API.batterycount, contentBean.getBatteryCount());
                SharePref.put(ChoseCarplateActivity.this, API.bindId, contentBean.getBindId());
                SharePref.put(ChoseCarplateActivity.this, API.carId, contentBean.getCarId());
                SharePref.put(ChoseCarplateActivity.this, API.plateNo, contentBean.getPlateNo());
                SharePref.put(ChoseCarplateActivity.this, API.carType, contentBean.getKwh());
                SharePref.put(ChoseCarplateActivity.this, API.batterycount, contentBean.getBatteryCount());
                setResult(1002, intent);
                finish();
            });
            return myView;
        }
    }
}
