package com.kulun.energynet.ui.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kulun.energynet.MainApp;
import com.kulun.energynet.R;
import com.kulun.energynet.adapter.MyCouponInfoAdapter;
import com.kulun.energynet.databinding.CouponFragmentBinding;
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
import java.util.ArrayList;
import java.util.List;
public class CouponFragment extends Fragment {
    private CouponFragmentBinding binding;
    private int pageNo = 1;
    private MyCouponInfoAdapter adapter;
    private List<CouponUserModel.ContentBean.DataBean> dataBeanList = new ArrayList<>();
    private boolean ischongdian;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coupon_fragment, container, false);
        binding = CouponFragmentBinding.bind(view);
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
        ischongdian = getArguments().getBoolean(API.chongdian);
        adapter = new MyCouponInfoAdapter(getContext(), dataBeanList, getActivity(), ischongdian);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        loadData(true);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            pageNo = 1;
            loadData(true);
        }
    }

    private void loadData(final boolean b) {
        final String url = API.BASE_URL+API.URL_COUPON_LIST;
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
                CouponUserModel couponUserModel = GsonUtils.getInstance().fromJson(json, CouponUserModel.class);
                if (couponUserModel.isSuccess()){
                    if (b){
                        dataBeanList.clear();
                    }
                    if (couponUserModel.getContent() != null) {
                        dataBeanList.addAll(couponUserModel.getContent().getData());
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
}
