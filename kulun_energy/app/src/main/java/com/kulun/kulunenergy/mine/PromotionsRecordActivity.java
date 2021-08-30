package com.kulun.kulunenergy.mine;
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
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityPromotionsRecordBinding;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.modelnew.PackageRecordList;
import com.kulun.kulunenergy.modelnew.RecordData;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.DateUtils;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.utils.Utils;
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
public class PromotionsRecordActivity extends AppCompatActivity implements  View.OnClickListener {
    private ActivityPromotionsRecordBinding binding;
    private RecordAdapter adapter;
    private List<RecordData> list = new ArrayList<>();
    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_promotions_record);
        binding.header.title.setText("活动记录");
        binding.header.left.setOnClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordAdapter(this, list);
        binding.recyclerView.setAdapter(adapter);
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
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId(this)));
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            default:
                break;
        }
    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {
        private Context context;
        private List<RecordData> list;

        public RecordAdapter(Context context, List<RecordData> list) {
            this.context = context;
            this.list = list;
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
            if (recordData.getActivity_type() == 4 || recordData.getActivity_type() == 5){
                holder.tv_pay.setVisibility(View.GONE);
            }else {
                holder.tv_pay.setVisibility(View.VISIBLE);
            }
            holder.tv_time.setText(DateUtils.stampToDate(recordData.getActivity_time()));
            holder.tv_name.setText(recordData.getActivity_name());
            holder.tv_pay.setText("实际支付："+recordData.getChange_balance()+"元");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time, tv_name, tv_pay;

        public RecordViewHolder(View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_name = itemView.findViewById(R.id.tv_activity_name);
            tv_pay = itemView.findViewById(R.id.tv_pay);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
