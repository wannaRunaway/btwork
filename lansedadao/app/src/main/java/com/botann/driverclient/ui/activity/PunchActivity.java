package com.botann.driverclient.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.BaseObjectString;
import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityPunchBinding;
import com.botann.driverclient.db.SharedPreferencesHelper;
import com.botann.driverclient.model.BaseObject;
import com.botann.driverclient.model.BindCar;
import com.botann.driverclient.model.CloclModel;
import com.botann.driverclient.model.MyObject;
import com.botann.driverclient.model.PunchModel;
import com.botann.driverclient.model.User;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.utils.DateUtils;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.ToastUtil;
import com.botann.driverclient.utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * created by xuedi on 2019/10/29
 */
public class PunchActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityPunchBinding binding;
    private int mile, driverClockId;
    private int pageNo = 1;
    private String name;
    private List<PunchModel.ContentBean.DataBean> list = new ArrayList<>();
    private MyAdapter adapter;
    private String vin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_punch);
        binding.tvBack.setOnClickListener(this);
        binding.clock.setOnClickListener(this);
        binding.clock.setClickable(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(PunchActivity.this));
        punch(0, 0, 0); //获取上次的打卡状态
        adapter = new MyAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.smartrefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                loadData(true);
                punch(0, 0, 0);
            }
        });
        binding.smartrefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo = pageNo + 1;
                loadData(false);
            }
        });
        SharedPreferences sharedPreferences = MainApp.getInstance().getSharedPreferences("carplate", Context.MODE_PRIVATE);
        vin = sharedPreferences.getString("vin" + User.getInstance().getAccountId(), "");
    }

    private boolean isshowtoast = false;
    private void punch(int type, int miles, int driverClockIds) {
        /**
         * accountId     [string]	是	司机id
         * status        [int]	是	0获取车辆最新打卡记录，status=1取消，2申请（下班），4确认（下班，上班），8驳回（下班）
         * mile           [int]		车辆里程，status=2时必填
         * driverClockId [string]		打卡记录id，status=1,4,8时必填
         */
        isshowtoast = false;
        final String url = API.BASE_URL + API.PUNCH;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        requestParams.add("status", String.valueOf(type));
        if (type == 2) {
            requestParams.add("mile", String.valueOf(miles));
        }
        if (type == 1 || type == 4 || type == 8) {
            requestParams.add("driverClockId", String.valueOf(driverClockIds));
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                CloclModel cloclModel = GsonUtils.getInstance().fromJson(json, CloclModel.class);
                /**
                 * type=0上班，1下班
                 *  status=1取消，2申请（下班），4确认（下班，上班），8驳回（下班）
                 */
                if (cloclModel.isSuccess()) {
                    if (cloclModel.getContent() != null) {
                        int type = cloclModel.getContent().getType();
                        int status = cloclModel.getContent().getStatus();
                        int clockAccountId = cloclModel.getContent().getAccountId();
                        name = cloclModel.getContent().getDriverName();
                        mile = cloclModel.getContent().getCarMile();
                        driverClockId = cloclModel.getContent().getId();
                        switch (status) {
                            case 1: //取消下班打卡，自己可以继续打卡，别人无法上班打卡
                                if (User.getInstance().getAccountId() == clockAccountId) {
                                    binding.clock.setText("下车打卡");
                                    binding.clock.setBackgroundColor(getResources().getColor(R.color.transparent));
                                } else {
                                    isshowtoast = true;
//                                    binding.clock.setBackgroundColor(getResources().getColor(R.color.dark));
//                                    binding.clock.setText("其他人正在使用，无法打卡");
                                }
                                break;
                            case 2: //申请下班，自己可以取消，
                                if (User.getInstance().getAccountId() == clockAccountId) {
                                    binding.clock.setText("取消本次打卡");
                                    binding.clock.setBackgroundColor(getResources().getColor(R.color.transparent));
                                } else {
                                    if (type == 1) {
                                        new AlertDialog.Builder(PunchActivity.this).setMessage(name + "的换车里程为" + mile + "km,请确认是否正确")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        punch(4, mile, driverClockId);
                                                    }
                                                }).setNegativeButton("驳回", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                punch(8, mile, driverClockId);
                                            }
                                        }).setCancelable(false).create().show();
                                    }
                                    isshowtoast = true;
//                                    binding.clock.setBackgroundColor(getResources().getColor(R.color.dark));
//                                    binding.clock.setText("其他人正在使用，无法打卡");
                                }
                                break;
                            /**
                             * type=0上班，1下班
                             *  status=1取消，2申请（下班），4确认（下班，上班），8驳回（下班）
                             */
                            case 4:
                                if (User.getInstance().getAccountId() == clockAccountId) {
                                    if (type == 0) { //上班状态
                                        binding.clock.setText("下车打卡");
                                        binding.clock.setBackgroundColor(getResources().getColor(R.color.transparent));
                                    } else {
                                        binding.clock.setText("上车打卡");
                                        binding.clock.setBackgroundColor(getResources().getColor(R.color.transparent));
                                    }
                                } else {
                                    isshowtoast = true;
                                    binding.clock.setText("上车打卡");
                                    binding.clock.setBackgroundColor(getResources().getColor(R.color.transparent));
                                }
                                break;
                            case 8:
                                if (User.getInstance().getAccountId() == clockAccountId) {
                                    binding.clock.setText("下车打卡");
                                    binding.clock.setBackgroundColor(getResources().getColor(R.color.transparent));
                                } else {
                                    binding.clock.setText("上车打卡");
                                    binding.clock.setBackgroundColor(getResources().getColor(R.color.transparent));
                                    isshowtoast = true;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    Utils.toast(PunchActivity.this, cloclModel.getMsg());
                }
                pageNo = 1;
                loadData(true);
                Utils.log(url, requestParams, json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.log(url, requestParams, null);
            }
        });
    }

    private void loadData(final boolean isrefresh) {
        final String url = API.BASE_URL + API.PUNCH_LIST;
        final RequestParams requestParams = new RequestParams();
        //pageNo pageSize  accountId
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        requestParams.add("pageNo", String.valueOf(pageNo));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                finishLoad();
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                    if (jsonObject.getString("content").isEmpty()) {
                        String msg = jsonObject.getString("msg");
                        if (!msg.isEmpty()) {
                            Utils.toast(PunchActivity.this, msg);
                        }
                    } else {
                        PunchModel model = GsonUtils.getInstance().fromJson(json, PunchModel.class);
                        if (isrefresh) {
                            list.clear();
                        }
                        if (model != null && model.getContent() != null && model.getContent().getData() != null) {
                            list.addAll(model.getContent().getData());
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                finishLoad();
                Utils.log(url, requestParams, null);
            }
        });
    }

    //smartRefresh结束刷新
    private void finishLoad() {
        binding.smartrefreshLayout.finishRefresh();
        binding.smartrefreshLayout.finishLoadMore();
    }

    private long time;
    private AlertDialog alertDialog;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.clock: //	-1获取司机上次打卡状态，0上班，1下班
                if (System.currentTimeMillis() - time < 5000) {
                    Utils.toast(PunchActivity.this, "点击过快");
                    return;
                }
                if (isshowtoast){
                    new AlertDialog.Builder(PunchActivity.this).setMessage("换车司机正在上车，无法上车打卡")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                }else {
                    switch (binding.clock.getText().toString()) {
                        case "上车打卡":
                            punch(4, mile, driverClockId);
                            break;
                        case "下车打卡":
                            xiaban();
                            break;
                        case "取消本次打卡":
                            punch(1, mile, driverClockId);
                            break;
                        default:
                            break;
                    }
                }
                time = System.currentTimeMillis();
                break;
            default:
                break;
        }
    }

    private void xiaban() {
        View view1 = LayoutInflater.from(PunchActivity.this).inflate(R.layout.punch1, null);
        final EditText e1 = view1.findViewById(R.id.et);
        TextView imageView = view1.findViewById(R.id.textView);
        final AlertDialog.Builder builder = new AlertDialog.Builder(PunchActivity.this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e1.getText().toString().isEmpty()) {
                    Utils.toast(PunchActivity.this, "请输入换车总里程");
                } else {
                    alertDialog.dismiss();
                    punch(2, Integer.parseInt(e1.getText().toString()), driverClockId);
                }
            }
        });
        alertDialog = builder.setView(view1).create();
        alertDialog.show();
