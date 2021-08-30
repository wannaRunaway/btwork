package com.kulun.energynet.ui.activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.model.Poi;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.bigkoo.pickerview.OptionsPickerView;
import com.kulun.energynet.MainApp;
import com.kulun.energynet.R;
import com.kulun.energynet.model.Bean.JsonBean;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.chongdian.ChongdianInfo;
import com.kulun.energynet.model.huandian.StationInfo;
import com.kulun.energynet.model.loginmodel.CityNametoIdModel;
import com.kulun.energynet.model.huandian.StationlistModel;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.GetJsonDataUtil;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONArray;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * created by xuedi on 2019/4/24
 */
public class GaoDeMapActivity extends AppCompatActivity implements View.OnClickListener, INaviInfoCallback {
    private TextView tv_cityname, tv_showlist;
    private MapView mMapView;
    private AMap aMap;
    private List<StationInfo> huandianList;
    private List<ChongdianInfo> chongdianList;
    private static BitmapDescriptor bitmap = null;
    private Dialog mStationDialog, mChongdianDialog;
    private DecimalFormat df = new DecimalFormat("######0.00");
    private ArrayList<ArrayList<String>> option2Items = new ArrayList<>();
    private ArrayList<JsonBean> option1Items = new ArrayList<>();
    private ArrayList<ArrayList<Double>> Lat2Items = new ArrayList<>();
    private ArrayList<ArrayList<Double>> Lon2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> option3Items = new ArrayList<>();
    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            Object object = marker.getObject();
            if (marker.getObject() instanceof StationInfo) {
                showPopDialog((StationInfo) object);
            } else {
//                SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
//                if (preferences.getBoolean("hasChargeAccount", false)){
                    showChongdianPopDialog((ChongdianInfo) object);
//                }else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(GaoDeMapActivity.this);
//                    builder.setMessage("您无充电服务权限，请联系客服开通充电服务");
//                    builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//                    builder.show();
//                }
            }
            return true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaode_map);
        huandianList = (List<StationInfo>) getIntent().getSerializableExtra("huandianlist");
        chongdianList = (List<ChongdianInfo>) getIntent().getSerializableExtra("chongdianlist");
        tv_cityname = findViewById(R.id.tv_cityname);
        tv_showlist = findViewById(R.id.tv_showmap);
        mMapView = findViewById(R.id.mapview);
        tv_showlist.setOnClickListener(this);
        tv_cityname.setOnClickListener(this);
//        tv_cityname.setText(Constants.cityname);
        tv_cityname.setText((String) SharePref.get(this, API.city, ""));
