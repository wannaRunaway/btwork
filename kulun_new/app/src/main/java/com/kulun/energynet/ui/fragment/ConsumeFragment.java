package com.kulun.energynet.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kulun.energynet.R;
import com.kulun.energynet.adapter.MyConsumeInfoAdapter;
import com.kulun.energynet.databinding.ConsumeFragmentBinding;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.consume.ConsumeInfo;
import com.kulun.energynet.model.consume.ConsumeModel;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.timeselector.SelectData;
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

/**
 * Created by Orion on 2017/7/18.
 */
public class ConsumeFragment extends Fragment implements View.OnClickListener {
    private ConsumeFragmentBinding binding;
    private List<ConsumeInfo> list = new ArrayList<>();
    private MyConsumeInfoAdapter adapter;
    private int pageNo = 1;
    private TextView tv_startTime, tv_endTime, tv_confirm;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consume_fragment, container, false);
        binding = ConsumeFragmentBinding.bind(view);
        binding.tvTime.setOnClickListener(this);
        binding.tvClear.setOnClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyConsumeInfoAdapter(list, getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.smartrefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                loadData(true);
                loadCommon();
            }
        });
        binding.smartrefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo = pageNo + 1;
                loadData(false);
            }
        });
//        binding.smartrefreshLayout.autoRefresh();
        loadData(true);
        loadCommon();
        return view;
    }

    private void loadCommon() {
        final String url = API.BASE_URL + API.URL_EXCHANGE_COMMON;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        if (tv_startTime != null && tv_endTime != null) {
            if (!tv_startTime.getText().toString().isEmpty() && !tv_endTime.getText().toString().isEmpty()) {
                requestParams.add("startTime", tv_startTime.getText().toString().replaceAll("-", ""));
                requestParams.add("endTime", tv_endTime.getText().toString().replaceAll("-", ""));
            }
        }
        PersistentCookieStore cookieStore = new PersistentCookieStore(getContext());
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                ConsumeAllModel model = GsonUtils.getInstance().fromJson(json, ConsumeAllModel.class);
                if (model.isSuccess() && model.getContent() != null) {
                    binding.tvShishou.setText("?????????" + model.getContent().getTotalRealFare());
                    binding.tvCishu.setText("???????????????" + model.getContent().getCount());
                    binding.tvYouhuquan.setText("????????????" + model.getContent().getTotalCoupon());
                    binding.tvTuikuan.setText("???????????????" + model.getContent().getTotalRefundMoney());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
            }
        });
    }

    private void loadData(final boolean isRefresh) {
        /**
         * accountId[int]	???	??????id
         * bigType[int]		???????????? 1?????? 0??????
         * stationId[int]		??????id
         * startTime[string]		20190301
         * endTime[string]		20190601
         * pageNo[int]		??? ??????1
         * pageSize[int]		???/??? ??????20
         */
        final String url = API.BASE_URL + API.URL_EXCHANGE_LIST;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        if (tv_startTime != null && tv_endTime != null) {
            if (!tv_startTime.getText().toString().isEmpty() && !tv_endTime.getText().toString().isEmpty()) {
                requestParams.add("startTime", tv_startTime.getText().toString().replaceAll("-", ""));
                requestParams.add("endTime", tv_endTime.getText().toString().replaceAll("-", ""));
            }
        }
        requestParams.add("pageNo", String.valueOf(pageNo));
        PersistentCookieStore cookieStore = new PersistentCookieStore(getContext());
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                binding.smartrefreshLayout.finishRefresh();
                binding.smartrefreshLayout.finishLoadMore();
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                ConsumeModel model = GsonUtils.getInstance().fromJson(json, ConsumeModel.class);
                if (isRefresh) {
                    list.clear();
                }
                if (model.getContent() != null) {
                    list.addAll(model.getContent().getData());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.smartrefreshLayout.finishRefresh();
                binding.smartrefreshLayout.finishLoadMore();
                Utils.toast(getContext(), API.error_internet);
            }
        });
    }

    @Override
    public void onClick(View view) {
        SelectData selectData = null;
        switch (view.getId()) {
            case R.id.tv_time:
                if (alertDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_recharge, null);
                    tv_startTime = view1.findViewById(R.id.tv_start_time);
                    tv_endTime = view1.findViewById(R.id.tv_end_time);
                    tv_confirm = view1.findViewById(R.id.tv_confirm);
                    tv_startTime.setOnClickListener(this);
                    tv_endTime.setOnClickListener(this);
                    tv_confirm.setOnClickListener(this);
                    builder.setView(view1);
                    alertDialog = builder.create();
                }
                alertDialog.show();
                break;
            case R.id.tv_clear:
                binding.tvTime.setText("");
                if (alertDialog != null) {
                    tv_startTime.setText("");
                    tv_endTime.setText("");
                }
                break;
            case R.id.tv_start_time:
                selectData = new SelectData(getContext(), false);
                selectData.showAtLocation(tv_startTime, Gravity.BOTTOM, 0, 0);
                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        tv_startTime.setText(year + "-" + month + "-" + day);
                    }
                });
                break;
            case R.id.tv_end_time:
                selectData = new SelectData(getContext(), false);
                selectData.showAtLocation(tv_startTime, Gravity.BOTTOM, 0, 0);
                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        tv_endTime.setText(year + "-" + month + "-" + day);
                    }
                });
                break;
            case R.id.tv_confirm:
                if (tv_startTime.getText().toString().isEmpty()) {
                    Utils.toast(getContext(), "????????????????????????");
                    return;
                }
                if (tv_endTime.getText().toString().isEmpty()) {
                    Utils.toast(getContext(), "????????????????????????");
                    return;
                }
                binding.tvTime.setText(tv_startTime.getText().toString() + "???" + tv_endTime.getText().toString());
                alertDialog.dismiss();
                pageNo = 1;
                loadData(true);
                loadCommon();
                break;
            default:
                break;
        }
    }
