package com.btkj.chongdianbao.mine;

import android.content.Intent;
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
import com.btkj.chongdianbao.databinding.ActivityMyActivityBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.model.ActivityModel;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.DateUtils;
import com.btkj.chongdianbao.utils.GsonUtils;
import com.btkj.chongdianbao.utils.L;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyActivityListActivity extends BaseActivity {

    private ActivityMyActivityBinding binding;
    private int pageNo = 1;
    private Myadapter myadapter;
    private List<ActivityModel.ContentBean> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_activity);
        binding.header.title.setText("活动列表");
        binding.header.left.setOnClickListener(view -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myadapter = new Myadapter();
        binding.recyclerView.setAdapter(myadapter);
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

    //pageNo[int]	是	第几页，默认1
    //pageSize[int]	是	页/条，默认20
    private void loadData(boolean isRefresh) {
        String url = API.BASE_URL + API.ACTIVITYLIST;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNo));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, MyActivityListActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                ActivityModel model = GsonUtils.getInstance().fromJson(json, ActivityModel.class);
                if (isRefresh) {
                    list.clear();
                }
                list.addAll(model.getContent());
                binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
                myadapter.notifyDataSetChanged();
            }
        },binding.smartRefresh);
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                finishLoad();
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                ActivityModel model = GsonUtils.getInstance().fromJson(json, ActivityModel.class);
//                if (model.isSuccess()) {
//                    if (isRefresh) {
//                        list.clear();
//                    }
//                    list.addAll(model.getContent());
//                    binding.imgageBack.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
//                    myadapter.notifyDataSetChanged();
//                } else {
//                    Utils.snackbar(getApplicationContext(), MyActivityListActivity.this, model.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                finishLoad();
//                Utils.log(url, requestParams, null);
//                Utils.snackbar(getApplicationContext(), MyActivityListActivity.this, API.net_error);
//            }
//        });
    }

    private void finishLoad() {
        binding.smartRefresh.finishLoadMore();
        binding.smartRefresh.finishRefresh();
    }
    private class Myadapter extends RecyclerView.Adapter<MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MyActivityListActivity.this).inflate(R.layout.adapter_activity, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ActivityModel.ContentBean data = list.get(position);
            holder.name.setText("活动名称："+data.getName());
            holder.time.setText("活动时间："+DateUtils.stampToYear(data.getStartTime())+"至"+DateUtils.stampToYear(data.getEndTime()));
            holder.re.setOnClickListener(view -> {
                Intent intent = new Intent(MyActivityListActivity.this, MyActivityDetailActivity.class);
                intent.putExtra("data", list.get(position));
                startActivity(intent);
            });
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time;
        private RelativeLayout re;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time);
            re = itemView.findViewById(R.id.re);
        }
    }
}
