package com.kulun.energynet.adapter;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.Poi;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.kulun.energynet.R;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.chongdian.ChongdianInfo;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;
import java.text.DecimalFormat;
import java.util.List;
/**
 * Created by Orion on 2017/7/26.
 */
public class ChongdianAdapter extends RecyclerView.Adapter<ChongdianAdapter.MyViewHolder> {
    private DecimalFormat df = new DecimalFormat("######0.00");
    private Context mContext;
    private List<ChongdianInfo> list;
    private INaviInfoCallback callback;
    public ChongdianAdapter(Context context, List<ChongdianInfo> list, INaviInfoCallback callback) {
        mContext = context;
        this.list = list;
        this.callback = callback;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_chongdian, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder holder1 = (MyViewHolder) holder;
        holder1.bind(list.get(position));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mStationName;
        TextView mStationAddr;
        TextView mStationDist;
        TextView mStationTime;
        Button btnNavigation;
        TextView tv_parking, tv_rmb, tv_zhuang1,tv_zhuang2, tv_price, yunyingshang;

        public MyViewHolder(View itemView) {
            super(itemView);
            mStationName = (TextView) itemView.findViewById(R.id.station_name);
            mStationAddr = (TextView) itemView.findViewById(R.id.station_address);
            mStationDist = (TextView) itemView.findViewById(R.id.station_distance);
            mStationTime = (TextView) itemView.findViewById(R.id.station_time);
            btnNavigation = (Button) itemView.findViewById(R.id.btn_navigation);
            tv_parking = (TextView) itemView.findViewById(R.id.parking);
            tv_rmb = itemView.findViewById(R.id.tv_rmb);
            tv_zhuang1 = itemView.findViewById(R.id.tv_zhuang1);
            tv_zhuang2 = itemView.findViewById(R.id.tv_zhuang2);
            tv_price = itemView.findViewById(R.id.tv_price);
            yunyingshang = itemView.findViewById(R.id.yunyingshang);
        }

        public void bind(final ChongdianInfo content) {
            if (content != null) {
//                LatLng start = new LatLng(Constants.mylatitude, Constants.mylongitude);
                LatLng start = new LatLng(Double.longBitsToDouble(((long) SharePref.get(mContext, API.lat, 0l))), Double.longBitsToDouble(((long) SharePref.get(mContext, API.lon, 0l))));
                LatLng end = new LatLng(content.getStationLat(), content.getStationLng());
                mStationName.setText(content.getStationName());
                tv_parking.setText(content.getParkFee()+"");
                mStationAddr.setText(content.getAddress()+"");
                mStationTime.setText(content.getBusineHours()+"");
                if (content.getDiscountElectricityFee().isEmpty()){
                    tv_rmb.setText(content.getElectricityFee()+"");
                    tv_price.setVisibility(View.GONE);
                }else {
                    tv_rmb.setText(content.getDiscountElectricityFee()+"");
                    tv_price.setText(content.getElectricityFee()+"");
                    tv_price.setVisibility(View.VISIBLE);
                    tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG );
                }
//                tv_rmb.setText(content.getElectricityFee()+"");
//                tv_zhuang.setText("可用桩(快/慢):  "+content.getQuickAvailConnector()+"/"+content.getSlowAvailConnector());
                tv_zhuang1.setText("快充(空/总):"+content.getQuickAvailConnector()+"/"+content.getQuickConnector());
                tv_zhuang2.setText("慢充(空/总):"+content.getSlowAvailConnector()+"/"+content.getSlowConnector());
                if (content.getSlowConnector()==0){
                    tv_zhuang2.setVisibility(View.GONE);
                }else {
                    tv_zhuang2.setVisibility(View.VISIBLE);
                }
                yunyingshang.setText(content.getStationCompany()+"");
                mStationDist.setText(df.format(AMapUtils.calculateLineDistance(start, end) / 1000) + "km");
//            mStationTime.setText(content.getBeginTime().substring(0, 5) + "-" + content.getEndTime().substring(0, 5));
                btnNavigation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Poi start = new Poi("", new com.amap.api.maps.model.LatLng(User.getInstance().getLatitude(), User.getInstance().getLongtitude()), "");
                        /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
                        /**Poi支持传入经纬度和PoiID，PoiiD优先级更高，使用Poiid算路，导航终点会更合理**/
                        Poi end = new Poi("", new com.amap.api.maps.model.LatLng(content.getStationLat(), content.getStationLng()), "");
                        AmapNaviPage.getInstance().showRouteActivity(mContext, new AmapNaviParams(null, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI), callback);
//                        Utils.log(null, null, content.getStationLat() + "==" + content.getStationLng() + "==" + User.getInstance().getLatitude()+ "=="+ User.getInstance().getLongtitude());
                    }
                });
            }
        }
    }
}

