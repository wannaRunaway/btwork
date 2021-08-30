package com.botann.driverclient.ui.fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.navi.INaviInfoCallback;
import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.adapter.ChongdianAdapter;
import com.botann.driverclient.model.User;
import com.botann.driverclient.model.chongdian.ChongdianInfo;
import com.botann.driverclient.model.chongdian.ChongdianlistModel;
import com.botann.driverclient.model.huandian.StationInfo;
import com.botann.driverclient.model.loginmodel.CityNametoIdModel;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.utils.Constants;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.SharePref;
import com.botann.driverclient.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * created by xuedi on 2019/6/26
 */
public class ChongdianFragment extends Fragment {
    public SmartRefreshLayout refresh;
    private RecyclerView recyclerView;
    private ChongdianAdapter mAdapter;
    public int pageNo = 1;
    public List<ChongdianInfo> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chongdian, null);
        refresh = (SmartRefreshLayout) view.findViewById(R.id.refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ChongdianAdapter(getActivity(), list, (INaviInfoCallback) getParentFragment());
        recyclerView.setAdapter(mAdapter);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNo = 1;
//                refreshUI();
                ((StationFragment)getParentFragment()).requestMapPermissions();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo = pageNo + 1;
                loadMore();
            }
        });
        if (!((StationFragment)ChongdianFragment.this.getParentFragment()).islocationfinish){//定位没有完成，这界面打开就是加载。定位完成，直接刷新
            refresh.autoRefreshAnimationOnly();
        }else {
            pageNo = 1;
            refreshUI();
        }
//        refresh.autoRefresh();
//        refreshUI();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                refreshUI();
//            }
//        }, 3000);
        return view;
    }

    private void loadMore() {
        final String url = API.BASE_URL + API.CHONGDIAN_LIST;
        final RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
//        params.add("cityId", Constants.cityId);
        params.add("cityId", String.valueOf((int)SharePref.get(getContext(), API.cityId, 0)));
//        params.add("cityId", String.valueOf(User.getInstance().getCityId()));
        params.add("pageNo", String.valueOf(pageNo));
        params.add("pageSize", "100");
        AsyncHttpClient client = new AsyncHttpClient();
        //保存cookie，自动保存到了sharepreferences
        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
        client.setCookieStore(myCookieStore);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                refresh.finishLoadMore();
                refresh.finishRefresh();
                String json = new String(response);
                Utils.log(url, params, json);
                ChongdianlistModel model = GsonUtils.getInstance().fromJson(json, ChongdianlistModel.class);
                if (model.isSuccess()) {
                    if (model.getContent() != null) {
                        list.addAll(model.getContent().getData());
                    }
                    final LatLng start = new LatLng(Double.longBitsToDouble(((long) SharePref.get(getContext(), API.lat, 0l))), Double.longBitsToDouble(((long) SharePref.get(getContext(), API.lon, 0l))));
                    Collections.sort(list, new Comparator<ChongdianInfo>() {
                        @Override
                        public int compare(ChongdianInfo c1, ChongdianInfo c2) {
                            LatLng end1 = new LatLng(c1.getStationLat(), c1.getStationLng());
                            LatLng end2 = new LatLng(c2.getStationLat(), c2.getStationLng());
                            return c1.getMydistance(start, end1).compareTo(c2.getMydistance(start,end2));
                        }
                    });
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                refresh.finishLoadMore();
                refresh.finishRefresh();
                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
            }
        });
    }

    public void refreshUI() {
        final String url = API.BASE_URL + API.URL_CITYID;
        final RequestParams params = new RequestParams();
//        params.add("name", Constants.cityname);
        params.add("name", (String) SharePref.get(getContext(), API.city, ""));
//        params.add("name", User.getInstance().getCityName());
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                Utils.log(url, params, json);
                CityNametoIdModel model = GsonUtils.getInstance().fromJson(json, CityNametoIdModel.class);
                if (model.isSuccess()) {
//                    Constants.cityId = String.valueOf(model.getContent().getId());
//                    User.getInstance().setCityId(model.getContent().getId());
                    SharePref.put(getContext(), API.cityId, model.getContent().getId());
//                    double latdouble = Double.parseDouble(model.getContent().getLat());
//                    double lngdouble = Double.parseDouble(model.getContent().getLng());
//                    Constants.mylatitude = latdouble;
//                    Constants.mylongitude = lngdouble;
//                    User.getInstance().setLatitude(latdouble);
//                    User.getInstance().setLongtitude(lngdouble);
                } else {
                    refresh.finishRefresh();
                    refresh.finishLoadMore();
                    return;
                }
                final String url = API.BASE_URL + API.CHONGDIAN_LIST;
                final RequestParams params = new RequestParams();
                params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
                params.add("cityId", String.valueOf((int)SharePref.get(getContext(), API.cityId, 0)));
//                params.add("cityId", String.valueOf(User.getInstance().getCityId()));
//                params.add("cityId", Constants.cityId);
                params.add("pageNo", "1");
                params.add("pageSize", "100");
                AsyncHttpClient client = new AsyncHttpClient();
                //保存cookie，自动保存到了sharepreferences
                PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                client.setCookieStore(myCookieStore);
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                        String json = new String(response);
                        Utils.log(url, params, json);
                        ChongdianlistModel model = GsonUtils.getInstance().fromJson(json, ChongdianlistModel.class);
                        if (model.isSuccess()) {
                            list.clear();
                            list.addAll(model.getContent().getData());
                            final LatLng start = new LatLng(Double.longBitsToDouble(((long) SharePref.get(getContext(), API.lat, 0l))), Double.longBitsToDouble(((long) SharePref.get(getContext(), API.lon, 0l))));
                            Collections.sort(list, new Comparator<ChongdianInfo>() {
                                @Override
                                public int compare(ChongdianInfo c1, ChongdianInfo c2) {
                                    LatLng end1 = new LatLng(c1.getStationLat(), c1.getStationLng());
                                    LatLng end2 = new LatLng(c2.getStationLat(), c2.getStationLng());
                                    return c1.getMydistance(start, end1).compareTo(c2.getMydistance(start,end2));
                                }
                            });
                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                        Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
//                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                refresh.finishLoadMore();
                refresh.finishRefresh();
                Utils.toast(getActivity(), "请求失败");
            }
        });
    }
}
