package com.botann.driverclient.ui.fragment;

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
import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.R;
import com.botann.driverclient.databinding.RechargeFragmentBinding;
import com.botann.driverclient.model.User;
import com.botann.driverclient.model.recharge.RechargeInfo;
import com.botann.driverclient.model.recharge.RechargeModel;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.timeselector.SelectData;
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
 * Created by Orion on 2017/7/24.
 */
public class RechargeFragment extends Fragment implements View.OnClickListener {
    private RechargeFragmentBinding binding;
    private List<RechargeInfo> list = new ArrayList<>();
    private int pageNo = 1;
    private MyRechargeInfoAdapter adapter;
    private TextView tv_startTime, tv_endTime, tv_confirm;
    private AlertDialog alertDialog;
    private View dialogView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recharge_fragment, container, false);
        binding = RechargeFragmentBinding.bind(view);
        binding.tvTime.setOnClickListener(this);
        binding.tvClear.setOnClickListener(this);
        binding.tvClear.setOnClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRechargeInfoAdapter(list);
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
//        binding.smartrefreshLayout.autoRefresh();
        loadData(true);
        return view;
    }

    private void loadData(final boolean isRefresh) {
        final String url = API.BASE_URL + API.URL_RECHARGE_LIST;
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
                RechargeModel model = GsonUtils.getInstance().fromJson(json, RechargeModel.class);
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
                    dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_recharge, null);
                    tv_startTime = dialogView.findViewById(R.id.tv_start_time);
                    tv_endTime = dialogView.findViewById(R.id.tv_end_time);
                    tv_confirm = dialogView.findViewById(R.id.tv_confirm);
                    tv_confirm.setOnClickListener(this);
                    tv_startTime.setOnClickListener(this);
                    tv_endTime.setOnClickListener(this);
                    builder.setView(dialogView);
                    alertDialog = builder.create();
                }
                alertDialog.show();
                break;
            case R.id.tv_clear:
                binding.tvTime.setText("");
                if (dialogView != null) {
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
                selectData.showAtLocation(tv_endTime, Gravity.BOTTOM, 0, 0);
                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        tv_endTime.setText(year + "-" + month + "-" + day);
                    }
                });
                break;
            case R.id.tv_confirm:
                if (tv_startTime.getText().toString().isEmpty()){
                    Utils.toast(getContext(), "开始时间不能为空");
                    return;
                }
                if (tv_endTime.getText().toString().isEmpty()){
                    Utils.toast(getContext(), "结束时间不能为空");
                    return;
                }
                binding.tvTime.setText(tv_startTime.getText().toString()+"至"+tv_endTime.getText().toString());
                alertDialog.dismiss();
                pageNo=1;
                loadData(true);
                break;
            default:
                break;
        }
    }

    private class MyRechargeInfoAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<RechargeInfo> list;

        public MyRechargeInfoAdapter(List<RechargeInfo> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_recharge, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            RechargeInfo info = list.get(position);
            holder.tv_money.setText("￥ " + info.getChangeBalance());
            holder.tv_time.setText(DateUtils.stampToDate(Long.parseLong(info.getCreateTime())));
            switch (info.getBigType()) {
                case 1:
                    holder.tv_type.setText("充电充值");
                    break;
                case 0:
                    holder.tv_type.setText("换电充值");
                    break;
                default:
                    break;
            }
            holder.tv_stationname.setText(info.getName());
            //换电类型：0微信，1支付宝，2现金；     充电类型：1现金，2微信，3支付宝
            String type = "";
            switch (info.getPayType()) {
                case 0:
                    type = info.getBigType() == 1 ? "" : "微信";
                    break;
                case 1:
                    type = info.getBigType() == 1 ? "现金" : "支付宝";
                    break;
                case 2:
                    type = info.getBigType() == 1 ? "微信" : "现金";
                    break;
                case 3:
                    type = info.getBigType() == 1 ? "支付宝" : "";
                    break;
                default:
                    break;
            }
            holder.tv_recharge_type.setText(type);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_money, tv_time, tv_type, tv_stationname, tv_recharge_type;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_stationname = (TextView) itemView.findViewById(R.id.tv_stationname);
            tv_recharge_type = (TextView) itemView.findViewById(R.id.tv_charge_type);
        }
    }
    //    RefreshLayout rechargeRefresh;
