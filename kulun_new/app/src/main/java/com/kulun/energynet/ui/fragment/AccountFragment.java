package com.kulun.energynet.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kulun.energynet.MainApp;
import com.kulun.energynet.R;
import com.kulun.energynet.adapter.CarIdentAdapter;
import com.kulun.energynet.adapter.adapterinter.CarIdentInterface;
import com.kulun.energynet.db.SharedPreferencesHelper;
import com.kulun.energynet.model.BindCar;
import com.kulun.energynet.model.CloclModel;
import com.kulun.energynet.model.CouponReceiveModel;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.chongdian.ChongdianZhongModel;
import com.kulun.energynet.model.chongdian.ChongdianZhuangInforContent;
import com.kulun.energynet.model.chongdian.ChongdianzhuangInfoModer;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.activity.CouponActivity;
import com.kulun.energynet.ui.activity.MessageActivity;
import com.kulun.energynet.ui.activity.PunchActivity;
import com.kulun.energynet.ui.activity.chongdian.ChongdianActivity;
import com.kulun.energynet.ui.activity.chongdian.ChongdianZhongActivity;
import com.kulun.energynet.ui.activity.login.DriverCarInfoModel;
import com.kulun.energynet.ui.activity.login.LoginActivity;
import com.kulun.energynet.ui.activity.login.SetPasswordActivity;
import com.kulun.energynet.ui.activity.login.UploadDriverCarInfoActivity;
import com.kulun.energynet.ui.activity.more.MoreActivity;
import com.kulun.energynet.ui.activity.PayActivity;
import com.kulun.energynet.ui.activity.promotions.PromotionsActivity;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.QRCodeUtil;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Orion on 2017/7/13.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {
    private String account;
    //    private String name;
//    private Float balance;
//    private Integer couponNum;
//    private Integer messageNum;
//    private int appCanRecharge;
//    private ArrayList<BindCar> bindCarList = new ArrayList<>();
    private TextView mUserName;
    private TextView mUserAccount;
    private TextView mLeftMoney;
    private TextView mCouponNum;
    private TextView mMessageNum;
    private TextView mCouponLargeNum;
    private TextView mMessageLargeNum;
    //    private TextView mRecharge;
    private ImageView mQRCodeView, image_coupon;
    private RefreshLayout accountRefresh;
    private RelativeLayout re_coupon, re_message, re_chongdian;
    private RelativeLayout fm_recharge, re_lock;
    private RecyclerView recyclerView;
    private TextView tv_queueNum, tv_carident;
    private int bindId;
    private TextView tv_search;
    private EditText et_plateSearch;
    private List<BindCar> searchList;
    private TextView tv_more, chongdian_money;
    private final DecimalFormat df = new DecimalFormat("######0.00");
    private String sep = "", connector = "";
    private boolean isHaveOrder = false;
    private double money_chongdian;
    private boolean isChongdian = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    String account = msg.getData().getString("account");
                    int bindId = msg.getData().getInt("bindId");
                    requestQueueNum(account, bindId);
                    refreshQueueNum(account, bindId);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 车辆搜索
     * 根据输入字符串长度，截取list中车牌，进行比对
     */
    private void plateSearch(String s) {
        if (s == null || s.equals("")) {
            Toast.makeText(MainApp.getInstance().getApplicationContext(), "请输入要搜索的车牌", Toast.LENGTH_LONG).show();
            return;
        }
        List<BindCar> list = new ArrayList<>();
        for (int i = 0; i < searchList.size(); i++) {
            if (searchList.get(i).getPlate_number().toUpperCase().indexOf(s.toUpperCase()) != -1) {
                list.add(searchList.get(i));
            }
        }
        recyclerView.setAdapter(new CarIdentAdapter(MainApp.getInstance().getApplicationContext(), list, new CarIdentInterface() {
            @Override
            public void itemClick(int id, String plateNum, String vin) {
                bindId = id;
                createARCode(account, id);
                showSelectCarId(plateNum, id, vin);
            }
        }));
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);
        mUserName = (TextView) view.findViewById(R.id.username);
        mUserAccount = (TextView) view.findViewById(R.id.user_account);
        mLeftMoney = (TextView) view.findViewById(R.id.left_money);
        mCouponNum = (TextView) view.findViewById(R.id.couponNum);
        mMessageNum = (TextView) view.findViewById(R.id.messageNum);
        mCouponLargeNum = (TextView) view.findViewById(R.id.coupon_largeNum);
        mMessageLargeNum = (TextView) view.findViewById(R.id.message_largeNum);
//        mRecharge = (TextView) view.findViewById(R.id.to_recharge_activity);
        mQRCodeView = (ImageView) view.findViewById(R.id.iv);
        accountRefresh = (RefreshLayout) view.findViewById(R.id.account_refreshLayout);
        tv_search = view.findViewById(R.id.tv_search);
        et_plateSearch = view.findViewById(R.id.et_platenumsearch);
        tv_search.setOnClickListener(this);
        fm_recharge = view.findViewById(R.id.fm_recharge);
        fm_recharge.setOnClickListener(this);
        tv_queueNum = view.findViewById(R.id.tv_queueNum);
        recyclerView = view.findViewById(R.id.recyclerView);
        tv_carident = view.findViewById(R.id.text_carident);
        re_coupon = view.findViewById(R.id.re_coupon);
        re_message = view.findViewById(R.id.re_message);
        re_chongdian = view.findViewById(R.id.re_chongdian);
        chongdian_money = view.findViewById(R.id.chongdian_money);
//        tv_mile = view.findViewById(R.id.tv_mile);
        re_chongdian.setOnClickListener(this);
        re_coupon.setOnClickListener(this);
        re_message.setOnClickListener(this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainApp.getInstance().getApplicationContext()));
        requestQueueNum(account, bindId);
        tv_more = view.findViewById(R.id.more);
        tv_more.setOnClickListener(this);
        handler.sendMessageDelayed(getMessage(account, bindId), 30000);
        mUserAccount.setText(account);
