package com.botann.driverclient.ui.activity.refund;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityRefundListBinding;
import com.botann.driverclient.model.RefundModel;
import com.botann.driverclient.model.User;
import com.botann.driverclient.network.api.API;
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
 * created by xuedi on 2019/12/9
 */
public class ReFundListActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityRefundListBinding binding;
    private int pageNo = 1;
    private List<RefundModel.ContentBean.DataBean> list = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_refund_list);
        binding.title.title.setText("退款记录");
        binding.title.back.setOnClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ReFundListActivity.this));
        adapter = new MyAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.smartrefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                loadData(true);
            }
        });
        binding.smartrefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo = pageNo + 1;
                loadData(false);
            }
        });
        loadData(true);
    }

    private void loadData(final boolean isRefresh) {
        /**
         * accountId[int]	是	账户id
         * pageNo[int]	是
         * pageSize[string]	是
         */
        String url = API.BASE_URL + API.URL_REFUND_LIST;
        RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        params.add("pageNo", String.valueOf(pageNo));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(ReFundListActivity.this);
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                finishLoad();
                String json = new String(responseBody);
                Utils.log(null, "", json);
                if (isRefresh) {
                    list.clear();
                }
                RefundModel refundModel = GsonUtils.getInstance().fromJson(json, RefundModel.class);
                if (refundModel.isSuccess()) {
                    list.addAll(refundModel.getContent().getData());
                } else {
                    if (refundModel.getMsg() != null) {
                        Utils.toast(ReFundListActivity.this, refundModel.getMsg());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String json = new String(responseBody);
                Utils.log(null, "", json);
                finishLoad();
            }
        });
    }

    private void finishLoad(){
        binding.smartrefreshLayout.finishRefresh();
        binding.smartrefreshLayout.finishLoadMore();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
    private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ReFundListActivity.this).inflate(R.layout.adapter_refund, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            RefundModel.ContentBean.DataBean dataBean = list.get(position);
            if (dataBean.getRefundApplyType() == 4) {
                holder.title.setText("充电退款");
            }
            //type=0支付宝 1微信
            switch (dataBean.getType()) {
                case 0:
                    holder.type.setText("支付宝");
                    break;
                case 1:
                    holder.type.setText("微信");
                    break;
                default:
                    break;

            }
            holder.time.setText(DateUtils.stampToDate(dataBean.getRefundTime())+"");
            holder.money.setText(dataBean.getAmount()+"元");
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, type, time, money;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            time = itemView.findViewById(R.id.time);
            money = itemView.findViewById(R.id.money);
        }
    }
}