//
//    private class MyConsumeInfoAdapter extends RecyclerView.Adapter<MyViewHolder> {
//        private List<ConsumeInfo> list;
//        public MyConsumeInfoAdapter(List<ConsumeInfo> list) {
//            this.list = list;
//        }
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_consume, null);
//            return new MyViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position) {
//            holder.itemView
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//    }
//
//    private class MyViewHolder extends RecyclerView.ViewHolder {
//        public MyViewHolder(View itemView) {
//            super(itemView);
//        }
//
//    }
    //    RefreshLayout consumeRefresh;
//    RecyclerView consumeInfo;
//    ConsumeInfoAdapter mAdapter;
//    int loadCount;
//    int totalRecords;
//    private static boolean flag = false;
//    //??????????????????
//    private List<ConsumeInfo> consumeInfoList = new ArrayList<ConsumeInfo>();
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //????????????????????????
////        final MyConsumeInfoAdapter adapter = new MyConsumeInfoAdapter(getActivity(), isCustomerNotNull());
//        final MyConsumeInfoAdapter adapter = new MyConsumeInfoAdapter(getActivity());
//        //????????????????????????????????????????????????????????????
//        mAdapter = new ConsumeInfoAdapter(adapter, new OnLoad() {
//            @Override
//            public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
//                //??????????????????????????????0.5s????????????????????????????????????adapter???
//                new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            List<ConsumeInfo> dataSet = new ArrayList<ConsumeInfo>();
//                            if(loadCount == 0) {
//                                dataSet = consumeInfoList;
//                            } else {
//                                //????????????????????????????????????dataSet??????????????????????????????
//                                new Handler().post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String url = API.BASE_URL + API.URL_EXCHANGE_LIST;
//                                        RequestParams params = new RequestParams();
//                                        params.add("accountId", Constants.accountId.toString());
//                                        params.add("pageNo", loadCount  + "");
//                                        params.add("pageSize", "20");
//                                        final AsyncHttpClient client = new AsyncHttpClient();
//                                        //??????cookie?????????????????????sharepreferences
//                                        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
//                                        client.setCookieStore(myCookieStore);
//                                        client.post(url, params, new AsyncHttpResponseHandler() {
//                                            @Override
//                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                                String json = new String(responseBody);
//                                                Log.d(Utils.TAG, "onSuccess json = " + json);
//                                                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
//                                                JsonObject content = obj.get("content").getAsJsonObject();
//                                                JsonArray data = content.get("data").getAsJsonArray();
//                                                String res = data.toString();
//                                                if(res != null) {
//                                                    SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("consume",MODE_PRIVATE);
//                                                    SharedPreferences.Editor editor = sp.edit();
//                                                    editor.putString("consumeInfoLoadMoreList", res);
//                                                    editor.commit();
//                                                    flag = true;
//                                                }
//                                            }
//                                            @Override
//                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
//                                                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
//                                                Toast.makeText(MainApp.getInstance().getApplicationContext(), "???????????????????????????", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                });
//
//                                if(flag) {
//                                    SharedPreferences sp =  MainApp.getInstance().getApplicationContext().getSharedPreferences("consume",MODE_PRIVATE);
//                                    String res = sp.getString("consumeInfoLoadMoreList", "");
//                                    Gson gson = new Gson();
//                                    dataSet = gson.fromJson(res, new TypeToken<List<ConsumeInfo>>(){}.getType());
//                                }
//                            }
//                            //?????????????????????????????????????????????adapter?????????
//                            adapter.appendData(dataSet);
//                            callback.onSuccess();
//                            //???????????????????????????????????????????????????onFailure
//                            if(loadCount++ == (totalRecords/20 + 1)){
//                                callback.onFailure();
//                            }
//                        }
//                },500);
//            }
//        });
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.consume_fragment, container, false);
//        consumeRefresh = (RefreshLayout) view.findViewById(R.id.consume_swipe_refresh);
//        consumeInfo = (RecyclerView) view.findViewById(R.id.consume_list);
//        consumeRefresh.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = API.BASE_URL+API.URL_EXCHANGE_LIST;
//                        RequestParams params = new RequestParams();
//                        params.add("accountId", Constants.accountId.toString());
//                        params.add("pageNo", "1");
//                        params.add("pageSize", "20");
//                        final AsyncHttpClient client = new AsyncHttpClient();
//                        //??????cookie?????????????????????sharepreferences
//                        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
//                        client.setCookieStore(myCookieStore);
//                        client.post(url, params, new AsyncHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                                String json = new String(response);
//                                Integer total;
//                                String res;
//                                Log.d(Utils.TAG, "onSuccess json = " + json);
//                                //????????????
//                                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
//                                Integer code = obj.get("code").getAsInt();
//                                if(code == 0) {
//                                    JsonObject content = obj.get("content").getAsJsonObject();
//                                    JsonArray data = content.get("data").getAsJsonArray();
//                                    total = content.get("total").getAsInt();
//                                    res = data.toString();
//                                }else{
//                                    total = 0;
//                                    res = null;
//                                }
//                                if(res != null) {
//                                    SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("consume",MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sp.edit();
//
//                                    editor.putString("consumeInfoList",res);
//                                    editor.putInt("total",total);
//                                    editor.commit();
//                                }
//                                flag = false;
//                                consumeRefresh.finishRefresh(500);
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
//                                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
//                                Toast.makeText(MainApp.getInstance().getApplicationContext(), "???????????????????????????", Toast.LENGTH_SHORT).show();
//                            }
//
//                        });
//                    }
//                },500);
//            }
//        });
//        consumeInfo.setAdapter(mAdapter);
//        consumeInfo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        return view;
//    }
}
