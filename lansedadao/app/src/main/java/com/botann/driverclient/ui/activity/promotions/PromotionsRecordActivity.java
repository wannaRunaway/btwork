package com.botann.driverclient.ui.activity.promotions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityPromotionsRecordBinding;
import com.botann.driverclient.model.User;
import com.botann.driverclient.model.packagerecord.PackageRecordList;
import com.botann.driverclient.model.packagerecord.RecordData;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.ActivityCouponActivity;
import com.botann.driverclient.ui.activity.MainActivity;
import com.botann.driverclient.utils.Constants;
import com.botann.driverclient.utils.DateUtils;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * created by xuedi on 2019/1/10
 */
public class PromotionsRecordActivity extends AppCompatActivity implements RecordClickinter, View.OnClickListener {
    private ActivityPromotionsRecordBinding binding;
    private RecordAdapter adapter;
    private List<RecordData> list = new ArrayList<>();
    private int page = 1;
    private boolean isPayResult = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_promotions_record);
        binding.tvBack.setOnClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordAdapter(this, list, this);
        binding.recyclerView.setAdapter(adapter);
//        binding.smartrefreshLayout.autoRefresh();
        loadData(true);
        binding.smartrefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                loadData(true);
            }
        });
        binding.smartrefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                loadData(false);
            }
        });
        isPayResult = getIntent().getBooleanExtra("isPayResult", false);
    }

    //加载数据

    /**
     * accountId	[int]	是	账户ID
     * pageSize	[int]		默认20条
     * pageNo	[int]		默认第1页
     */
    private void loadData(final boolean isRefresh) {
        String url = API.BASE_URL + API.URL_ACTIVITY_LIST;
        RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        requestParams.add("pageNo", page + "");
        Log.d(Utils.TAG, "url:" + url);
        Log.d(Utils.TAG, "请求参数:" + requestParams);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(persistentCookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                binding.smartrefreshLayout.finishRefresh();
                binding.smartrefreshLayout.finishLoadMore();
                String json = new String(responseBody);
                Log.d(Utils.TAG, "返回：" + json);
                PackageRecordList packageRecordList = GsonUtils.getInstance().fromJson(json, PackageRecordList.class);
                if (isRefresh) {
                    list.clear();
                }
                if (packageRecordList.getContent() != null && packageRecordList.getContent().getData() != null && packageRecordList.getContent().getData().size() != 0) {
                    list.addAll(packageRecordList.getContent().getData());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(PromotionsRecordActivity.this, "请求失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void click(int id) {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.setClass(this, ActivityCouponActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                backfinish();
                break;
            default:
                break;
        }
    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {
        private Context context;
        private List<RecordData> list;
        private RecordClickinter recordClickinter;

        public RecordAdapter(Context context, List<RecordData> list, RecordClickinter recordClickinter) {
            this.context = context;
            this.list = list;
            this.recordClickinter = recordClickinter;
        }

        @Override
        public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_record, null);
            RecordViewHolder holder = new RecordViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecordViewHolder holder, int position) {
            final RecordData recordData = list.get(position);
            String type = "";
            switch (recordData.getActivity_type()) {
                case 1:
                    type = "充值送活动";
                    break;
                case 2:
                    type = "消费送活动";
                    break;
                case 3:
                    type = "累计消费送活动";
                    break;
                case 4:
                    type = "排队送活动";
                    break;
                case 5:
                    type = "充电宝活动";
                    break;
                case 6:
                    type = "定时任务活动";
                    break;
                case 7:
                    type = "里程套餐";
//                    holder.tv_detail.setText("支付" + recordData.getChange_balance() + "元，剩余"+recordData.getGift_left()+"公理");
                    holder.tv_value.setText("当前剩余："+recordData.getGift_left()+"km");
                    break;
                case 10:
                    type = "按次套餐";
//                    holder.tv_detail.setText("支付" + recordData.getChange_balance() + "元，剩余"+recordData.getGift_left()+"次");
                    holder.tv_value.setText("当前剩余："+recordData.getGift_left()+"次");
                    break;
                case 11:
                    type = "费用套餐";
                    holder.tv_value.setText("当前剩余："+recordData.getGift_left()/100.00+"元");
                    break;
                default:
                    break;
            }
            if (recordData.getActivity_type() == 4 || recordData.getActivity_type() == 5){
                holder.tv_pay.setVisibility(View.GONE);
            }else {
                holder.tv_pay.setVisibility(View.VISIBLE);
            }
            if (recordData.getActivity_type()==7 || recordData.getActivity_type() == 10 || recordData.getActivity_type() == 11){
                if (recordData.getGift_left()>0){
                    holder.tv_value.setVisibility(View.VISIBLE);
                }else {
                    holder.tv_value.setVisibility(View.GONE);
                }
            }else {
                holder.tv_value.setVisibility(View.GONE);
            }
            holder.re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recordClickinter.click(recordData.getActivity_recore_id());
                }
            });
            holder.tv_time.setText(DateUtils.stampToDate(recordData.getActivity_time()));
            holder.tv_type.setText(type);
            holder.tv_name.setText(recordData.getActivity_name());
            holder.tv_pay.setText("实际支付："+recordData.getChange_balance()+"元");
            if (recordData.getPackage_start_time()==null || recordData.getPackage_start_time().equals("")){
                holder.tv_youxiaoqi.setVisibility(View.GONE);
            }else {
                holder.tv_youxiaoqi.setVisibility(View.VISIBLE);
            }
            holder.tv_youxiaoqi.setText("有效期："+ recordData.getPackage_start_time() + "~" + recordData.getPackage_end_time());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time, tv_type, tv_name, tv_pay, tv_value, tv_youxiaoqi;
        RelativeLayout re;

        public RecordViewHolder(View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_type = itemView.findViewById(R.id.tv_activity_type);
            tv_name = itemView.findViewById(R.id.tv_activity_name);
            tv_pay = itemView.findViewById(R.id.tv_pay);
            tv_value = itemView.findViewById(R.id.tv_value);
            tv_youxiaoqi = itemView.findViewById(R.id.tv_youxiaoqi);
            re = itemView.findViewById(R.id.re);
        }
    }

    @Override
    public void onBackPressed() {
        backfinish();
    }

    //点击返回，右下角返回键
    private void backfinish() {
        if (isPayResult) {
            Intent message2 = new Intent(MainApp.getInstance().getApplicationContext(), MainActivity.class);
            message2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            MainApp.getInstance().getApplicationContext().startActivity(message2);
        } else {
            finish();
        }
    }
}
