package com.botann.driverclient.ui.activity;
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
import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.model.Bean.JsonBean;
import com.botann.driverclient.model.User;
import com.botann.driverclient.model.chongdian.ChongdianInfo;
import com.botann.driverclient.model.huandian.StationInfo;
import com.botann.driverclient.model.loginmodel.CityNametoIdModel;
import com.botann.driverclient.model.huandian.StationlistModel;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.utils.Constants;
import com.botann.driverclient.utils.GetJsonDataUtil;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.SharePref;
import com.botann.driverclient.utils.Utils;
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
    // ?????? Marker ??????????????????
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker ?????????????????????????????????
        // ?????? true ?????????????????????????????????????????????false
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
//                    builder.setMessage("????????????????????????????????????????????????????????????");
//                    builder.setNeutralButton("??????", new DialogInterface.OnClickListener() {
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
        //???activity??????onCreate?????????mMapView.onCreate(savedInstanceState)???????????????
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        refreshMap(Double.longBitsToDouble(((long) SharePref.get(GaoDeMapActivity.this, API.lat, 0l))), Double.longBitsToDouble(((long) SharePref.get(GaoDeMapActivity.this, API.lon, 0l))));
        aMap.setOnMarkerClickListener(markerClickListener);
        bluePointLocat(); //????????????
        markerCreate();  //?????????marker??????
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
            if (stationInfo.getStatus().equals("?????????")) {
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
        myLocationStyle = new MyLocationStyle();//??????????????????????????????myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????1???1???????????????????????????myLocationType????????????????????????????????????
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(1000); //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        aMap.setMyLocationStyle(myLocationStyle);//?????????????????????Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);?????????????????????????????????????????????????????????
        aMap.setMyLocationEnabled(true);// ?????????true?????????????????????????????????false??????????????????????????????????????????????????????false???
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //???activity??????onDestroy?????????mMapView.onDestroy()???????????????
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //???activity??????onResume?????????mMapView.onResume ()???????????????????????????
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //???activity??????onPause?????????mMapView.onPause ()????????????????????????
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //???activity??????onSaveInstanceState?????????mMapView.onSaveInstanceState (outState)??????????????????????????????
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_showmap:
                setResult(1001, null);
                finish();
                break;
            case R.id.tv_cityname: //????????????
                showPickerView();
                break;
        }
    }

    //????????????
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
                .setTitleText("????????????")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //???????????????????????????
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(option1Items, option2Items);//???????????????
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

    //???????????????window
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
                /**?????????????????????????????????,??????POI???ID "B000A83M61"????????????????????????????????????????????????????????????????????????**/
                /**Poi????????????????????????PoiID???PoiiD????????????????????????Poiid?????????????????????????????????**/
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
        if (model.getStatus().equals("?????????")) {
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
            stationBattery.setText("????????????:???");
        } else {
            stationBattery.setText("????????????:???");
        }
        stationLine.setText("????????????:" + model.getLineCount());
        mStationDialog.setContentView(root);
        Window dialogWindow = mStationDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = -20;
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // ??????
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        mStationDialog.setCanceledOnTouchOutside(true);
        mStationDialog.show();
    }

    //???????????????window
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
                /**?????????????????????????????????,??????POI???ID "B000A83M61"????????????????????????????????????????????????????????????????????????**/
                /**Poi????????????????????????PoiID???PoiiD????????????????????????Poiid?????????????????????????????????**/
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
        kuaizhuang.setText("??????(???/???):"+model.getQuickAvailConnector()+"/"+model.getQuickConnector());
        manzhuang.setText("??????(???/???):"+model.getSlowAvailConnector()+"/"+model.getSlowConnector());
        if (model.getSlowConnector()==0){
            manzhuang.setVisibility(View.GONE);
        }else {
            manzhuang.setVisibility(View.VISIBLE);
        }
        yunyingshang.setText(model.getStationCompany()+"");
//        tv_zhuang.setText("?????????(???/???):  "+model.getQuickAvailConnector()+"/"+model.getSlowAvailConnector());
//        chongdianzhuang_kuai.setText("?????????????????????: "+model.getQuickAvailConnector());
//        chongdianzhuang_man.setText("?????????????????????: "+model.getSlowAvailConnector());
        stationDistance.setText(df.format(AMapUtils.calculateLineDistance(start, end) / 1000) + "km");
        mChongdianDialog.setContentView(root);
        Window dialogWindow = mChongdianDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = -20;
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // ??????
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
        for (int i = 0; i < jsonBean.size(); i++) {//????????????
            ArrayList<String> CityList = new ArrayList<>();//????????????????????????????????????
            ArrayList<Double> LatList = new ArrayList<>();//????????????????????????????????????
            ArrayList<Double> LonList = new ArrayList<>();//????????????????????????????????????
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//??????????????????????????????????????????
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//??????????????????????????????
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                Double Lat = jsonBean.get(i).getCityList().get(c).getLat();
                Double Lon = jsonBean.get(i).getCityList().get(c).getLon();
                CityList.add(CityName);//????????????
                LatList.add(Lat);//??????????????????
                LonList.add(Lon);//??????????????????
                ArrayList<String> City_AreaList = new ArrayList<>();//??????????????????????????????
                //??????????????????????????????????????????????????????????????????null ?????????????????????????????????????????????
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//?????????????????????????????????
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//?????????????????????????????????
                    }
                }
                Province_AreaList.add(City_AreaList);//??????????????????????????????
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
                //Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //???????????????????????????
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
        //??????cookie?????????????????????sharepreferences
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
                    markerCreate();  //?????????marker??????
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
