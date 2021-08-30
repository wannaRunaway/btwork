package com.kulun.energynet.main;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.kulun.energynet.R;
import com.kulun.energynet.customizeView.PromotionCarSelect;
import com.kulun.energynet.databinding.ActivityStationListBinding;
import com.kulun.energynet.databinding.AdapterStationBinding;
import com.kulun.energynet.mine.MapCustomActivity;
import com.kulun.energynet.mine.MineActivity;
import com.kulun.energynet.model.StationInfo;
import com.kulun.energynet.model.UseBind;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.DateUtils;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.utils.JsonSplice;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StationListActivity extends BaseActivity implements View.OnClickListener {
    private ActivityStationListBinding binding;
    private List<StationInfo> list = new ArrayList<>();
    private MyAdapter adapter;
    private final int REQUESTCODE_CITY = 1002;
    //    private StationSelectFragment stationSelectFragment;
    private int stationId = -1;
    private UseBind useBind;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_station_list);
        if (!TextUtils.isEmpty(UserLogin.getInstance().getCityName(this))) {
            binding.tvCity.setText(UserLogin.getInstance().getCityName(this));
        }
        binding.imgLeft.setOnClickListener(this);
        binding.tvRight.setOnClickListener(this);
        binding.yuyue.setOnClickListener(this);
        binding.tvCity.setOnClickListener(this);
        binding.imgKefu.setOnClickListener(this);
        useBind = (UseBind) getIntent().getSerializableExtra("useBind");
        binding.etStationName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    //此处做逻辑处理
                    loadData(UserLogin.getInstance().getLatitude(StationListActivity.this), UserLogin.getInstance().getLongtitude(StationListActivity.this), "-1", UserLogin.getInstance().getCityid(StationListActivity.this), binding.etStationName.getText().toString());
                    return true;
                }
                return false;
            }
        });
        binding.tvChoseStation.setOnClickListener(this);
        adapter = new MyAdapter(list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                binding.tvChoseStation.setText("全部");
                binding.etStationName.setText("");
                loadData(UserLogin.getInstance().getLatitude(StationListActivity.this), UserLogin.getInstance().getLongtitude(StationListActivity.this), "-1", UserLogin.getInstance().getCityid(StationListActivity.this), binding.etStationName.getText().toString());
            }
        });
        loadData(UserLogin.getInstance().getLatitude(StationListActivity.this), UserLogin.getInstance().getLongtitude(StationListActivity.this), "-1", UserLogin.getInstance().getCityid(StationListActivity.this), binding.etStationName.getText().toString());
    }

    private void loadData(double latitude, double longitude, String type, int cityId, String search) {
        //{
        //    "type": 1,
        //    "latitude": 30.298013,
        //    "longitude": 120.148848,
        //    "search": ""
        //}
        if (latitude == 0 || longitude == 0) {
            return;
        }
        String json = JsonSplice.leftparent + JsonSplice.yin + "longitude" + JsonSplice.yinandmao + longitude + JsonSplice.dou +
                JsonSplice.yin + "latitude" + JsonSplice.yinandmao + latitude + JsonSplice.dou +
                JsonSplice.yin + "type" + JsonSplice.yinandmao + type + JsonSplice.dou +
                JsonSplice.yin + "city_Id" + JsonSplice.yinandmao + cityId + JsonSplice.dou +
                JsonSplice.yin + "search" + JsonSplice.yinandmao + JsonSplice.yin + search + JsonSplice.yin + JsonSplice.rightparent;
        HashMap<String, String> map = new HashMap<>();
        map.put("longitude", String.valueOf(longitude));
        map.put("latitude", String.valueOf(latitude));
        map.put("type", type);
        map.put("city_Id", String.valueOf(cityId));
        new MyRequest().spliceJson(API.SITELIST, true, json, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                list.clear();
                list.addAll(GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<StationInfo>>() {
                }.getType()));
                for (int i = 0; i < list.size(); i++) {
                    StationInfo stationInfo = list.get(i);
                    float distance = AMapUtils.calculateLineDistance(new LatLng(UserLogin.getInstance().getMylatitude(StationListActivity.this), UserLogin.getInstance().getMylongtitude(StationListActivity.this)), new LatLng(stationInfo.getLatitude(), stationInfo.getLongitude()));
                    stationInfo.setDistances(distance);
                }
                Collections.sort(list);
                List<StationInfo> frontlist = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    StationInfo stationInfo = list.get(i);
                    boolean worktime = false;
                    String startTime = DateUtils.timeTotime(stationInfo.getStart_time());
                    String endTime = DateUtils.timeTotime(stationInfo.getEnd_time());
                    worktime = DateUtils.isBelong(startTime, endTime);
                    if (stationInfo.getType() == 0) {//0换电站
                        if (stationInfo.getStatus() == 2 | stationInfo.getBattery() == 0 | !worktime) {
                            frontlist.add(stationInfo);
                            list.remove(stationInfo);
                            i--;
                        }
                    } else if (stationInfo.getType() == 1) {//1充电站
                        if (stationInfo.getStatus() == 2 | stationInfo.getBattery() == 0 | !worktime | !stationInfo.isAppointment()) {
                            frontlist.add(stationInfo);
                            list.remove(stationInfo);
                            i--;
                        }
                    }
                }
                list.addAll(frontlist);
                adapter.notifyDataSetChanged();
                binding.image.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                if (Utils.getToken(this) == null || Utils.getToken(this).equals("")) {
                    Utils.toLogin(StationListActivity.this);
                    Utils.snackbar(this, "请先登陆");
                    return;
                }
                Intent intent = new Intent(StationListActivity.this, MineActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_right:
                back();
                break;
            case R.id.yuyue:
                if (Utils.getToken(this) == null || Utils.getToken(this).equals("")) {
                    Utils.toLogin(StationListActivity.this);
                    Utils.snackbar(this, "请先登陆");
                    return;
                }
                if (Utils.usebindisNotexist(useBind)) {
                    Utils.snackbar(StationListActivity.this, "请先选择您的车辆，才能预约站点");
                    return;
                }
                Intent intents = new Intent();
                intents.putExtra("stationId", stationId);
                setResult(2, intents);
                finish();
                break;
            case R.id.img_kefu:
                Utils.loadkefu(StationListActivity.this, binding.imgKefu);
                break;
            case R.id.tv_city:
                intent = new Intent(StationListActivity.this, ChoseCityActivity.class);
                startActivityForResult(intent, REQUESTCODE_CITY);
                break;
            case R.id.tv_chose_station:
                showEasyPopup();
                break;
            default:
                break;
        }
    }

    private EasyPopup customPopup;
    private TextView tv_phone, tv_time, tv_call;

    private void showEasyPopup() {
        if (customPopup == null) {
            customPopup = EasyPopup.create()
                    .setContentView(this, R.layout.layout_station_popup)
                    .setFocusAndOutsideEnable(true).apply();
            customPopup.showAtAnchorView(binding.tvChoseStation, YGravity.BELOW, XGravity.CENTER, 0, 0);
            tv_phone = customPopup.findViewById(R.id.tv_phone);
            tv_time = customPopup.findViewById(R.id.tv_time);
            tv_call = customPopup.findViewById(R.id.tv_call);
            tv_phone.setOnClickListener(v -> {
                binding.tvChoseStation.setText("全部");
                binding.etStationName.setText("");
                tv_phone.setTextColor(getResources().getColor(R.color.white));
                tv_time.setTextColor(getResources().getColor(R.color.blue_light));
                tv_call.setTextColor(getResources().getColor(R.color.blue_light));
                loadData(UserLogin.getInstance().getLatitude(StationListActivity.this), UserLogin.getInstance().getLongtitude(StationListActivity.this), "-1", UserLogin.getInstance().getCityid(StationListActivity.this), binding.etStationName.getText().toString());
                customPopup.dismiss();
            });
            tv_time.setOnClickListener(v -> {
                binding.tvChoseStation.setText("换电站");
                binding.etStationName.setText("");
                tv_time.setTextColor(getResources().getColor(R.color.white));
                tv_phone.setTextColor(getResources().getColor(R.color.blue_light));
                tv_call.setTextColor(getResources().getColor(R.color.blue_light));
                loadData(UserLogin.getInstance().getLatitude(StationListActivity.this), UserLogin.getInstance().getLongtitude(StationListActivity.this), "0", UserLogin.getInstance().getCityid(StationListActivity.this), binding.etStationName.getText().toString());
                customPopup.dismiss();
            });
            tv_call.setOnClickListener(v -> {
                binding.tvChoseStation.setText("换电宝");
                binding.etStationName.setText("");
                tv_call.setTextColor(getResources().getColor(R.color.white));
                tv_time.setTextColor(getResources().getColor(R.color.blue_light));
                tv_phone.setTextColor(getResources().getColor(R.color.blue_light));
                loadData(UserLogin.getInstance().getLatitude(StationListActivity.this), UserLogin.getInstance().getLongtitude(StationListActivity.this), "1", UserLogin.getInstance().getCityid(StationListActivity.this), binding.etStationName.getText().toString());
                customPopup.dismiss();
            });
        } else {
            customPopup.showAtAnchorView(binding.tvChoseStation, YGravity.BELOW, XGravity.CENTER, 0, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_CITY && resultCode == 1) {
            String cityName = data.getStringExtra(API.cityName);
            binding.tvCity.setText(cityName);
            UserLogin.getInstance().setCityName(cityName);
            SharePref.put(StationListActivity.this, API.cityName, cityName);
            double lat = data.getDoubleExtra(API.latitude, 0);
            UserLogin.getInstance().setLatitude(lat);
            SharePref.put(StationListActivity.this, API.latitude, lat + "");
            double lon = data.getDoubleExtra(API.longtitude, 0);
            UserLogin.getInstance().setLongtitude(lon);
            SharePref.put(StationListActivity.this, API.longtitude, lon + "");
            int cityId = data.getIntExtra(API.cityId, 0);
            UserLogin.getInstance().setCityid(cityId);
            SharePref.put(StationListActivity.this, API.cityId, cityId + "");
            binding.tvChoseStation.setText("全部");
            binding.etStationName.setText("");
            loadData(UserLogin.getInstance().getLatitude(StationListActivity.this), UserLogin.getInstance().getLongtitude(StationListActivity.this), "-1", UserLogin.getInstance().getCityid(StationListActivity.this), binding.etStationName.getText().toString());
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        Intent intents = new Intent();
        setResult(2, intents);
        finish();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<StationInfo> lists;

        public MyAdapter(List<StationInfo> list) {
            this.lists = list;
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
            StationInfo station = lists.get(position);
            if (station.isListclick()) {
                holder.getBinding().re.setBackground(getResources().getDrawable(R.drawable.station_item1));
            } else {
                holder.getBinding().re.setBackground(getResources().getDrawable(R.drawable.station_item2));
            }
            boolean worktime = false;
            String startTime = DateUtils.timeTotime(station.getStart_time());
            String endTime = DateUtils.timeTotime(station.getEnd_time());
            worktime = DateUtils.isBelong(startTime, endTime);
            boolean finalWorktime = worktime;
            holder.getBinding().re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < lists.size(); i++) {
                        lists.get(i).setListclick(false);
                    }
                    station.setListclick(true);
                    adapter.notifyDataSetChanged();
                    if (station.isAppointment()) {
                        if (finalWorktime && station.getBattery() > 0 && station.getStatus() != 2) {//在运营时间内，且电池数量》0,并且不再维修状态
                            binding.yuyue.setVisibility(View.VISIBLE);
                        } else {
                            binding.yuyue.setVisibility(View.GONE);
                        }
                    } else {
                        binding.yuyue.setVisibility(View.GONE);
                    }
                    stationId = station.getId();
                }
            });
            int mipmap = 0, lefticon = 0;
            if (station.getStatus() == 2) {
                mipmap = R.mipmap.sign_repair;
                if (station.getType() == 0) {
                    lefticon = R.mipmap.station_repair;
                } else {
                    lefticon = R.mipmap.bao_nopower;
                }
            } else {
                if (station.getBattery() > 0 && worktime) {
                    if (station.getType() == 0) {//0是换电站
                        mipmap = R.mipmap.sign_working;
                        lefticon = R.mipmap.station_icon;
                    } else {
                        if (station.isAppointment()) {
                            mipmap = R.mipmap.sign_reserve;
                        } else {
                            mipmap = R.mipmap.sign_working;
                        }
                        lefticon = R.mipmap.bao_working;
                    }
                } else if (station.getBattery() > 0 && !worktime) {//休息中
                    mipmap = R.mipmap.sign_rest;
                    if (station.getType() == 0) {//0是换电站
                        lefticon = R.mipmap.station_rest;
                    } else {
                        lefticon = R.mipmap.bao_rest;
                    }
                } else if (worktime && station.getBattery() == 0) {//无电池
                    if (station.getType() == 0) {//0是换电站
                        lefticon = R.mipmap.station_icon;
                        mipmap = R.mipmap.sign_working;
                    } else {
                        lefticon = R.mipmap.bao_nopower;
                        mipmap = R.mipmap.sign_nopower;
                    }
                } else {
                    if (station.getType() == 0) {//0是换电站
                        lefticon = R.mipmap.station_icon;
                        mipmap = R.mipmap.sign_working;
                    } else {
                        lefticon = R.mipmap.bao_nopower;
                        mipmap = R.mipmap.sign_nopower;
                    }
                }
            }
            Drawable rightDrawable = getResources().getDrawable(mipmap);
            Drawable leftDrawable = getResources().getDrawable(lefticon);
            rightDrawable.setBounds(0, 0, 100, 50);//36 17
            leftDrawable.setBounds(0, 0, 80, 80);
            holder.getBinding().tvName.setCompoundDrawables(leftDrawable, null, rightDrawable, null);
            Drawable distancedrawable = getResources().getDrawable(R.mipmap.station_record_guide);
            distancedrawable.setBounds(0, 0, 60, 60);
            holder.getBinding().tvDistance.setCompoundDrawables(null, distancedrawable, null, null);
            holder.getBinding().tvName.setText(station.getName());
            holder.getBinding().tvAddress.setText(station.getAddress());
            holder.getBinding().tvPhone.setText(station.getPhone());
            holder.getBinding().tvPhone.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + station.getPhone());
                intent.setData(data);
                startActivity(intent);
            });
            holder.getBinding().tvClock.setText(DateUtils.datetoTime(station.getStart_time()) + "~" + DateUtils.datetoTime(station.getEnd_time()));
            if (station.getType() == 0) {
                holder.getBinding().tvPaidui.setText("排队人数：" + station.getWaiting());
                holder.getBinding().tvKucun.setVisibility(View.VISIBLE);
            } else if (station.getType() == 1) {
                holder.getBinding().tvPaidui.setText("可用电池：" + station.getBattery());
                holder.getBinding().tvKucun.setVisibility(View.INVISIBLE);
            }
            holder.getBinding().tvKucun.setText(station.getBattery() > 0 ? "电池库存:有" : "电池库存:无");
            float distance = AMapUtils.calculateLineDistance(new LatLng(UserLogin.getInstance().getMylatitude(StationListActivity.this), UserLogin.getInstance().getMylongtitude(StationListActivity.this)), new LatLng(station.getLatitude(), station.getLongitude()));
            DecimalFormat df = new DecimalFormat("#.00");
            if (distance < 1000) {
                holder.getBinding().tvDistance.setText("导航\n" + df.format(distance) + "m");
            } else {
                holder.getBinding().tvDistance.setText("导航\n" + df.format(distance / 1000) + "km");
            }
            holder.getBinding().tvDistance.setOnClickListener(view ->
            {
                if (UserLogin.getInstance().getYuyueId() == -1 && station.getType() == 1) {
                    Utils.snackbar(StationListActivity.this, "请预约成功后前往换电");
                } else {
                    Poi start = new Poi("", new com.amap.api.maps.model.LatLng(UserLogin.getInstance().getMylatitude(StationListActivity.this), UserLogin.getInstance().getMylongtitude(StationListActivity.this)), "");
                    /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
                    /**Poi支持传入经纬度和PoiID，PoiiD优先级更高，使用Poiid算路，导航终点会更合理**/
                    Poi end = new Poi("", new com.amap.api.maps.model.LatLng(station.getLatitude(), station.getLongitude()), "");
                    AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI), null, MapCustomActivity.class);
                }
            });
        }

        @Override
        public int getItemCount() {
            return lists.size();
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
}
