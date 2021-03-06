 package com.btkj.chongdianbao.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityStationListBinding;
import com.btkj.chongdianbao.databinding.AdapterStationBinding;
import com.btkj.chongdianbao.mine.MineActivity;
import com.btkj.chongdianbao.model.ServiceListModel;
import com.btkj.chongdianbao.model.StartReserverModel;
import com.btkj.chongdianbao.model.Station;
import com.btkj.chongdianbao.model.StationListModel;
import com.btkj.chongdianbao.model.User;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.BaseDialog;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.GsonUtils;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationListActivity extends BaseActivity implements View.OnClickListener, ItemClick, INaviInfoCallback, PromotionCarSelect {
    private ActivityStationListBinding binding;
    private List<Station> list;
    private List<Station> selectstationlist;
    private int pageNo = 1;
    private int cityId, bindId, cartype, batteryCount;
    private MyAdapter adapter;
    private String cityName;
    private Station station;
    private EasyPopup customPopup;
    private final int REQUESTCODE_CITY = 1002;
    private StationSelectFragment stationSelectFragment;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_station_list);
        binding.imgLeft.setOnClickListener(this);
        binding.tvRight.setOnClickListener(this);
        binding.yuyue.setOnClickListener(this);
        binding.imgYuyueCancel.setOnClickListener(this);
        binding.imgYuyueDelay.setOnClickListener(this);
        binding.tvCity.setOnClickListener(this);
        binding.tvStationName.setOnClickListener(this);
        binding.imgKefu.setOnClickListener(this);
        list = (List<Station>) getIntent().getSerializableExtra("list");
        selectstationlist = (List<Station>) getIntent().getSerializableExtra("lists");
        cityId = getIntent().getIntExtra(API.cityId, 0);
        bindId = getIntent().getIntExtra(API.bindId, 0);
        cartype = getIntent().getIntExtra(API.carType, 0);
        batteryCount = getIntent().getIntExtra(API.batterycount, 0);
        cityName = getIntent().getStringExtra(API.cityName);
        binding.tvCity.setText(cityName + "");
        if (list != null) {
            binding.image.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
        }
        adapter = new MyAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        if (User.getInstance().isYuyue()) {
            binding.yuyue.setVisibility(View.GONE);
            binding.reBottom.setVisibility(View.VISIBLE);
            if (User.getInstance().isDelay()) {
                binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_success_delay));
            }
        }
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                loadData(true, cityId, pageNo, 20, User.getInstance().getMylatitude(), User.getInstance().getMylongtitude(), binding.tvStationName.getText().toString());
            }
        });
        binding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo = pageNo + 1;
                loadData(false, cityId, pageNo, 20, User.getInstance().getMylatitude(), User.getInstance().getMylongtitude(), binding.tvStationName.getText().toString());
            }
        });
        if (list == null){
            loadData(true, cityId, 1, 20, User.getInstance().getMylatitude(), User.getInstance().getMylongtitude(), binding.tvStationName.getText().toString());
        }
    }

    private void loadfinish() {
        binding.smartRefresh.finishRefresh();
        binding.smartRefresh.finishLoadMore();
    }

    private void loadData(boolean b, int cityId, int pageNo, int pageSize, double lat, double lon, String name) {
        /**
         * cityId    [int]		??????Id
         * pageNo    [int]		??????????????????1
         * pageSize  [int]		???/???????????????20
         * name      [string]		????????????????????????
         * longitude [double]		????????????
         * latitude  [double]		????????????
         */
        final String url = API.BASE_URL + API.STATIONLIST;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("cityId", String.valueOf(cityId));
        map.put("pageNo", String.valueOf(pageNo));
        if (!name.isEmpty()) {
            map.put("name", name);
        }
        map.put("pageSize", String.valueOf(pageSize));
        map.put("longitude", String.valueOf(lon));
        map.put("latitude", String.valueOf(lat));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, StationListActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                StationListModel model = GsonUtils.getInstance().fromJson(json, StationListModel.class);
                if (b) {
                    list.clear();
                }
                list.addAll(model.getContent());
                binding.image.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
                adapter.notifyDataSetChanged();
            }
        }, binding.smartRefresh);
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.setCookieStore(cookieStore);
//        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                loadfinish();
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                StationListModel model = GsonUtils.getInstance().fromJson(json, StationListModel.class);
//                if (b) {
//                    list.clear();
//                }
//                list.addAll(model.getContent());
//                binding.imgEmpty.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                loadfinish();
//                Utils.snackbar(getApplicationContext(), StationListActivity.this, API.net_error);
//                Utils.log(url, requestParams, "");
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                Intent intent = new Intent(StationListActivity.this, MineActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_right:
                Intent intents = new Intent();
                setResult(2, intents);
                finish();
                break;
            case R.id.yuyue:
                if (bindId == 0 || batteryCount == 0 || cartype == 0) {
                    Utils.snackbar(getApplicationContext(), StationListActivity.this, "??????????????????");
                    return;
                }
                startYuyue();
                break;
            case R.id.img_yuyue_delay:
                if (User.getInstance().isDelay()) {
                    Utils.snackbar(getApplicationContext(), StationListActivity.this, "?????????????????????");
                    return;
                }
                BaseDialog.showDialog(StationListActivity.this, "???????????????????????????10??????", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delayappoint();
                    }
                });
                break;
            case R.id.img_yuyue_cancel:
                BaseDialog.showDialog(StationListActivity.this, "???????????????????????????", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelappoint();
                    }
                });
                break;
            case R.id.tv_station_name:
                stationSelectFragment = new StationSelectFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(API.station, (Serializable) selectstationlist);
                bundle.putBoolean(API.ismain, false);
                stationSelectFragment.setArguments(bundle);
                stationSelectFragment.show(getSupportFragmentManager(), "data");
                break;
            case R.id.img_kefu:
                loadkefu();
                break;
            case R.id.tv_city:
                intent = new Intent(StationListActivity.this, ChoseCityActivity.class);
                startActivityForResult(intent, REQUESTCODE_CITY);
                break;
            default:
                break;
        }
    }

    @Override
    public void click(Station station) {
        if (User.getInstance().isYuyue()) {
            Utils.snackbar(getApplicationContext(), StationListActivity.this, "??????????????????????????????????????????");
            return;
        }
        if (station == null) {
            binding.tvStationName.setText("");
        }else {
            binding.tvStationName.setText(station.getName());
        }
        loadData(true, cityId, 1, 20, User.getInstance().getMylatitude(), User.getInstance().getMylongtitude(), binding.tvStationName.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_CITY && resultCode == 1) {
            binding.tvCity.setText(data.getStringExtra(API.cityName));
            cityId = data.getIntExtra(API.cityId, 0);
            double longtitude = Double.parseDouble(data.getStringExtra(API.longtitude));
            double latitude = Double.parseDouble(data.getStringExtra(API.latitude));
            loadData(true, cityId, 1, 20, latitude, longtitude, binding.tvStationName.getText().toString());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intents = new Intent();
        setResult(2, intents);
        finish();
    }

    private void loadkefu() {
        String url = API.BASE_URL + API.SERVICELIST;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, StationListActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                ServiceListModel model = GsonUtils.getInstance().fromJson(json, ServiceListModel.class);
                customPopup = new EasyPopup(StationListActivity.this)
                        .setContentView(R.layout.layout_easy_popup)
                        //??????????????????PopupWindow?????????????????????
                        .setFocusAndOutsideEnable(true)
                        .createPopup();
                customPopup.showAtAnchorView(binding.imgKefu, VerticalGravity.BELOW, HorizontalGravity.ALIGN_RIGHT, 0, 0);
                TextView tv_phone = customPopup.getView(R.id.tv_phone);
                TextView tv_time = customPopup.getView(R.id.tv_time);
                TextView tv_call = customPopup.getView(R.id.tv_call);
                tv_phone.setText("????????????\n" + model.getContent().get(0).getValue());
                tv_time.setText(model.getContent().get(1).getValue() + "\n" + model.getContent().get(2).getValue());
                tv_call.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + model.getContent().get(0).getValue());
                    intent.setData(data);
                    startActivity(intent);
                });
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                ServiceListModel model = GsonUtils.getInstance().fromJson(json, ServiceListModel.class);
//                if (model.isSuccess()) {
//                    customPopup = new EasyPopup(StationListActivity.this)
//                            .setContentView(R.layout.layout_easy_popup)
//                            //??????????????????PopupWindow?????????????????????
//                            .setFocusAndOutsideEnable(true)
//                            .createPopup();
//                    customPopup.showAtAnchorView(binding.imgKefu, VerticalGravity.BELOW, HorizontalGravity.ALIGN_RIGHT, 0, 0);
//                    TextView tv_phone = customPopup.getView(R.id.tv_phone);
//                    TextView tv_time = customPopup.getView(R.id.tv_time);
//                    TextView tv_call = customPopup.getView(R.id.tv_call);
//                    tv_phone.setText("????????????\n" + model.getContent().get(0).getValue());
//                    tv_time.setText(model.getContent().get(1).getValue() + "\n" + model.getContent().get(2).getValue());
//                    tv_call.setOnClickListener(view -> {
//                        Intent intent = new Intent(Intent.ACTION_DIAL);
//                        Uri data = Uri.parse("tel:" + model.getContent().get(0).getValue());
//                        intent.setData(data);
//                        startActivity(intent);
//                    });
//                } else {
//                    Utils.snackbar(getApplicationContext(), StationListActivity.this, model.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), StationListActivity.this, API.net_error);
//                Utils.log(url, requestParams, null);
//            }
//        });
    }

    private void startYuyue() {
        String url = API.BASE_URL + API.ADDAPPOINT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("stationId", String.valueOf(station.getId()));
        map.put("bindId", String.valueOf(bindId));
        map.put("batteryCount", String.valueOf(batteryCount));
        map.put("batteryType", String.valueOf(cartype));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, StationListActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                binding.yuyue.setVisibility(View.GONE);
                binding.reBottom.setVisibility(View.VISIBLE);
                StartReserverModel startReserverModel = GsonUtils.getInstance().fromJson(json, StartReserverModel.class);
                User.getInstance().setReserverId(startReserverModel.getContent().getId());
                User.getInstance().setStation(station);
                User.getInstance().setYuyue(true);
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.setCookieStore(cookieStore);
//        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                StartReserverModel startReserverModel = GsonUtils.getInstance().fromJson(json, StartReserverModel.class);
//                if (startReserverModel.isSuccess()) {
//                    binding.yuyue.setVisibility(View.GONE);
//                    binding.reBottom.setVisibility(View.VISIBLE);
//                    User.getInstance().setReserverId(startReserverModel.getContent().getId());
//                    User.getInstance().setStation(station);
//                    User.getInstance().setYuyue(true);
//                } else {
//                    Utils.snackbar(getApplicationContext(), StationListActivity.this, startReserverModel.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), StationListActivity.this, API.net_error);
//                Utils.log(url, requestParams, "");
//            }
//        });
    }

    //????????????
    private void delayappoint() {
        //id  [int]	???	??????id
        if (!User.getInstance().isYuyue()) {
            Utils.snackbar(getApplicationContext(), StationListActivity.this, API.yueyue_code);
            return;
        }
        String url = API.BASE_URL + API.DELAYAPPOINT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(User.getInstance().getReserverId()));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, StationListActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_success_delay));
                User.getInstance().setDelay(true);
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.setCookieStore(cookieStore);
//        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                ReserverDelayModel model = GsonUtils.getInstance().fromJson(json, ReserverDelayModel.class);
//                if (model.isSuccess()) {
//                    binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_success_delay));
//                    User.getInstance().setDelay(true);
//                } else {
//                    Utils.snackbar(getApplicationContext(), StationListActivity.this, model.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), StationListActivity.this, API.net_error);
//                Utils.log(url, requestParams, "shibai");
//            }
//        });
    }

    //????????????
    private void cancelappoint() {
        //id  [int]	???	??????id
        if (!User.getInstance().isYuyue()) {
            Utils.snackbar(getApplicationContext(), StationListActivity.this, API.yueyue_code);
            return;
        }
        String url = API.BASE_URL + API.CANCELAPPOINT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(User.getInstance().getReserverId()));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, StationListActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_delay));
                User.getInstance().setDelay(false);
                User.getInstance().setYuyue(false);
                binding.reBottom.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.setCookieStore(cookieStore);
