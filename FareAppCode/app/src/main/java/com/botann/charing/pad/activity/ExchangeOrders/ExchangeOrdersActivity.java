package com.botann.charing.pad.activity.ExchangeOrders;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.botann.charing.pad.activity.ExchangeBattery.AccountByPlateModelContent;
import com.botann.charing.pad.activity.ExchangeBattery.ClickAccount;
import com.botann.charing.pad.activity.caridident.CarIdIdentActivity;
import com.botann.charing.pad.activity.malfunctionrecord.RecordAdapter;
import com.botann.charing.pad.base.Pager;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.callbacks.SGOnAlertClick;
import com.botann.charging.pad.R;
import com.botann.charing.pad.activity.PagerActivity;

import com.botann.charing.pad.callbacks.SGClickCellInsideCallBack;
import com.botann.charing.pad.callbacks.SGFinishCallBack;
import com.botann.charing.pad.model.Car;
import com.botann.charing.pad.model.ExchangeOrder;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.activity.ExchangeBattery.ExchangeBatteryActivity;
import com.botann.charing.pad.activity.Main.MainActivity;
import com.botann.charing.pad.components.zxing.QRScanActivity;
import com.botann.charing.pad.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ????????????
 * Created by liushanguo on 2017/8/18.
 * ???????????????????????????????????????????????????????????????
 */

public class ExchangeOrdersActivity extends PagerActivity implements View.OnClickListener,ClickAccount{

