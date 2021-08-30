package com.kulun.kulunenergy.main;

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

import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityChoseBinding;
import com.kulun.kulunenergy.model.CityListModel;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.Customize;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.requestparams.Myparams;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChoseCityActivity extends BaseActivity {
    private ActivityChoseBinding binding;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_chose);
        binding.header.title.setText("选择城市");
        binding.header.left.setOnClickListener(view -> finish());
        if(TextUtils.isEmpty(User.getInstance().getCityName())){
            binding.citySelect.setText("定位失败");
        }else {
            if(User.getInstance().getCityName().equals("null")){
                binding.citySelect.setText("定位失败");
            }else {
                binding.citySelect.setText(User.getInstance().getCityName());
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
        final String url = API.BASE_URL + API.CITYLIST;
        Map<String, String> map = new HashMap<>();
        final RequestParams requestParams = new RequestParams();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, ChoseCityActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                CityListModel model = GsonUtils.getInstance().fromJson(json, CityListModel.class);
                binding.gridview.setAdapter(new MyAdapter(model.getContent()));
            }
        }, binding.smartRefresh);
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.setCookieStore(cookieStore);
//        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                binding.smartRefresh.finishRefresh();
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                CityListModel model = GsonUtils.getInstance().fromJson(json, CityListModel.class);
//                binding.gridview.setAdapter(new MyAdapter(model.getContent()));
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                binding.smartRefresh.finishRefresh();
//                Utils.snackbar(getApplicationContext(),ChoseCityActivity.this, API.net_error);
//                Utils.log(url, requestParams, "");
//            }
//        });
    }

    private class MyAdapter extends BaseAdapter {
        private List<CityListModel.DataBean> list;
        public MyAdapter(List<CityListModel.DataBean> content) {
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
            CityListModel.DataBean dataBean = list.get(i);
            View myView = LayoutInflater.from(ChoseCityActivity.this).inflate(R.layout.gridview_item, null);
            TextView carplate = myView.findViewById(R.id.carplate);
            carplate.setText(dataBean.getName());
            carplate.setOnClickListener(view1 -> {
                Intent intent = new Intent();
                intent.putExtra(API.cityId, dataBean.getId());
                intent.putExtra(API.longtitude, dataBean.getLng());
                intent.putExtra(API.latitude, dataBean.getLat());
                intent.putExtra(API.cityName, dataBean.getName());
                setResult(1, intent);
                finish();
            });
            return myView;
        }
    }
}
