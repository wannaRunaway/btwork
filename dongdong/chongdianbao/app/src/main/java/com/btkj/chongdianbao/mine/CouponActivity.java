package com.btkj.chongdianbao.mine;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityCouponBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.model.CouponModel;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.DateUtils;
import com.btkj.chongdianbao.utils.GsonUtils;
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
public class CouponActivity extends BaseActivity {
    private ActivityCouponBinding binding;
    private Myadapter myadapter;
    private int pageNo = 1;
    private List<CouponModel.ContentBean> list = new ArrayList<>();
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this , R.layout.activity_coupon);
        binding.title.setText("优惠券");
        binding.left.setOnClickListener(view -> finish());
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

    //type[int]		默认0，可用优惠券；1，失效的优惠券
    //pageNo[int]
    //pageSize[int]
    private void loadData(boolean isRefresh) {
        String url = API.BASE_URL + API.COUPON;
        RequestParams requestParams = new RequestParams();
        Map<String,String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNo));
//        map.put("type", "0");
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, CouponActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                CouponModel model = GsonUtils.getInstance().fromJson(json, CouponModel.class);
                if (isRefresh){
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
//                CouponModel model = GsonUtils.getInstance().fromJson(json, CouponModel.class);
//                if (model.isSuccess()){
//                    if (isRefresh){
//                        list.clear();
//                    }
//                    list.addAll(model.getContent());
//                    binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
//                    myadapter.notifyDataSetChanged();
//                }else {
//                    Utils.snackbar(getApplicationContext(), CouponActivity.this, model.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                finishLoad();
//                Utils.log(url, requestParams, null);
//                Utils.snackbar(getApplicationContext(), CouponActivity.this, API.net_error);
//            }
//        });
    }

    private void finishLoad(){
        binding.smartRefresh.finishLoadMore();
        binding.smartRefresh.finishRefresh();
    }

    private class Myadapter extends RecyclerView.Adapter<MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CouponActivity.this).inflate(R.layout.adapter_coupon, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            CouponModel.ContentBean data = list.get(position);
            holder.name.setText(data.getName());
            holder.money.setText(data.getAmount()+"元");
            holder.time.setText(DateUtils.stampToYear(data.getBeginDate())+"至"+DateUtils.stampToYear(data.getExpireDate()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time, money;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time);
            money = itemView.findViewById(R.id.tv_money);
        }
    }
}
