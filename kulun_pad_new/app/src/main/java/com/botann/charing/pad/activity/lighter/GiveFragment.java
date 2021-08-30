package com.botann.charing.pad.activity.lighter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.botann.charging.pad.R;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.model.batterylighter.BatteryLighter;
import com.botann.charing.pad.model.batterylighter.BatteryLighterList;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * created by xuedi on 2018/11/30
 */
public class GiveFragment extends Fragment {
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<BatteryLighter> list = new ArrayList<>();
    private GiveAdapter adapter;
    private int pageNo = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_give, container, false);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        refreshLayout.autoRefresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GiveAdapter(getContext(), list, new GiveClickInter() {
            @Override
            public void click(BatteryLighter batteryLighter) {
                /**
                 * 查看
                 */
                Intent intent = new Intent();
                batteryLighter.setIsgive(true);
                intent.putExtra("batteryLighter", batteryLighter);
                intent.setClass(getContext(), BatteryLighterInquireDetailsActivity.class);
                startActivity(intent);
            }

            @Override
            public void modify(BatteryLighter batteryLighter) {
                /**
                 * 修改
                 */
                Intent intent = new Intent();
                intent.putExtra("batteryLighter", batteryLighter);
                intent.putExtra("station", getArguments().getString("station"));
                intent.setClass(getContext(), BatteryLighterActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                loadData(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData(false);
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData(final boolean isRefresh) {
        URLParams urlParams = new URLParams();
        urlParams.put("querySiteId", User.shared().getStationId());
        urlParams.put("queryType", 1);
        urlParams.put("pageNo", pageNo);
        SGHTTPManager.POST(API.URL_LIGHTER_LIST, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                if (isSuccess) {
                    BatteryLighterList batteryLighterList = SGFetchModel.getGson().fromJson(fetchModel.getJsonObject().toString(), BatteryLighterList.class);
                    if (isRefresh) {
                        list.clear();
                    }
                    list.addAll(batteryLighterList.getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), userInfo, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