//        mRecharge.setOnClickListener(this);
        image_coupon = view.findViewById(R.id.img_coupon);
        image_coupon.setOnClickListener(this);
        re_lock = view.findViewById(R.id.re_lock);
        re_lock.setOnClickListener(this);
        refreshUI();
        getdrivercarinfo();
        accountRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshUI();
                getdrivercarinfo();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadclock();
//        refreshUI();
    }

    private void getdrivercarinfo() {
        final RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        final String url = API.BASE_URL + API.DRIVERCARINFO;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(getContext());
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                try {
                    JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                    String data = obj.get("msg").getAsString();
                    if (data != null || !data.equals("")) {
                        new AlertDialog.Builder(getContext()).setMessage(data + "")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
//                                    Intent intents = new Intent(getContext(), UploadDriverCarInfoActivity.class);
//                                    intents.putExtra(API.islogin, false);
//                                    startActivity(intents);
                                    }
                                }).create().show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
            }
        });
    }

    private void loadclock() {
        /**
         * accountId     [string]	是	司机id
         * status        [int]	是	0获取车辆最新打卡记录，status=1取消，2申请（下班），4确认（下班，上班），8驳回（下班）
         * mile           [int]		车辆里程，status=2时必填
         * driverClockId [string]		打卡记录id，status=1,4,8时必填
         */
        final String url = API.BASE_URL + API.PUNCH;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        requestParams.add("status", "0");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(getContext());
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                CloclModel cloclModel = GsonUtils.getInstance().fromJson(json, CloclModel.class);
                /**
                 * type=0上班，1下班
                 *  status=1取消，2申请（下班），4确认（下班，上班），8驳回（下班）
                 */
                if (cloclModel.isSuccess()) {
                    if (cloclModel.getContent() != null) {
                        if (User.getInstance().getAccountId() == cloclModel.getContent().getAccountId()) {
                            if (cloclModel.getContent().getType() == 1 && cloclModel.getContent().getStatus() == 4) {
                                SharedPreferencesHelper.getInstance(getContext()).putInt(API.isshangban, 1);
                            } else {
                                SharedPreferencesHelper.getInstance(getContext()).putInt(API.isshangban, 0);
                            }
                        }
                        if (User.getInstance().getAccountId() == cloclModel.getContent().getAccountId() && cloclModel.getContent().getStatus() == 8) {
                            new AlertDialog.Builder(getContext()).setMessage("您的下车打卡被驳回，请重新进行下车打卡")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            Intent intentlock = new Intent(getActivity(), PunchActivity.class);
                                            startActivity(intentlock);
                                        }
                                    }).create().show();
                        }
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.log(url, requestParams, null);
            }
        });
    }

    private void requestCoupon() {
        final String url = API.BASE_URL + API.COUPON_AVA;
        final RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        params.add("pageNo", "1");
        params.add("pageSize", "20");
        final AsyncHttpClient client = new AsyncHttpClient();
        //保存cookie，自动保存到了sharepreferences
        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
        client.setCookieStore(myCookieStore);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                Utils.log(url, params, json);
                CouponReceiveModel couponUserModel = GsonUtils.getInstance().fromJson(json, CouponReceiveModel.class);
                if (couponUserModel.isSuccess()) {
                    if (couponUserModel.getContent() != null && couponUserModel.getContent().getData() != null && couponUserModel.getContent().getData().size() > 0) {
                        image_coupon.setVisibility(View.VISIBLE);
                    } else {
                        image_coupon.setVisibility(View.GONE);
                    }
                } else {
                    Utils.toast(getContext(), couponUserModel.getMsg() + "");
                }
//                Utils.log(url,params,json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Utils.log(url, params, "");
            }
        });
    }

    private void requestQueueNum(String account, int bindId) {
        String url = API.BASE_URL + API.URL_QUEUENUM;
        RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        if (bindId != 0) {
            params.add("bindId", bindId + "");
        }
        Log.d(Utils.TAG, "请求参数:" + params.toString());
        Log.d(Utils.TAG, "请求url:" + url);
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
        client.setCookieStore(myCookieStore);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Log.d(Utils.TAG, "json返回数据" + json);
                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                int num = obj.get("content").getAsInt();
                showQueueNum(num);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(Utils.TAG, "Exception = " + error.toString());
                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        SharedPreferences data = MainApp.getInstance().getApplicationContext().getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        switch (view.getId()) {
            case R.id.re_coupon:
                Intent intent = new Intent(getActivity(), CouponActivity.class);
                intent.putExtra(API.coupon, true);
                intent.putExtra(API.chongdian, isChongdian);
                startActivity(intent);
                break;
            case R.id.re_message:
                final String Url = API.BASE_URL + API.URL_SYSTEM_MESSAGE;
                final RequestParams Params = new RequestParams();
                Params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
                Params.add("pageNo", "1");
                Params.add("pageSize", "20");
                final AsyncHttpClient Client = new AsyncHttpClient();
                //保存cookie，自动保存到了sharepreferences
                PersistentCookieStore MyCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                Client.setCookieStore(MyCookieStore);
                Client.post(Url, Params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        String json = new String(response);
                        Utils.log(Url, Params, json);
                        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                        JsonObject content = obj.get("content").getAsJsonObject();
                        JsonArray data = content.get("data").getAsJsonArray();
                        Integer total = content.get("total").getAsInt();
                        String res = data.toString();
                        SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("message", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("systemMessageList", res);
                        editor.putInt("system_total", total);
                        editor.commit();
                        Constants.messageRes = res;
                        Constants.messageTotal = total;
                        final String Url = API.BASE_URL + API.URL_POST_MESSAGE;
                        final RequestParams Params = new RequestParams();
                        Params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
                        Params.add("pageNo", "1");
                        Params.add("pageSize", "20");
                        final AsyncHttpClient Client = new AsyncHttpClient();
                        PersistentCookieStore MyCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                        Client.setCookieStore(MyCookieStore);
                        Client.post(Url, Params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                String json = new String(response);
                                Utils.log(Url, Params, json);
                                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                JsonObject content = obj.get("content").getAsJsonObject();
                                JsonArray data = content.get("data").getAsJsonArray();
                                Integer total = content.get("total").getAsInt();
                                String res = data.toString();
                                SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("message", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("postMessageList", res);
                                editor.putInt("post_total", total);
                                editor.commit();
//                                Constants.postMessageRes = res;
//                                Constants.postMessageTotal = total;
                                Intent message = new Intent(MainApp.getInstance().getApplicationContext(), MessageActivity.class);
                                message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MainApp.getInstance().getApplicationContext().startActivity(message);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                                Log.e(Utils.TAG, "Exception = " + e.toString());
                                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                        Log.e(Utils.TAG, "Exception = " + e.toString());
                        Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMessageNum.setText("");
                        mMessageNum.setVisibility(View.INVISIBLE);
                        mMessageLargeNum.setVisibility(View.INVISIBLE);
                    }
                }, 2000);
                editor.putInt("messageNum", 0);
                editor.commit();
                break;
            case R.id.fm_recharge:
                Intent message = new Intent(MainApp.getInstance().getApplicationContext(), PayActivity.class);
                message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainApp.getInstance().getApplicationContext().startActivity(message);
                break;
            case R.id.tv_search:
                plateSearch(et_plateSearch.getText().toString());
                break;
            case R.id.more:
                Intent moreintent = new Intent(getActivity(), MoreActivity.class);
                startActivity(moreintent);
                break;
            case R.id.re_chongdian:
//                if (money_chongdian >= 10) {
                if (connector.isEmpty() && sep.isEmpty()) {
                    Intent chongdianIntent = new Intent(getActivity(), ChongdianActivity.class);
                    startActivity(chongdianIntent);
                } else {
                    loadConnector(connector, sep);
                }
                break;
            case R.id.img_coupon:
                Intent intents = new Intent(getActivity(), CouponActivity.class);
                intents.putExtra(API.coupon, false);
                intents.putExtra(API.chongdian, isChongdian);
                startActivity(intents);
                break;
            case R.id.re_lock:
                Intent intentlock = new Intent(getActivity(), PunchActivity.class);
                startActivity(intentlock);
                break;
//            case R.id.left_money:
//                Intent message2 = new Intent(MainApp.getInstance().getApplicationContext(), PayResultActivity.class);
//                message2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                MainApp.getInstance().getApplicationContext().startActivity(message2);
//                break;
            default:
                break;
        }
    }

    private void createARCode(String account, int id) {
        Bitmap bitmap = QRCodeUtil.createQRCode(account + "&" + id, 500, 500);
        mQRCodeView.setImageBitmap(bitmap);
    }

    private void showQueueNum(int queueNum) {
        if (queueNum > 0) {
            tv_queueNum.setText("排队人数" + "\n" + queueNum);
            tv_queueNum.setVisibility(View.VISIBLE);
        } else {
            tv_queueNum.setVisibility(View.INVISIBLE);
        }
    }

    private void showRechargeButton(int appCanRecharge) {
        if (appCanRecharge == 1) {
            fm_recharge.setVisibility(View.VISIBLE);
        } else {
            fm_recharge.setVisibility(View.GONE);
        }
    }

    private Message getMessage(String account, int bindId) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("account", account);
        bundle.putInt("bindId", bindId);
        message.setData(bundle);
        return message;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void refreshQueueNum(String account, int bindId) {
        handler.removeCallbacksAndMessages(null);
        handler.sendMessageDelayed(getMessage(account, bindId), 30000);
    }

    private void QRCodeInit(ArrayList<BindCar> list, String account) {
        if (list.size() == 1) {
            tv_carident.setVisibility(View.VISIBLE);
            tv_carident.setText(carstring + list.get(0).getPlate_number());
            bindId = list.get(0).getId();
            Bitmap bitmap = QRCodeUtil.createQRCode(account + "&" + bindId, 500, 500);
            mQRCodeView.setImageBitmap(bitmap);
        } else {
            SharedPreferences sharedPreferences = MainApp.getInstance().getSharedPreferences("carplate", Context.MODE_PRIVATE);
            String carplateSave = sharedPreferences.getString("carplate" + User.getInstance().getAccountId(), "");
            int id = sharedPreferences.getInt("id" + User.getInstance().getAccountId(), 0);
            if (!carplateSave.equals("") && id != 0 && list.size() > 0) {
                tv_carident.setVisibility(View.VISIBLE);
                tv_carident.setText(carstring + carplateSave);
                bindId = id;
            } else {
                bindId = 0;
            }
            Bitmap bitmap = QRCodeUtil.createQRCode(account + "&" + bindId, 500, 500);
            mQRCodeView.setImageBitmap(bitmap);
        }
    }

    private final String carstring = "车牌号:";

    private void showSelectCarId(String plateNum, int id, String vin) {
        tv_carident.setVisibility(View.VISIBLE);
        tv_carident.setText(carstring + plateNum);
        SharedPreferences sharedPreferences = MainApp.getInstance().getSharedPreferences("carplate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("carplate" + User.getInstance().getAccountId(), plateNum);
        editor.putInt("id" + User.getInstance().getAccountId(), id);
        editor.putString("vin" + User.getInstance().getAccountId(), vin);
        editor.commit();
    }

    private void refreshUI() {
        final String url = API.BASE_URL + API.URL_INFO;
        final RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        final AsyncHttpClient client = new AsyncHttpClient();
        //保存cookie，自动保存到了sharepreferences
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getContext());
        client.setCookieStore(myCookieStore);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                Utils.log(url, params, json);
                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                JsonObject data = obj.get("content").getAsJsonObject();
                mUserName.setText(data.get("name").getAsString().isEmpty() ? "我" : data.get("name").getAsString());
                mUserAccount.setText(data.get("account").getAsString());
                //`account_type` int(2) NOT NULL DEFAULT '1' COMMENT '0：换电账户；1 充电账户；2 即是换电又是充电； 3 两者都不是',
                if (data.has("accountType")) {
                    SharedPreferencesHelper.getInstance(getContext()).putInt(API.accountType, data.get("accountType").getAsInt());
                }
                if (data.has("exchangeAccountAppRefund")) {
                    SharedPreferencesHelper.getInstance(getContext()).putBoolean(API.exchangeAccountAppRefund, data.get("exchangeAccountAppRefund").getAsBoolean());
                }
                if (data.has("bucketName")){
                    SharedPreferencesHelper.getInstance(getContext()).putString(API.bucketName, data.get("bucketName").getAsString());
                }
//                double duigongMoney = data.get("companyBalance").getAsDouble();
//                if (duigongMoney==0){
//                    mLeftMoney.setText("换电账户余额：" + df.format(data.get("balance").getAsFloat()) + "元");
//                }else {
//                    mLeftMoney.setText("换电账户余额：" + df.format(duigongMoney) + "元");
//                }
                Integer couponNum = data.get("couponNum").getAsInt();
                Integer messageNum = data.get("messageNum").getAsInt();
                if (data.get("showClock").getAsBoolean()) {
                    re_lock.setVisibility(View.VISIBLE);
                } else {
                    re_lock.setVisibility(View.GONE);
                }
                if (couponNum == 0) {
                    mCouponNum.setText("");
                    mCouponNum.setVisibility(View.INVISIBLE);
                    mCouponLargeNum.setVisibility(View.INVISIBLE);
                } else if (couponNum > 9) {
                    mCouponLargeNum.setText(couponNum.toString());
                    mCouponLargeNum.setVisibility(View.VISIBLE);
                    mCouponNum.setVisibility(View.INVISIBLE);
                } else {
                    mCouponNum.setText(couponNum.toString());
                    mCouponNum.setVisibility(View.VISIBLE);
                    mCouponLargeNum.setVisibility(View.INVISIBLE);
                }
                if (messageNum == 0) {
                    mMessageNum.setText("");
                    mMessageNum.setVisibility(View.INVISIBLE);
                    mMessageLargeNum.setVisibility(View.INVISIBLE);
                } else if (messageNum > 9) {
                    mMessageLargeNum.setText(messageNum.toString());
                    mMessageLargeNum.setVisibility(View.VISIBLE);
                    mMessageNum.setVisibility(View.INVISIBLE);
                } else {
                    mMessageNum.setText(messageNum.toString());
                    mMessageNum.setVisibility(View.VISIBLE);
                    mMessageLargeNum.setVisibility(View.INVISIBLE);
                }
                SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("account", data.get("account").getAsString());
                editor.putString("name", data.get("name").getAsString());
                editor.putFloat("balance", data.get("balance").getAsFloat());
                editor.putInt("couponNum", data.get("couponNum").getAsInt());
                editor.putInt("messageNum", data.get("messageNum").getAsInt());
                editor.putString("city", data.get("city").getAsString());
                editor.putInt("appCanRecharge", data.get("appCanRecharge").getAsInt());
                JsonArray bindCarList = data.get("bindCarList").getAsJsonArray();
                ArrayList<BindCar> bindCarArrayList = new ArrayList<>();
                for (int i = 0; i < bindCarList.size(); i++) {
                    JsonObject jsonObject = bindCarList.get(i).getAsJsonObject();
                    BindCar bindCar = new BindCar();
                    bindCar.setId(jsonObject.get("id").getAsInt());
                    bindCar.setPlate_number(jsonObject.get("plate_number").getAsString());
                    bindCar.setStatus(jsonObject.get("status").getAsInt());
                    bindCar.setVin(jsonObject.get("vin").getAsString());
                    if (jsonObject.has("account_type")){
                        bindCar.setAccount_type(jsonObject.get("account_type").getAsInt());
                    }
                    bindCar.setBusiness_type(jsonObject.get("business_type").getAsInt());
                    bindCarArrayList.add(bindCar);
                }
                boolean isduigong = false;
                for (int i = 0; i < bindCarArrayList.size(); i++) {
                    if (bindCarArrayList.get(i).getAccount_type() == 1 && bindCarArrayList.get(i).getBusiness_type() == 6) {
                        isduigong = true;
                    }
                }
                double money;
                if (isduigong) {
                    money = data.get("companyBalance").getAsDouble();
//                    tv_mile.setText("账户里程：" + data.get("companyMile").getAsInt() + "km");
                } else {
                    money = data.get("balance").getAsDouble();
                    //companyMile
//                    tv_mile.setText("账户里程：" + data.get("mile").getAsInt() + "km");
                }
                mLeftMoney.setText("换电账户余额：" +money+"元");
                SharedPreferences sharedPreferences = MainApp.getInstance().getSharedPreferences("carplate", Context.MODE_PRIVATE);
                String carplateSave = sharedPreferences.getString("carplate" + User.getInstance().getAccountId(), "");
                if (bindCarArrayList.size() > 0) {
                    if (bindCarArrayList.size() == 1) {
                        SharedPreferences sharedPreferencesme = MainApp.getInstance().getSharedPreferences("carplate", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorme = sharedPreferencesme.edit();
                        editorme.putString("carplate" + User.getInstance().getAccountId(), bindCarArrayList.get(0).getPlate_number());
                        editorme.putInt("id" + User.getInstance().getAccountId(), bindCarArrayList.get(0).getId());
                        editorme.putString("vin" + User.getInstance().getAccountId(), bindCarArrayList.get(0).getVin());
                        editorme.commit();
                    } else {
                        int position = -1;
                        BindCar bindCar = new BindCar();
                        for (int i = 0; i < bindCarArrayList.size(); i++) {
                            if (carplateSave.equals(bindCarArrayList.get(i).getPlate_number())) {
                                bindCar.setStatus(bindCarArrayList.get(i).getStatus());
                                bindCar.setId(bindCarArrayList.get(i).getId());
                                bindCar.setPlate_number(bindCarArrayList.get(i).getPlate_number());
                                position = i;
                            }
                        }
                        if (position != -1) {
                            bindCarArrayList.remove(position);
                            bindCarArrayList.add(0, bindCar);
                        }
                    }
                }
                searchList = bindCarArrayList;
                if (bindCarArrayList.size() == 0) {
                    mLeftMoney.setVisibility(View.GONE);
//                    tv_mile.setVisibility(View.GONE);
                } else {
                    mLeftMoney.setVisibility(View.VISIBLE);
//                    tv_mile.setVisibility(View.VISIBLE);
                }
//                editor.putString("bindCarList", GsonUtils.getInstance().toJson(bindCarArrayList));
                SharedPreferencesHelper.getInstance(getContext()).putAccountString("bindCarList", GsonUtils.getInstance().toJson(bindCarArrayList));
                editor.putBoolean("hasChargeAccount", data.get("hasChargeAccount").getAsBoolean());
                if (data.get("hasChargeAccount").getAsBoolean()) {
                    isChongdian = true;
                    chongdian_money.setVisibility(View.VISIBLE);
                    re_chongdian.setVisibility(View.VISIBLE);
                    money_chongdian = data.get("chargeBalance").getAsFloat();
                    chongdian_money.setText("充电账户余额：" + df.format(money_chongdian) + "元");
                    if (!data.get("chargeBalance").equals(JsonNull.INSTANCE)) {
                        editor.putFloat("chargeBalance", data.get("chargeBalance").getAsFloat());
                    }
                } else {
                    chongdian_money.setVisibility(View.GONE);
                    re_chongdian.setVisibility(View.GONE);
                    isChongdian = false;
                }
                editor.putBoolean("hasAuthAccount", data.get("hasAuthAccount").getAsBoolean());
                JsonArray chongdianBindCarList = null;
                if (!data.get("chargeBindCarList").equals(JsonNull.INSTANCE)) {
                    chongdianBindCarList = data.get("chargeBindCarList").getAsJsonArray();
                    ArrayList<BindCar> chongdianbindCarArrayList = new ArrayList<>();
                    for (int i = 0; i < chongdianBindCarList.size(); i++) {
                        JsonObject jsonObject = chongdianBindCarList.get(i).getAsJsonObject();
                        BindCar bindCar = new BindCar();
                        bindCar.setId(jsonObject.get("id").getAsInt());
                        bindCar.setPlate_number(jsonObject.get("plate_number").getAsString());
                        bindCar.setStatus(jsonObject.get("status").getAsInt());
                        chongdianbindCarArrayList.add(bindCar);
                    }
                    SharedPreferencesHelper.getInstance(getContext()).putAccountString("chargeBindCarList", GsonUtils.getInstance().toJson(chongdianbindCarArrayList));
                }
                editor.commit();
                int appCanRecharge = data.get("appCanRecharge").getAsInt();
                showRechargeButton(appCanRecharge);
                account = data.get("account").getAsString();
                recyclerView.setAdapter(new CarIdentAdapter(MainApp.getInstance().getApplicationContext(), bindCarArrayList, new CarIdentInterface() {
                    @Override
                    public void itemClick(int id, String plateNum, String vin) {
                        bindId = id;
                        createARCode(account, id);
                        showSelectCarId(plateNum, id, vin);
                    }
                }));
                QRCodeInit(bindCarArrayList, data.get("account").getAsString());
                requestQueueNum(account, bindId);
                refreshQueueNum(account, bindId);
                isHaveOrder = data.get("hasUnpaidChargeOrder").getAsBoolean();
                if (!data.get("connectorId").equals(JsonNull.INSTANCE)) {
                    connector = data.get("connectorId").getAsString();
                }
                if (!data.get("startChargeSeq").equals(JsonNull.INSTANCE)) {
                    sep = data.get("startChargeSeq").getAsString();
                }
                if (isHaveOrder) {
                    loadConnector(connector, sep);
                }
                accountRefresh.finishRefresh();
                if (isChongdian) {
                    requestCoupon();
                }
                if (data.has("tipsNotBatteryCar")){
                    boolean isbudaidianche = data.get("tipsNotBatteryCar").getAsBoolean();
                    if (isbudaidianche){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("您的车已锁定，请购买基础套餐");
                        builder.setNeutralButton("去购买", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getContext(), PromotionsActivity.class);
                                startActivity(intent);
//                                Intent intent = new Intent(getContext(), ChongdianZhongActivity.class);
//                                intent.putExtra("info", moder.getContent());
//                                intent.putExtra("sep", startChargeSep);
//                                intent.putExtra("isdingdan", true);
//                                startActivity(intent);
                            }
                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Log.e(Utils.TAG, "Exception = " + e.toString());
                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                accountRefresh.finishRefresh();
            }
        });
    }

    private void loadConnector(String connectorId, final String startChargeSep) {
        final String url = API.BASE_URL + API.CHONGDIAN_INFOR;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        requestParams.add("connectorId", connectorId);
        PersistentCookieStore cookieStore = new PersistentCookieStore(getContext());
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                final ChongdianzhuangInfoModer moder = GsonUtils.getInstance().fromJson(json, ChongdianzhuangInfoModer.class);
                if (moder.isSuccess()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("您有一个未结束的充电订单，点击确定进入订单");
                    builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent intent = new Intent(getContext(), ChongdianZhongActivity.class);
                            intent.putExtra("info", moder.getContent());
                            intent.putExtra("sep", startChargeSep);
                            intent.putExtra("isdingdan", true);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                } else {
                    Utils.toast(getContext(), moder.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(getContext(), API.error_internet);
            }
        });
    }
}
