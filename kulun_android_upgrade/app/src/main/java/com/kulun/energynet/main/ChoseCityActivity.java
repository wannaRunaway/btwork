package com.kulun.energynet.main;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityChoseBinding;
import com.kulun.energynet.model.City;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.requestparams.MyRequest;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ChoseCityActivity extends BaseActivity {
    private ActivityChoseBinding binding;
    private List<City> cityList = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chose);
        binding.header.title.setText("选择城市");
        binding.header.left.setOnClickListener(view -> finish());
        adapter = new MyAdapter(cityList);
        binding.gridview.setAdapter(adapter);
        binding.gridview.setVerticalSpacing(60);
        if (TextUtils.isEmpty(UserLogin.getInstance().getCityName(this))) {
            binding.citySelect.setText("定位失败");
        } else {
            if (UserLogin.getInstance().getCityName(this).equals("")) {
                binding.citySelect.setText("定位失败");
            } else {
                binding.citySelect.setText(UserLogin.getInstance().getCityName(this));
            }
        }

        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadCity();
            }
        });
        loadCity();
    }

    private void loadCity() {
        new MyRequest().myRequest(API.CITYLIST, false, null, false, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                cityList.clear();
                cityList.addAll(GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<City>>() {
                }.getType()));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        private List<City> list;

        public MyAdapter(List<City> content) {
            this.list = content;
        }

        @Override
        public int getCount() {
            return list.size();
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
            City dataBean = list.get(i);
            View myView = LayoutInflater.from(ChoseCityActivity.this).inflate(R.layout.gridview_item, viewGroup, false);
            TextView carplate = myView.findViewById(R.id.carplate);
            carplate.setText(dataBean.getName());
            carplate.setOnClickListener(view1 -> {
                Intent intent = new Intent();
//                intent.putExtra(API.cityId, dataBean.getId());
                intent.putExtra(API.longtitude, dataBean.getLongitude());
                intent.putExtra(API.latitude, dataBean.getLatitude());
                intent.putExtra(API.cityName, dataBean.getName());
                intent.putExtra(API.cityId, dataBean.getId());
                setResult(1, intent);
                finish();
            });
            return myView;
        }
    }
}
