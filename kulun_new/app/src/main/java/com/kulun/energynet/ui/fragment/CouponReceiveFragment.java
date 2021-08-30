package com.kulun.energynet.ui.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kulun.energynet.MainApp;
import com.kulun.energynet.R;
import com.kulun.energynet.adapter.MyCouponInfoAdapter;
import com.kulun.energynet.databinding.CouponFragmentBinding;
import com.kulun.energynet.databinding.CouponReceiveFragmentBinding;
import com.kulun.energynet.model.BaseObject;
import com.kulun.energynet.model.CouponReceiveModel;
import com.kulun.energynet.model.CouponUserModel;
import com.kulun.energynet.model.User;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * created by xuedi on 2019/9/29
 */
public class CouponReceiveFragment extends Fragment {
    private CouponReceiveFragmentBinding binding;
    private int pageNo = 1;
    private ReceiverAdapter adapter;
    private List<CouponReceiveModel.ContentBean.DataBean> list = new ArrayList<>();
    private static DecimalFormat df = new DecimalFormat("######0.00");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coupon_receive_fragment, container, false);
        binding = CouponReceiveFragmentBinding.bind(view);
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
        adapter = new ReceiverAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        loadData(true);
        return view;
    }

    private void loadData(final boolean b) {
        final String url = API.BASE_URL+API.COUPON_AVA;
        final RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        params.add("pageNo", String.valueOf(pageNo));
        params.add("pageSize","20");
        final AsyncHttpClient client = new AsyncHttpClient();
        //保存cookie，自动保存到了sharepreferences
        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
        client.setCookieStore(myCookieStore);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                binding.smartrefreshLayout.finishRefresh();
                binding.smartrefreshLayout.finishLoadMore();
                String json = new String(response);
                Utils.log(url,params,json);
                CouponReceiveModel couponUserModel = GsonUtils.getInstance().fromJson(json, CouponReceiveModel.class);
                if (couponUserModel.isSuccess()){
                    if (b){
                        list.clear();
                    }
                    if (couponUserModel.getContent() != null) {
                        list.addAll(couponUserModel.getContent().getData());
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    Utils.toast(getContext(), couponUserModel.getMsg()+"");
                }
                Utils.log(url,params,json);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                binding.smartrefreshLayout.finishRefresh();
                binding.smartrefreshLayout.finishLoadMore();
                Utils.log(url, params, "");
            }
        });
    }
    private class ReceiverAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.adapter_receive, parent, false);
            return new MyViewHolder(v);
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final CouponReceiveModel.ContentBean.DataBean dataBean = list.get(position);
            holder.money.setText(dataBean.getAmount()+"元");
            holder.shengyu.setText(dataBean.getName()+"");
            if (dataBean.getCouponId() == 0){
                holder.lingqu.setImageDrawable(getResources().getDrawable(R.drawable.button_receive));
                holder.lingqu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lingqu(dataBean.getId(), position, list);
                    }
                });
            }else {
                holder.lingqu.setImageDrawable(getResources().getDrawable(R.drawable.button_have_received));
                holder.lingqu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.toast(getContext(), "优惠券已经领取了");
                    }
                });
            }
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private void lingqu(int dataBeanId, final int position, final List<CouponReceiveModel.ContentBean.DataBean> list) {
        /**
         * accountId
         * [int]	是	账户id
         * couponTemplateId
         * [int]	是	优惠券模板id
         */
        final String url = API.BASE_URL + API.COUPON_RECEIVE;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        requestParams.add("couponTemplateId", String.valueOf(dataBeanId));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(getContext());
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url,requestParams,json);
                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
                if (baseObject.isSuccess()){
                    list.get(position).setCouponId(1);
                    adapter.notifyDataSetChanged();
                }else {
                    Utils.toast(getContext(), baseObject.getMsg()+"");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.log(url,requestParams, null);
            }
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView money, shengyu;
        private ImageView lingqu;
        public MyViewHolder(View itemView) {
            super(itemView);
            money = itemView.findViewById(R.id.money);
            shengyu = itemView.findViewById(R.id.shengyu);
            lingqu = itemView.findViewById(R.id.lingqu);
        }
    }
}
