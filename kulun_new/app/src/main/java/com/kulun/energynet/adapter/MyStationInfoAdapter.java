package com.kulun.energynet.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.kulun.energynet.model.huandian.StationInfo;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;
/**
 * Created by Orion on 2017/7/26.
 */
public class MyStationInfoAdapter extends RecyclerView.Adapter<MyStationInfoAdapter.MyViewHolder> {
    private DecimalFormat df = new DecimalFormat("######0.00");
    private Context mContext;
    private List<StationInfo> list;
    private INaviInfoCallback callback;
    public MyStationInfoAdapter(Context context, List<StationInfo> list, INaviInfoCallback callback) {
        mContext = context;
        this.list = list;
        this.callback = callback;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.station_list_item, parent, false);
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
        TextView mStationStatus;
        TextView mStationAddr;
        TextView mStationDist;
        TextView mStationPhone;
        TextView mStationTime;
        TextView mBatteryCount;
        TextView mLineCount;
        Button btnNavigation;

        public MyViewHolder(View itemView) {
            super(itemView);
            mStationName = (TextView) itemView.findViewById(R.id.station_name);
            mStationStatus = (TextView) itemView.findViewById(R.id.station_status);
            mStationAddr = (TextView) itemView.findViewById(R.id.station_address);
            mStationDist = (TextView) itemView.findViewById(R.id.station_distance);
            mStationPhone = (TextView) itemView.findViewById(R.id.station_phone);
            mStationTime = (TextView) itemView.findViewById(R.id.station_time);
            mBatteryCount = (TextView) itemView.findViewById(R.id.battery_count);
            mLineCount = (TextView) itemView.findViewById(R.id.line_count);
            btnNavigation = (Button) itemView.findViewById(R.id.btn_navigation);
        }

        public void bind(final StationInfo content) {
            LatLng start = new LatLng(Double.longBitsToDouble(((long) SharePref.get(mContext, API.lat, 0l))), Double.longBitsToDouble(((long) SharePref.get(mContext, API.lon, 0l))));
            LatLng end = new LatLng(content.getLatitude(), content.getLongitude());
            mStationName.setText(content.getStationName());
            mStationStatus.setText(content.getStatus());
//            float textLength = (content.getStationName() == null ? 0.0f : Utils.getTextViewLength(mStationName, content.getStationName()));
//            int length = ((int) textLength) + 6;
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStationStatus.getLayoutParams();
//            params.setMargins(length, 6, 0, 6);
//            mStationStatus.setLayoutParams(params);
            if (content.getStatus().equals("运营中")) {
                switch (content.getLineLevel()) {
                    case 1:
                        mStationStatus.setBackgroundResource(R.drawable.station_status_on_bg);
                        break;
                    case 2:
                        mStationStatus.setBackgroundResource(R.drawable.station_status_yellow_bg);
                        break;
                    case 3:
                        mStationStatus.setBackgroundResource(R.drawable.station_status_crimson_bg);
                        break;
                    case 4:
                        mStationStatus.setBackgroundResource(R.drawable.station_status_purple_bg);
                        break;
                    default:
                        break;
                }
            } else {
                mStationStatus.setBackgroundResource(R.drawable.station_status_off_bg);
            }
            mStationAddr.setText(content.getAddress());
            mStationDist.setText(df.format(AMapUtils.calculateLineDistance(start, end) / 1000) + "km");
            mStationPhone.setText(content.getPhone());
            mStationTime.setText(content.getBeginTime().substring(0, 5) + "-" + content.getEndTime().substring(0, 5));
            if (content.getMonitorStatus() != 1) {
                mBatteryCount.setVisibility(View.INVISIBLE);
            } else {
                mBatteryCount.setVisibility(View.VISIBLE);
                if (content.getBatteryCount() > 0) {
                    mBatteryCount.setText("电池库存:有");
                } else {
                    mBatteryCount.setText("电池库存:无");
                }
            }
            mLineCount.setText("排队人数:" + content.getLineCount());
            btnNavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Poi start = new Poi("", new com.amap.api.maps.model.LatLng(User.getInstance().getLatitude(), User.getInstance().getLongtitude()), "");
                    /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
                    /**Poi支持传入经纬度和PoiID，PoiiD优先级更高，使用Poiid算路，导航终点会更合理**/
                    Poi end = new Poi("", new com.amap.api.maps.model.LatLng(content.getLatitude(), content.getLongitude()), "");
                    AmapNaviPage.getInstance().showRouteActivity(mContext, new AmapNaviParams(null, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI), callback);
//                    Utils.log(null,null,content.getLatitude()+"=="+content.getLongitude()+"=="+User.getInstance().getLatitude()+"=="+User.getInstance().getLongtitude());
                }
            });
        }
    }
}
