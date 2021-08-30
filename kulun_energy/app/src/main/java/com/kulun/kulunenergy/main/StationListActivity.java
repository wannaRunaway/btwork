package com.kulun.kulunenergy.main;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.customizeView.ItemClick;
import com.kulun.kulunenergy.customizeView.PromotionCarSelect;
import com.kulun.kulunenergy.databinding.ActivityStationListBinding;
import com.kulun.kulunenergy.databinding.AdapterStationBinding;
import com.kulun.kulunenergy.mine.MineActivity;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.modelnew.CustomerServicebean;
import com.kulun.kulunenergy.modelnew.StationContent;
import com.kulun.kulunenergy.modelnew.StationInfo;
import com.kulun.kulunenergy.requestparams.Response;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationListActivity extends BaseActivity implements View.OnClickListener, ItemClick, INaviInfoCallback, PromotionCarSelect {
    private ActivityStationListBinding binding;
    private List<StationInfo> list;
    private List<StationInfo> selectstationlist;
    private int pageNo = 1;
    private int cityId, bindId, cartype, batteryCount;
    private MyAdapter adapter;
    private String cityName;
    private StationInfo station;
    private EasyPopup customPopup;
    private final int REQUESTCODE_CITY = 1002;
    private StationSelectFragment stationSelectFragment;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_station_list);
        binding.imgLeft.setOnClickListener(this);
        binding.tvRight.setOnClickListener(this);