//        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                BaseObject model = GsonUtils.getInstance().fromJson(json, BaseObject.class);
//                if (model.isSuccess()) {
//                    binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_delay));
//                    User.getInstance().setDelay(false);
//                    User.getInstance().setYuyue(false);
//                    binding.reBottom.setVisibility(View.GONE);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Utils.snackbar(getApplicationContext(), StationListActivity.this, model.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.snackbar(getApplicationContext(), StationListActivity.this, API.net_error);
//                Utils.log(url, requestParams, "shibai");
//            }
//        });
    }

    @Override
    public void clickItem(Station stations, int position) {
        binding.yuyue.setVisibility(View.GONE);
        if (stations.getStatusApp() == 0) {
            if (stations.getAvailBatteryCountMap().getKwh10() + stations.getAvailBatteryCountMap().getKwh12() + stations.getAvailBatteryCountMap().getKwh15() >= 2) {
                binding.yuyue.setVisibility(View.VISIBLE);
            }
        }
        this.station = stations;
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIsclick(false);
        }
        list.get(position).setIsclick(true);
        adapter.notifyDataSetChanged();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ItemClick itemClick;

        public MyAdapter(ItemClick itemClick) {
            this.itemClick = itemClick;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(StationListActivity.this).inflate(R.layout.adapter_station, null);
            AdapterStationBinding binding = AdapterStationBinding.bind(view);
            MyViewHolder holder = new MyViewHolder(view, binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            int number = -1;
            if (User.getInstance().isYuyue()){
                for (int i = 0; i < list.size(); i++) {
                    if (User.getInstance().getStation().getId()==list.get(i).getId()){
                        number = i;
                    }
                }
                Utils.log(null,null,number+"number");
                if (position == number){
                    holder.getBinding().re.setBackground(getResources().getDrawable(R.drawable.station_item1));
                }
            }
            Station station = list.get(position);
            if (station.isIsclick() || number == position) {
                holder.getBinding().re.setBackground(getResources().getDrawable(R.drawable.station_item1));
            } else {
                holder.getBinding().re.setBackground(getResources().getDrawable(R.drawable.station_item2));
            }
            holder.getBinding().tvName.setText(station.getName());
            holder.getBinding().tvAddress.setText(station.getAddress());
            holder.getBinding().tvPhone.setText(station.getTelephone());
            holder.getBinding().tvClock.setText(station.getWorkTime());
            if (station.getDistance() < 1000) {
                holder.getBinding().tvDistance.setText("??????\n" + station.getDistance() + "m");
            } else {
                holder.getBinding().tvDistance.setText("??????\n" + station.getDistance() / 1000 + "km");
            }
//            holder.getBinding().tvBatterycount.setText("???????????????" + station.getAvailBatteryCountMap().getKwh10() +
//                    station.getAvailBatteryCountMap().getKwh12() + station.getAvailBatteryCountMap().getKwh15());
            holder.getBinding().re.setOnClickListener(view -> {
                if (!User.getInstance().isYuyue()) {
                    itemClick.clickItem(station, position);
                }
            });
            if (station.getStatusApp() == 0) {
                if (station.getAvailBatteryCountMap().getKwh10() + station.getAvailBatteryCountMap().getKwh12() + station.getAvailBatteryCountMap().getKwh15() >= 2) {
                    holder.getBinding().imgStatus.setBackgroundResource(R.mipmap.station_status_normal);
                } else {
                    holder.getBinding().imgStatus.setBackgroundResource(R.mipmap.station_status_full);
                }
            } else if (station.getStatusApp() == 1) {
                holder.getBinding().imgStatus.setBackgroundResource(R.mipmap.station_status_close);
            }
            holder.getBinding().tvDistance.setOnClickListener(view -> {
                Poi start = new Poi("", new com.amap.api.maps.model.LatLng(User.getInstance().getMylatitude(), User.getInstance().getMylongtitude()), "");
                /**?????????????????????????????????,??????POI???ID "B000A83M61"????????????????????????????????????????????????????????????????????????**/
                /**Poi????????????????????????PoiID???PoiiD????????????????????????Poiid?????????????????????????????????**/
                Poi end = new Poi("", new com.amap.api.maps.model.LatLng(station.getLatitude(), station.getLongitude()), "");
                AmapNaviPage.getInstance().showRouteActivity(StationListActivity.this, new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI), StationListActivity.this);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterStationBinding binding;

        public MyViewHolder(View itemView, AdapterStationBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public AdapterStationBinding getBinding() {
            return binding;
        }
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public View getCustomMiddleView() {
        return null;
    }

    @Override
    public void onNaviDirectionChanged(int i) {

    }

    @Override
    public void onDayAndNightModeChanged(int i) {

    }

    @Override
    public void onBroadcastModeChanged(int i) {

    }

    @Override
    public void onScaleAutoChanged(boolean b) {

    }
}