//        final String mileUrl = API.BASE_URL + API.VINGETMILE;
//        final RequestParams requestParams = new RequestParams();
//        requestParams.add("vin", vin);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        PersistentCookieStore cookieStore = new PersistentCookieStore(PunchActivity.this);
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(mileUrl, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(mileUrl, requestParams, json);
//                BaseObjectString baseObject = GsonUtils.getInstance().fromJson(json, BaseObjectString.class);
//                if (baseObject.isSuccess()) {
//                    showdialog(baseObject.getContent());
//                } else {
//                    showdialog("");
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                String json = new String(responseBody);
//                Utils.log(mileUrl, requestParams, json);
//                showdialog("");
//            }
//        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PunchActivity.this).inflate(R.layout.adapter_punch, null);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PunchModel.ContentBean.DataBean dataBean = list.get(position);
            holder.name.setText(dataBean.getDriverName() + "");
            holder.time.setText(DateUtils.stampToDate(dataBean.getCreateTime()) + "");
            holder.punch.setText(dataBean.getType() == 0 ? "上车" : "下车");
            holder.licheng.setText(dataBean.getCarMile()+"km");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time, punch, licheng;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            punch = itemView.findViewById(R.id.punch);
            licheng = itemView.findViewById(R.id.licheng);
        }
    }
}