//    RecyclerView rechargeInfo;
//    BaseAdapter mAdapter;
//    int loadCount;
//    int totalRecords;
//    private static boolean flag = false;
//    private List<RechargeInfo> rechargeInfoList = new ArrayList<RechargeInfo>();
//    @Override
//    public void onCreate(Bundle saveInstanceState) {
//        super.onCreate(saveInstanceState);
//        //创建被装饰者实例
//        final MyRechargeInfoAdapter adapter = new MyRechargeInfoAdapter(getActivity());
//        //创建装饰者实例，并传入被装饰者和回调接口
//        mAdapter = new RechargeInfoAdapter(adapter, new OnLoad() {
//            @Override
//            public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
//                //此处模拟做网络操作，0.5s延迟，将拉取的数据更新到adapter中
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<RechargeInfo> dataSet = new ArrayList<RechargeInfo>();
//                        if(loadCount == 0) {
//                            dataSet = rechargeInfoList;
//                        } else {
//                            //发送一次请求，加载更多，dataSet更新为新查出来的数据
//                            new Handler().post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    String url = API.BASE_URL + API.URL_RECHARGE_LIST;
//                                    RequestParams params = new RequestParams();
//                                    params.add("accountId", Constants.accountId.toString());
//                                    params.add("pageNo", loadCount + "");
//                                    params.add("pageSize", "20");
//                                    Log.d(Utils.TAG, "url为:"+url);
//                                    Log.d(Utils.TAG, "参数为:"+params.toString());
//                                    final AsyncHttpClient client = new AsyncHttpClient();
//                                    //保存cookie，自动保存到了sharepreferences
//                                    PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
//                                    client.setCookieStore(myCookieStore);
//                                    client.post(url, params, new AsyncHttpResponseHandler() {
//                                        @Override
//                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                            String json = new String(responseBody);
//                                            Log.d(Utils.TAG, "onSuccess json = " + json);
//                                            JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
//                                            JsonObject content = obj.get("content").getAsJsonObject();
//                                            JsonArray data = content.get("data").getAsJsonArray();
//                                            String res = data.toString();
//                                            if(res != null) {
//                                                SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("recharge",MODE_PRIVATE);
//                                                SharedPreferences.Editor editor = sp.edit();
//                                                editor.putString("rechargeInfoLoadMoreList", res);
//                                                editor.commit();
//                                                flag = true;
//                                            }
//                                        }
//                                        @Override
//                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
//                                            Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
//                                            Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            });
//                            if(flag) {
//                                SharedPreferences sp =  MainApp.getInstance().getApplicationContext().getSharedPreferences("recharge",MODE_PRIVATE);
//                                String res = sp.getString("rechargeInfoLoadMoreList", "");
//                                Gson gson = new Gson();
//                                dataSet = gson.fromJson(res, new TypeToken<List<RechargeInfo>>(){}.getType());
//                            }
//                        }
//
//                        //数据的处理最终还是交给被装饰的adapter来处理
//                        adapter.appendData(dataSet);
//                        callback.onSuccess();
//                        //模拟加载到没有更多数据的情况，触发onFailure
//                        if(loadCount++ == (totalRecords/20 + 1)){
//                            callback.onFailure();
//                        }
//                    }
//                },500);
//            }
//        });
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.recharge_fragment, container, false);
//        rechargeRefresh = (RefreshLayout) view.findViewById(R.id.recharge_swipe_refresh);
//        rechargeInfo = (RecyclerView) view.findViewById(R.id.recharge_list);
//        rechargeRefresh.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        final List<RechargeInfo> dataSet = new ArrayList<RechargeInfo>();
//                        String url = API.BASE_URL+API.URL_RECHARGE_LIST;
//                        RequestParams params = new RequestParams();
//                        params.add("accountId", Constants.accountId.toString());
//                        params.add("pageNo", "1");
//                        params.add("pageSize", "20");
//                        Log.d(Utils.TAG, "请求的url："+url);
//                        Log.d(Utils.TAG, "请求参数："+params.toString());
//                        final AsyncHttpClient client = new AsyncHttpClient();
//                        //保存cookie，自动保存到了sharepreferences
//                        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
//                        client.setCookieStore(myCookieStore);
//                        client.post(url, params, new AsyncHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                                String json = new String(response);
//                                Integer total;
//                                String res;
//                                Log.d(Utils.TAG, "onSuccess json = " + json);
//                                //刷新完成
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
//                                    SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("recharge",MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sp.edit();
//                                    editor.putString("rechargeInfoList",res);
//                                    editor.putInt("total",total);
//                                    editor.commit();
//                                }
//                                flag = false;
//                            }
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
//                                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
//                                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }, 500);
//                rechargeRefresh.finishRefresh(500);
//            }
//        });
//        rechargeInfo.setAdapter(mAdapter);
//        rechargeInfo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        return view;
//    }
}
