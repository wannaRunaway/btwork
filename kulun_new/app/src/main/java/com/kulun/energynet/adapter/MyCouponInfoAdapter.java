package com.kulun.energynet.adapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kulun.energynet.R;
import com.kulun.energynet.model.CouponInfo;
import com.kulun.energynet.model.CouponUserModel;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.fragment.StationSelectFragment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Orion on 2017/8/3.
 */
public class MyCouponInfoAdapter extends RecyclerView.Adapter<MyCouponInfoAdapter.MyViewHolder> {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    private static DecimalFormat df = new DecimalFormat("######0.00");
    private Context mContext;
    private StationSelectFragment stationSelectFragment;
    private List<CouponUserModel.ContentBean.DataBean> list;
    private boolean ischongdian;
    private FragmentActivity fragmentActivity;
    public MyCouponInfoAdapter(Context context, List<CouponUserModel.ContentBean.DataBean> list, FragmentActivity fragmentActivity, boolean ischongdian) {
        mContext = context;
        this.list = list;
        this.fragmentActivity = fragmentActivity;
        this.ischongdian = ischongdian;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.coupon_list_item, parent, false);
        return new MyCouponInfoAdapter.MyViewHolder(v);
    }

    //type 》= 50 充电优惠券
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CouponUserModel.ContentBean.DataBean dataBean =  list.get(position);
        holder.mFare.setText(df.format(dataBean.getAmount())+"元");
        holder.mTitle.setText(dataBean.getCouponName());
        holder.mLeft.setText("剩余" + df.format((dataBean.getAmount().doubleValue() - dataBean.getUsed().doubleValue())) + "元未使用");
        holder.mTime.setText(dataBean.getExpireDate() == null ? "永久有效" : format.format(dataBean.getBeginDate()) + "至" + format.format(dataBean.getExpireDate()));
        holder.lianjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stationSelectFragment = new StationSelectFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) dataBean);
                stationSelectFragment.setArguments(bundle);
                stationSelectFragment.show(fragmentActivity.getSupportFragmentManager(), "data");
            }
        });
        if (dataBean.getType()>=50){
            holder.name.setText("充电优惠券");
            holder.lianjie.setVisibility(View.VISIBLE);
        }else {
            holder.name.setText("换电优惠券");
            holder.lianjie.setVisibility(View.GONE);
        }
//        if (content.getAmount().doubleValue() - content.getUsed().doubleValue() <= 0) {
//            mCoupon.setBackgroundColor(Color.parseColor("#FFA500"));
//            mUsed.setVisibility(View.INVISIBLE);
//        } else {
//            mCoupon.setBackgroundColor(Color.parseColor("#FFA500"));
//            mUsed.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mFare;
        TextView mTitle;
        TextView mLeft;
        TextView mTime,lianjie,name;
        public MyViewHolder(View itemView) {
            super(itemView);
            mFare = (TextView) itemView.findViewById(R.id.coupon_fare);
            mTitle = (TextView) itemView.findViewById(R.id.coupon_title);
            mLeft = (TextView) itemView.findViewById(R.id.left_amount);
            mTime = (TextView) itemView.findViewById(R.id.coupon_time);
            lianjie = (TextView) itemView.findViewById(R.id.lianjie);
            name = itemView.findViewById(R.id.name);
        }
    }
}