    private AlertDialog dialog;
    private LinearLayout li_carplateScan;
    //add scan carid, then QR code if not exist;
    private final int REQUESTCODE_CARID = 1001;
    private final int REQUESTCODE_PLATENUM = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("????????????");
        cellAdapter = new ExchangeOrderAdapter(listData);
        mRecyclerView.setAdapter(cellAdapter);
        ((ExchangeOrderAdapter) cellAdapter).callBack = new SGClickCellInsideCallBack() {
            @Override
            public void click(Object object) {
                final ExchangeOrder order = (ExchangeOrder) object;
                alert("?????????????????????", new SGOnAlertClick() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        dialog.dismiss();
                        if (index > 0) cancelLine(order.getId(), "?????????????????????", null, null);
                    }
                });
            }

            //change power need personal data
            @Override
            public void changePower(Object object) {
                ExchangeOrder order = (ExchangeOrder) object;
                Intent toMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent toChangeBatteryActivity = new Intent(getApplicationContext(), ExchangeBatteryActivity.class);
                toChangeBatteryActivity.putExtra("clear", true);
                toChangeBatteryActivity.putExtra("platenumber", order.getPlateNumber());
                toChangeBatteryActivity.putExtra("account", order.getAccount());
                TaskStackBuilder.create(mContext)
                        .addNextIntent(toMainActivity)
                        // use this method if you want "intentOnTop" to have it's parent chain of activities added to the stack. Otherwise, more "addNextIntent" calls will do.
                        .addNextIntent(toChangeBatteryActivity)
                        .startActivities();
            }
        };
        mRecyclerView.refresh();
    }


    @Override
    public int viewLayout() {
        return R.layout.activity_exchange_orders;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        TextView textView = findTextViewById(R.id.tv_station_name);
        textView.setText(User.shared().getStation());
        li_carplateScan = (LinearLayout) findViewById(R.id.li_scan_carplate);
        li_carplateScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_permissions();
                if (permissionList != null && permissionList.size() == 0) {
                    activitytoCaridentPlate();
                }
            }
        });
    }

    public void scan(View v) {
        request_permissions();
        if (permissionList != null && permissionList.size() == 0) {
            activitytoQRS();
        }
    }

    /**
     * ???????????????????????????
     */
    private void activitytoQRS() {
        Intent toScan = new Intent(getApplicationContext(), QRScanActivity.class);
        startActivityForResult(toScan, 1);
    }

    /**
     * ???????????????????????????????????????
     */
    private void activitytoCarident() {
        Intent toScanCarid = new Intent(ExchangeOrdersActivity.this, CarIdIdentActivity.class);
        startActivityForResult(toScanCarid, REQUESTCODE_CARID);
    }

    /**
     * ?????????????????????
     */
    private void activitytoCaridentPlate() {
        Intent toScanCarid = new Intent(ExchangeOrdersActivity.this, CarIdIdentActivity.class);
        startActivityForResult(toScanCarid, REQUESTCODE_PLATENUM);
    }

    private String carid = "";
    private String account = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * ?????????????????????????????????????????????????????????
         */
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String account = data.getStringExtra(QRScanActivity.ALL);
            this.account = account;
            queueCarid(null, account);
        } else if (requestCode == REQUESTCODE_PLATENUM && resultCode == 1002) {
            String carPlate = data.getStringExtra("carid");
            getAccountByPlate(carPlate);
        } else if (requestCode == REQUESTCODE_CARID && resultCode == 1002) {
            String carId = data.getStringExtra("carid");
            carid = carId;
            queueCarid(carId, account);
        }
    }
    @Override
    public void clickAccount(String account, String carPlate) {
//        queueCarPlate(carPlate);
        queueCarid(carPlate, account);
    }
    private void getAccountByPlate(final String carPlate) {//plateNumber
        URLParams urlParams = new URLParams();
        urlParams.put("plateNumber", carPlate);
        SGHTTPManager.POST(API.GET_ACCOUNT_BYPLATE, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess) {
                    if (fetchModel.getJsonArray().length() > 0) {
                        JSONArray jsonArray = fetchModel.getJsonArray();
                        List<AccountByPlateModelContent> list = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                AccountByPlateModelContent content = new AccountByPlateModelContent();
                                content.setName(jsonObject.getString("name"));
                                content.setPhone(jsonObject.getString("phone"));
                                content.setAccount(jsonObject.getString("account"));
                                list.add(content);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        showMyDialog(list,carPlate);
                    } else {
                        showToast("??????????????????????????????");
                    }
                } else {
                    showToast(userInfo);
                }
            }
        });
    }
    //??????????????????
    private AlertDialog alertDialog;
    private void showMyDialog(List<AccountByPlateModelContent> content, String carPlate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_exchange, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(content, this, carPlate));
        alertDialog = builder.setView(view).create();
        alertDialog.show();
    }
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<AccountByPlateModelContent> content;
        private ClickAccount clickAccount;
        private String carPlate;

        public MyAdapter(List<AccountByPlateModelContent> content, ClickAccount clickAccount, String carPlate) {
            this.content = content;
            this.clickAccount = clickAccount;
            this.carPlate = carPlate;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ExchangeOrdersActivity.this).inflate(R.layout.adapter_account_byplate, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final AccountByPlateModelContent data = content.get(position);
            holder.name.setText(data.getName() + "");
            holder.phone.setText(data.getPhone() + "");
            holder.re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    clickAccount.clickAccount(data.getAccount(), carPlate);
                }
            });
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView name, phone;
            private RelativeLayout re;

            public MyViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                phone = (TextView) itemView.findViewById(R.id.phone);
                re = (RelativeLayout) itemView.findViewById(R.id.re);
            }
        }
    }

    /*
     * ??????????????????
     *
     * */
    private void cancelCaridLine(final String lineId, String comment, final String account, final String carPlate) {
        URLParams params = new URLParams();
        params.put("lineId", lineId);
        params.put("comment", comment);
        SGHTTPManager.POST(API.URL_CANCEL_EXCHANGE_LINE, params, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess) {
                    if (lineId != null) {
                        if (account != null) {
                            queueCarid(carid, account);
                        } else {
                            queueCarPlate(carPlate);
                        }
                    } else {
                        mRecyclerView.refresh();
                        toast(userInfo);
                    }
                } else {
                    toast(userInfo);
                }
            }
        });
    }


    /**
     * ????????????
     *
     * @param lineId  ?????????lineId
     * @param comment ????????????
     * @param account if not null ????????????????????????????????????
     */
    private void cancelLine(final String lineId, String comment, final String account, final Integer carId) {
        if (account == null) {
            showProgressHud("??????????????????...");
        } else {
            showProgressHud("???????????????????????????...");
        }
        URLParams params = new URLParams();
        params.put("lineId", lineId);
        params.put("comment", comment);
        SGHTTPManager.POST(API.URL_CANCEL_EXCHANGE_LINE, params, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                hideProgressHud();
                if (isSuccess) {
                    if (account != null) {
                        addToLine(account, carId);
                    } else {
                        mRecyclerView.refresh();
                        toast(userInfo);
                    }
                } else {
                    toast(userInfo);
                }

            }
        });
    }

    //?????????????????????
    private void queueCarid(String carid, final String account) {
        URLParams urlParams = new URLParams();
        if (carid != null) {
            urlParams.put("plateNumber", carid);
        }
        urlParams.put("siteId", User.shared().getStationId());
        urlParams.put("account", account);
        SGHTTPManager.POST(API.URL_CARID_QUEUE, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, final SGFetchModel fetchModel) {
                if (isSuccess) {
                    toast(userInfo);
                    mRecyclerView.refresh();
                } else {
                    if (fetchModel.code == 801) { // ???????????????
                        alert("???????????????????", new SGOnAlertClick() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                dialog.dismiss();
                                if (index > 0) {
                                    cancelCaridLine(fetchModel.content, "????????????????????????", account, null);
                                }
                            }
                        });
                    } else if (fetchModel.code == 2002) {
                        /**
                         * ????????????????????????
                         */
                        List<String> carplateLists = SGFetchModel.getGson().fromJson(fetchModel.content, new TypeToken<List<String>>() {
                        }.getType());
                        showCarPlateDialog(carplateLists);
                    }
                    showToast(userInfo);
                }
            }
        });
    }

    /**
     * ??????????????????
     */
    private void queueCarPlate(final String carPlate) {
        URLParams urlParams = new URLParams();
        urlParams.put("plateNumber", carPlate);
        urlParams.put("siteId", User.shared().getStationId());
        SGHTTPManager.POST(API.URL_CARPLATE_QUEUE, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, final SGFetchModel fetchModel) {
                if (isSuccess) {
                    toast(userInfo);
                    mRecyclerView.refresh();
                } else {
                    if (fetchModel.code == 801) { // ???????????????
                        alert("???????????????????", new SGOnAlertClick() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                dialog.dismiss();
                                if (index > 0) {
                                    cancelCaridLine(fetchModel.content, "????????????????????????", null, carPlate);
                                }
                            }
                        });
                    }
                    showToast(userInfo);
                }
            }
        });
    }

    /**
     * ??????????????????dialog????????????
     */
    private void showCarPlateDialog(final List<String> carplateLists) {
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.active_dialog, null);
        LinearLayout layout = (LinearLayout) view
                .findViewById(R.id.id_active_dialog);
        layout.setPadding(10, 10, 10, 10);
        for (int i = -1; i < carplateLists.size(); i++) {
            Button btn = new Button(mContext);
            if (i == -1) {
                btn.setText("????????????");
            } else {
                btn.setText(carplateLists.get(i));
            }
            btn.setTag(i);
            btn.setTextColor(Color.WHITE);
            layout.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (Integer) v.getTag();
                    if (index == -1) {
                        activitytoCarident();
                    } else {
                        queueCarid(carplateLists.get(index), account);
                    }
                    dialog.dismiss();
                }
            });
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setView(view);
        dialogBuilder.setTitle("?????????????????????????????????" + account);
        dialogBuilder.setNegativeButton("??????",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog = dialogBuilder.show();
    }

    /**
     * toast??????
     */
    private void showToast(String message) {
        Toast toast = Toast.makeText(ExchangeOrdersActivity.this, message, Toast.LENGTH_LONG);
        ToastUtil.showMyToast(toast, 5000);
    }

    private void addToLine(final String account, final Integer carId) {
        showProgressHud("????????????????????????...");
        URLParams params = new URLParams();
        params.put("siteId", User.shared().getStationId());
        params.put("account", account);
        params.put("carId", carId);
        SGHTTPManager.POST(API.URL_ADD_EXCHANGE_LINE, params, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, final SGFetchModel fetchModel) {
                hideProgressHud();
                if (isSuccess) {
                    toast(userInfo);
                    mRecyclerView.refresh();
                } else {
                    if (fetchModel.code == 801) { // ???????????????
                        alert("???????????????????", new SGOnAlertClick() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                dialog.dismiss();
                                if (index > 0) {
                                    cancelLine(fetchModel.content, "????????????????????????", account, carId);
                                }
                            }
                        });
                    } else {
                        toast(userInfo);
                    }
                }
            }
        });
    }


    /**
     * ??????????????????
     *
     * @param isRefresh ?????????????????????
     * @param callBack  ??????????????????????????????
     */
    @Override
    public void loadDatas(final Boolean isRefresh, final SGFinishCallBack callBack) {
        int nowPage = currentPage;
        if (isRefresh) nowPage = 1;
        URLParams params = new URLParams();
        params.put("siteId", User.shared().getStationId());
        params.put("pageNo", nowPage);
        params.put("pageSize", pageSize);
        SGHTTPManager.POST(API.URL_EXCHANGE_LINE, params, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess) {
                    if (isRefresh) {
                        listData.clear();
                    }
                    Pager pager = fetchModel.pagerOfContent(ExchangeOrder.class);
                    listData.addAll(pager.list);
                    callBack.onFinish(pager.list.size() < pageSize);
                } else {
                    toast(userInfo);
                    callBack.onFinish(true);
                }
            }
        });
    }

    // ????????????????????????????????????????????????????????????????????????????????????
    List<String> permissionList = new ArrayList<>();

    // ??????????????????
    private void request_permissions() {

        // ????????????????????????????????????????????????????????????????????????
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]), 1002);
        }
    }

    // ????????????????????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1002:
                permissionList.clear();
                // 1002???????????????????????????????????????
                if (grantResults.length > 0) {
                    // ?????????????????????????????????????????????????????????????????????????????????
                    for (int i = 0; i < grantResults.length; i++) {
                        // PERMISSION_DENIED ????????????????????????????????????????????????????????????????????????????????????
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            Toast.makeText(ExchangeOrdersActivity.this, permissions[i] + "??????????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }
}