//        tv_cityname.setText(User.getInstance().getCityName());
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        refreshMap(Double.longBitsToDouble(((long) SharePref.get(GaoDeMapActivity.this, API.lat, 0l))), Double.longBitsToDouble(((long) SharePref.get(GaoDeMapActivity.this, API.lon, 0l))));
        aMap.setOnMarkerClickListener(markerClickListener);
        bluePointLocat(); //定位蓝点
        markerCreate();  //换电站marker显示
        new Thread(new Runnable() {
            @Override
            public void run() {
                initJsonData();
            }
        }).start();
    }

    private void markerCreate() {
        for (int i = 0; i < huandianList.size(); i++) {
            StationInfo stationInfo = huandianList.get(i);
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(stationInfo.getLatitude(), stationInfo.getLongitude());
            markerOptions.position(latLng);
            if (stationInfo.getStatus().equals("运营中")) {
                switch (stationInfo.getLineLevel()) {
                    case 1:
                        bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.drawable.huandian_open), 85, 123));
                        break;
                    case 2:
                        bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.drawable.huandian_1), 85, 123));
                        break;
                    case 3:
                        bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.drawable.huandian_2), 85, 123));
                        break;
                    case 4:
                        bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.drawable.huandian_3), 85, 123));
                        break;
                    default:
                        break;
                }
            } else {
                bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.drawable.huandian_guanbi), 85, 123));
            }
            markerOptions.icon(bitmap);
            Marker marker = aMap.addMarker(markerOptions);
            marker.setObject(stationInfo);
        }
        for (int i = 0; i < chongdianList.size(); i++) {
            ChongdianInfo chongdianInfo = chongdianList.get(i);
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(chongdianInfo.getStationLat(), chongdianInfo.getStationLng());
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.drawable.chongdian), 85, 123)));
            Marker marker = aMap.addMarker(markerOptions);
            marker.setObject(chongdianInfo);
        }
    }

    private void bluePointLocat() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_showmap:
                setResult(1001, null);
                finish();
                break;
            case R.id.tv_cityname: //城市名称
                showPickerView();
                break;
        }
    }

    //切换城市
    private void showPickerView() {
        final OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                final String city = option2Items.get(options1).get(options2);
                tv_cityname.setText(city);
//                Constants.cityname = city;
//                User.getInstance().setCityName(city);
                SharePref.put(GaoDeMapActivity.this, API.city, city);
                refreshUI();
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(option1Items, option2Items);//二级选择器
        pvOptions.show();
    }

    public Bitmap setImgSize(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    //显示换电的window
    private void showPopDialog(final StationInfo model) {
        mStationDialog = new Dialog(this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.station_map, null);
        TextView stationName = (TextView) root.findViewById(R.id.station_name);
        TextView stationStatus = (TextView) root.findViewById(R.id.station_status);
        TextView stationAddress = (TextView) root.findViewById(R.id.station_address);
        TextView stationPhone = (TextView) root.findViewById(R.id.station_phone);
        TextView stationDistance = (TextView) root.findViewById(R.id.station_distance);
        TextView stationTime = (TextView) root.findViewById(R.id.station_time);
        TextView stationBattery = (TextView) root.findViewById(R.id.battery_count);
        TextView stationLine = (TextView) root.findViewById(R.id.line_count);
        root.findViewById(R.id.btn_navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStationDialog.dismiss();
//                Poi start = new Poi("", new com.amap.api.maps.model.LatLng(User.getInstance().getLatitude(), User.getInstance().getLongtitude()), "");
                /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
                /**Poi支持传入经纬度和PoiID，PoiiD优先级更高，使用Poiid算路，导航终点会更合理**/
                Poi end = new Poi("", new com.amap.api.maps.model.LatLng(model.getLatitude(), model.getLongitude()), "");
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI), GaoDeMapActivity.this);
            }
        });
        LatLng start = new LatLng(Double.longBitsToDouble(((long) SharePref.get(GaoDeMapActivity.this, API.lat, 0l))), Double.longBitsToDouble(((long) SharePref.get(GaoDeMapActivity.this, API.lon, 0l))));
        LatLng end = new LatLng(model.getLatitude(), model.getLongitude());
        stationName.setText(model.getStationName());
        stationStatus.setText(model.getStatus());
        float textLength = Utils.getTextViewLength(stationName, model.getStationName());
        int length = ((int) textLength) + 6;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) stationStatus.getLayoutParams();
        params.setMargins(length, 6, 0, 6);
        stationStatus.setLayoutParams(params);
        if (model.getStatus().equals("运营中")) {
            switch (model.getLineLevel()) {
                case 1:
                    stationStatus.setBackgroundResource(R.drawable.station_status_on_bg);
                    break;
                case 2:
                    stationStatus.setBackgroundResource(R.drawable.station_status_yellow_bg);
                    break;
                case 3:
                    stationStatus.setBackgroundResource(R.drawable.station_status_crimson_bg);
                    break;
                case 4:
                    stationStatus.setBackgroundResource(R.drawable.station_status_purple_bg);
                    break;
                default:
                    break;
            }
        } else {
            stationStatus.setBackgroundResource(R.drawable.station_status_off_bg);
        }
        stationAddress.setText(model.getAddress());
        stationDistance.setText(df.format(AMapUtils.calculateLineDistance(start, end) / 1000) + "km");
        stationPhone.setText(model.getPhone());
        stationTime.setText(model.getBeginTime().substring(0, 5) + "-" + model.getEndTime().substring(0, 5));
        if (model.getBatteryCount() > 0) {
            stationBattery.setText("电池库存:有");
        } else {
            stationBattery.setText("电池库存:无");
        }
        stationLine.setText("排队人数:" + model.getLineCount());
        mStationDialog.setContentView(root);
        Window dialogWindow = mStationDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = -20;
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        mStationDialog.setCanceledOnTouchOutside(true);
        mStationDialog.show();
    }

    //显示充电的window
    private void showChongdianPopDialog(final ChongdianInfo model) {
        mChongdianDialog = new Dialog(this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_chongdian, null);
        TextView stationName = (TextView) root.findViewById(R.id.station_name);
        TextView stationAddress = (TextView) root.findViewById(R.id.station_address);
        TextView parking = (TextView) root.findViewById(R.id.parking);
        TextView stationDistance = (TextView) root.findViewById(R.id.station_distance);
        TextView stationTime = (TextView) root.findViewById(R.id.station_time);
//        TextView tv_zhuang = root.findViewById(R.id.tv_zhuang);
        TextView yunyingshang = root.findViewById(R.id.yunyingshang);
        TextView kuaizhuang = root.findViewById(R.id.tv_zhuang1);
        TextView manzhuang = root.findViewById(R.id.tv_zhuang2);
        TextView tv_rmb = root.findViewById(R.id.tv_rmb);
        TextView tv_price = root.findViewById(R.id.tv_price);
        root.findViewById(R.id.btn_navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChongdianDialog.dismiss();
//                Poi start = new Poi("", new com.amap.api.maps.model.LatLng(User.getInstance().getLatitude(), User.getInstance().getLongtitude()), "");
                /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
                /**Poi支持传入经纬度和PoiID，PoiiD优先级更高，使用Poiid算路，导航终点会更合理**/
                Poi end = new Poi("", new com.amap.api.maps.model.LatLng(model.getStationLat(), model.getStationLng()), "");
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI), GaoDeMapActivity.this);
            }
        });
        LatLng start = new LatLng(Double.longBitsToDouble(((long) SharePref.get(GaoDeMapActivity.this, API.lat, 0l))), Double.longBitsToDouble(((long) SharePref.get(GaoDeMapActivity.this, API.lon, 0l))));
        LatLng end = new LatLng(model.getStationLat(), model.getStationLng());
        parking.setText(model.getParkFee()+"");
        stationTime.setText(model.getBusineHours()+"");
        stationName.setText(model.getStationName()+"");
        stationAddress.setText(model.getAddress()+"");