//        binding.yuyue.setOnClickListener(this);
//        binding.imgYuyueCancel.setOnClickListener(this);
//        binding.imgYuyueDelay.setOnClickListener(this);
        binding.tvCity.setOnClickListener(this);
        binding.tvStationName.setOnClickListener(this);
        binding.imgKefu.setOnClickListener(this);
        list = (List<StationInfo>) getIntent().getSerializableExtra("list");
        selectstationlist = (List<StationInfo>) getIntent().getSerializableExtra("lists");
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
//        if (User.getInstance().isYuyue()) {
//            binding.yuyue.setVisibility(View.GONE);
//            binding.reBottom.setVisibility(View.VISIBLE);
//            if (User.getInstance().isDelay()) {
//                binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_success_delay));
//            }
//        }
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
        if (list == null) {
            loadData(true, cityId, 1, 20, User.getInstance().getMylatitude(), User.getInstance().getMylongtitude(), binding.tvStationName.getText().toString());
        }
    }

    private void loadData(boolean b, int cityId, int pageNo, int pageSize, double lat, double lon, String name) {
        /**
         * cityId    [int]		城市Id
         * pageNo    [int]		页数，默认值1
         * pageSize  [int]		条/也。默认值20
         * name      [string]		站点名称模糊查询
         * longitude [double]		位置信息
         * latitude  [double]		位置信息
         */
        Map<String, String> map = new HashMap<>();
        map.put("accountId", String.valueOf(User.getInstance().getAccountId(this)));
        map.put("cityId", String.valueOf(cityId));
        map.put("pageNo", "1");
        map.put("pageSize", "100");
        new MyRequest().req(API.URL_STATION_LIST, map, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray) {
                StationContent stationContent = GsonUtils.getInstance().fromJson(json, StationContent.class);
                if (b) {
                    list.clear();
                }
                list.addAll(stationContent.getData());
                binding.image.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
//        final String url = API.BASE_URL + API.URL_STATION_LIST;
//        RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("cityId", String.valueOf(cityId));
//        map.put("pageNo", String.valueOf(pageNo));
//        if (!name.isEmpty()) {
//            map.put("name", name);
//        }
//        map.put("pageSize", String.valueOf(pageSize));
//        map.put("longitude", String.valueOf(lon));
//        map.put("latitude", String.valueOf(lat));
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().mysmartrequest(url, requestParams, StationListActivity.this, getApplicationContext(), true, new Myparams() {
//            @Override
//            public void message(String json) {
//                StationListModel model = GsonUtils.getInstance().fromJson(json, StationListModel.class);
//                if (b) {
//                    list.clear();
//                }
//                list.addAll(model.getContent());
//                binding.image.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
//                adapter.notifyDataSetChanged();
//            }
//        }, binding.smartRefresh);
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
//            case R.id.yuyue:
//                if (bindId == 0 || batteryCount == 0 || cartype == 0) {
//                    Utils.snackbar(StationListActivity.this, "请先选择车辆");
//                    return;
//                }
//                startYuyue();
//                break;
//            case R.id.img_yuyue_delay:
//                if (User.getInstance().isDelay()) {
//                    Utils.snackbar( StationListActivity.this, "只能延迟一次哦");
//                    return;
//                }
//                BaseDialog.showDialog(StationListActivity.this, "您是否需要延迟预约10分钟", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        delayappoint();
//                    }
//                });
//                break;
//            case R.id.img_yuyue_cancel:
//                BaseDialog.showDialog(StationListActivity.this, "您是否确定取消预约", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        cancelappoint();
//                    }
//                });
//                break;
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
    public void click(StationInfo station) {
//        if (User.getInstance().isYuyue()) {
//            Utils.snackbar( StationListActivity.this, "正在预约中，无法选择其他站点");
//            return;
//        }
        if (station == null) {
            binding.tvStationName.setText("");
        } else {
            binding.tvStationName.setText(station.getStationName());
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
        Map<String, String> map = new HashMap();
        map.put("accountId", String.valueOf(User.getInstance().getAccountId(this)));
        new MyRequest().req(API.URL_CUSTOMERSERVICELIST, map, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray) {
                List<CustomerServicebean> list = GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<CustomerServicebean>>() {
                }.getType());
                if (list != null && list.size() > 0) {
                    CustomerServicebean databean = list.get(0);
                    customPopup = new EasyPopup(StationListActivity.this)
                            .setContentView(R.layout.layout_easy_popup)
                            //是否允许点击PopupWindow之外的地方消失
                            .setFocusAndOutsideEnable(true)
                            .createPopup();
                    customPopup.showAtAnchorView(binding.imgKefu, VerticalGravity.BELOW, HorizontalGravity.ALIGN_RIGHT, 0, 0);
                    TextView tv_phone = customPopup.getView(R.id.tv_phone);
                    TextView tv_time = customPopup.getView(R.id.tv_time);
                    TextView tv_call = customPopup.getView(R.id.tv_call);
                    tv_phone.setText("客服电话\n" + databean.getPhone());
                    tv_time.setText(databean.getWorkTime());
                    tv_call.setOnClickListener(view -> {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + databean.getPhone());
                        intent.setData(data);
                        startActivity(intent);
                    });
                }
            }
        });
//        String url = API.BASE_URL + API.SERVICELIST;
//        RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().myrequest(url, requestParams, StationListActivity.this, getApplicationContext(), true, new Myparams() {
//            @Override
//            public void message(String json) {
//                ServiceListModel model = GsonUtils.getInstance().fromJson(json, ServiceListModel.class);
//                customPopup = new EasyPopup(StationListActivity.this)
//                        .setContentView(R.layout.layout_easy_popup)
//                        //是否允许点击PopupWindow之外的地方消失
//                        .setFocusAndOutsideEnable(true)
//                        .createPopup();
//                customPopup.showAtAnchorView(binding.imgKefu, VerticalGravity.BELOW, HorizontalGravity.ALIGN_RIGHT, 0, 0);
//                TextView tv_phone = customPopup.getView(R.id.tv_phone);
//                TextView tv_time = customPopup.getView(R.id.tv_time);
//                TextView tv_call = customPopup.getView(R.id.tv_call);
//                tv_phone.setText("客服电话\n" + model.getContent().get(0).getValue());
//                tv_time.setText(model.getContent().get(1).getValue() + "\n" + model.getContent().get(2).getValue());
//                tv_call.setOnClickListener(view -> {
//                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                    Uri data = Uri.parse("tel:" + model.getContent().get(0).getValue());
//                    intent.setData(data);
//                    startActivity(intent);
//                });
//            }
//        });
    }

//    private void startYuyue() {
//        String url = API.BASE_URL + API.ADDAPPOINT;
//        RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("stationId", String.valueOf(station.ge));
//        map.put("bindId", String.valueOf(bindId));
//        map.put("batteryCount", String.valueOf(batteryCount));
//        map.put("batteryType", String.valueOf(cartype));
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().myrequest(url, requestParams, StationListActivity.this, getApplicationContext(), true, new Myparams() {
//            @Override
//            public void message(String json) {
//                binding.yuyue.setVisibility(View.GONE);
//                binding.reBottom.setVisibility(View.VISIBLE);
//                StartReserverModel startReserverModel = GsonUtils.getInstance().fromJson(json, StartReserverModel.class);
//                User.getInstance().setReserverId(startReserverModel.getContent().getId());
//                User.getInstance().setStation(station);
//                User.getInstance().setYuyue(true);
//            }
//        });
//    }

    //延迟预约
//    private void delayappoint() {
//        //id  [int]	是	预约id
//        if (!User.getInstance().isYuyue()) {
//            Utils.snackbar( StationListActivity.this, API.yueyue_code);
//            return;
//        }
//        String url = API.BASE_URL + API.DELAYAPPOINT;
//        RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("id", String.valueOf(User.getInstance().getReserverId()));
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().myrequest(url, requestParams, StationListActivity.this, getApplicationContext(), true, new Myparams() {
//            @Override
//            public void message(String json) {
//                binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_success_delay));
//                User.getInstance().setDelay(true);
//            }
//        });
//    }

    //取消预约
//    private void cancelappoint() {
//        //id  [int]	是	预约id
//        if (!User.getInstance().isYuyue()) {
//            Utils.snackbar(StationListActivity.this, API.yueyue_code);
//            return;
//        }
//        String url = API.BASE_URL + API.CANCELAPPOINT;
//        RequestParams requestParams = new RequestParams();
//        Map<String, String> map = new HashMap<>();
//        map.put("id", String.valueOf(User.getInstance().getReserverId()));
//        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
//        new MyRequest().myrequest(url, requestParams, StationListActivity.this, getApplicationContext(), true, new Myparams() {
//            @Override
//            public void message(String json) {
//                binding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.station_btn_delay));
//                User.getInstance().setDelay(false);
//                User.getInstance().setYuyue(false);
//                binding.reBottom.setVisibility(View.GONE);
//                adapter.notifyDataSetChanged();
//            }
//        });
//    }

    @Override
    public void clickItem(StationInfo stations, int position) {
//        binding.yuyue.setVisibility(View.GONE);
//        if (stations.getStatusApp() == 0) {
//            if (stations.getAvailBatteryCountMap().getKwh10() + stations.getAvailBatteryCountMap().getKwh12() + stations.getAvailBatteryCountMap().getKwh15() >= 2) {
//                binding.yuyue.setVisibility(View.VISIBLE);
//            }
//        }
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
//            if (User.getInstance().isYuyue()){
//                for (int i = 0; i < list.size(); i++) {
//                    if (User.getInstance().getStation().getId()==list.get(i).getId()){
//                        number = i;
//                    }
//                }
//                Utils.log(null,null,number+"number");
//                if (position == number){
//                    holder.getBinding().re.setBackground(getResources().getDrawable(R.drawable.station_item1));
//                }
//            }
            StationInfo station = list.get(position);
            if (station.isIsclick() || number == position) {
                holder.getBinding().re.setBackground(getResources().getDrawable(R.drawable.station_item1));
            } else {
                holder.getBinding().re.setBackground(getResources().getDrawable(R.drawable.station_item2));
            }
            holder.getBinding().tvName.setText(station.getStationName());
            holder.getBinding().tvAddress.setText(station.getAddress());
            holder.getBinding().tvPhone.setText(station.getPhone());
            holder.getBinding().tvClock.setText(station.getBeginTime());
            float distance = AMapUtils.calculateLineDistance(new LatLng(User.getInstance().getMylatitude(), User.getInstance().getMylongtitude()), new LatLng(station.getLatitude(), station.getLongitude()));
            DecimalFormat df = new DecimalFormat("#.00");
            if (distance < 1000) {
                holder.getBinding().tvDistance.setText("导航\n" + df.format(distance) + "m");
            } else {
                holder.getBinding().tvDistance.setText("导航\n" + df.format(distance / 1000) + "km");
            }
//            holder.getBinding().tvBatterycount.setText("可换电池：" + station.getAvailBatteryCountMap().getKwh10() +
//                    station.getAvailBatteryCountMap().getKwh12() + station.getAvailBatteryCountMap().getKwh15());
//            holder.getBinding().re.setOnClickListener(view -> {
//                if (!User.getInstance().isYuyue()) {
//                    itemClick.clickItem(station, position);
//                }
//            });
//            if (station.getStatusApp() == 0) {
//                if (station.getAvailBatteryCountMap().getKwh10() + station.getAvailBatteryCountMap().getKwh12() + station.getAvailBatteryCountMap().getKwh15() >= 2) {
//                    holder.getBinding().imgStatus.setBackgroundResource(R.mipmap.station_status_normal);
//                } else {
//                    holder.getBinding().imgStatus.setBackgroundResource(R.mipmap.station_status_full);
//                }
//            } else if (station.getStatusApp() == 1) {
//                holder.getBinding().imgStatus.setBackgroundResource(R.mipmap.station_status_close);
//            }
            holder.getBinding().tvDistance.setOnClickListener(view -> {
                Poi start = new Poi("", new com.amap.api.maps.model.LatLng(User.getInstance().getMylatitude(), User.getInstance().getMylongtitude()), "");
                /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
                /**Poi支持传入经纬度和PoiID，PoiiD优先级更高，使用Poiid算路，导航终点会更合理**/
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