//        tv_rmb.setText(model.getElectricityFee()+"");
        if (model.getDiscountElectricityFee().isEmpty()){
            tv_rmb.setText(model.getElectricityFee()+"");
            tv_price.setVisibility(View.GONE);
        }else {
            tv_rmb.setText(model.getDiscountElectricityFee()+"");
            tv_price.setText(model.getElectricityFee()+"");
            tv_price.setVisibility(View.VISIBLE);
            tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG );
        }
        kuaizhuang.setText("快充(空/总):"+model.getQuickAvailConnector()+"/"+model.getQuickConnector());
        manzhuang.setText("慢充(空/总):"+model.getSlowAvailConnector()+"/"+model.getSlowConnector());
        if (model.getSlowConnector()==0){
            manzhuang.setVisibility(View.GONE);
        }else {
            manzhuang.setVisibility(View.VISIBLE);
        }
        yunyingshang.setText(model.getStationCompany()+"");
//        tv_zhuang.setText("可用桩(快/慢):  "+model.getQuickAvailConnector()+"/"+model.getSlowAvailConnector());
//        chongdianzhuang_kuai.setText("快充空闲桩数量: "+model.getQuickAvailConnector());
//        chongdianzhuang_man.setText("慢充空闲桩数量: "+model.getSlowAvailConnector());
        stationDistance.setText(df.format(AMapUtils.calculateLineDistance(start, end) / 1000) + "km");
        mChongdianDialog.setContentView(root);
        Window dialogWindow = mChongdianDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = -20;
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        mChongdianDialog.setCanceledOnTouchOutside(true);
        mChongdianDialog.show();
    }

    private void initJsonData() {
        String JsonData = new GetJsonDataUtil().getJson(this, "city.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);
        option1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<Double> LatList = new ArrayList<>();//该省的城市列表的维度列表
            ArrayList<Double> LonList = new ArrayList<>();//该省的城市列表的经度列表
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                Double Lat = jsonBean.get(i).getCityList().get(c).getLat();
                Double Lon = jsonBean.get(i).getCityList().get(c).getLon();
                CityList.add(CityName);//添加城市
                LatList.add(Lat);//添加城市纬度
                LonList.add(Lon);//添加城市经度
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            option2Items.add(CityList);
            Lat2Items.add(LatList);
            Lon2Items.add(LonList);
            option3Items.add(Province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    private void refreshUI() {
        final String url = API.BASE_URL + API.URL_CITYID;
        final RequestParams params = new RequestParams();
//        params.add("name", Constants.cityname);
//        params.add("name", User.getInstance().getCityName());
        params.add("name", (String) SharePref.get(GaoDeMapActivity.this, API.city, ""));
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
                    SharePref.put(GaoDeMapActivity.this, API.cityId, model.getContent().getId());
                    double latdouble = Double.parseDouble(model.getContent().getLat());
                    double lngdouble = Double.parseDouble(model.getContent().getLng());
//                    User.getInstance().setLatitude(latdouble);
//                    User.getInstance().setLongtitude(lngdouble);
                    loadList();
                    refreshMap(latdouble, lngdouble);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                //Toast.makeText(mContext, "连接到服务器失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //地图显示刷新的位置
    private void refreshMap(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    private void loadList() {
        final String url = API.BASE_URL + API.URL_STATION_LIST;
        final RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        params.add("cityId", String.valueOf((int)SharePref.get(GaoDeMapActivity.this, API.cityId, 0)));
//        params.add("cityId", Constants.cityId);
        params.add("pageNo", "1");
        params.add("pageSize", "100");
        AsyncHttpClient client = new AsyncHttpClient();
        //保存cookie，自动保存到了sharepreferences
        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
        client.setCookieStore(myCookieStore);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                Utils.log(url, params, json);
                StationlistModel model = GsonUtils.getInstance().fromJson(json, StationlistModel.class);
                if (model.isSuccess()) {
                    huandianList.clear();
                    huandianList.addAll(model.getContent().getData());
                    markerCreate();  //换电站marker显示
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
            }
        });
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
