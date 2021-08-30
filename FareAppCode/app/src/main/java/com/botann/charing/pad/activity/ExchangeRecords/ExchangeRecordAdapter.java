package com.botann.charing.pad.activity.ExchangeRecords;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import com.botann.charging.pad.R;
import com.botann.charing.pad.model.ExchangeRecord;
import com.botann.charing.pad.utils.DateUtil;

/**
 * Created by mengchenyun on 2017/1/18.
 */

public class ExchangeRecordAdapter extends RecyclerView.Adapter<ExchangeRecordAdapter.ViewHolder> {


    public ArrayList<ExchangeRecord> datas = null;

    public ExchangeRecordAdapter(ArrayList<ExchangeRecord> datas) {
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.changebattery_item, parent, false);
        return new ExchangeRecordAdapter.ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ExchangeRecordAdapter.ViewHolder holder, int position) {

        ExchangeRecord exchangeRecord = datas.get(position);
        holder.tvTime.setText(getTime(exchangeRecord.getCreateDate()));
        holder.tvCarNo.setText(exchangeRecord.getCarNumber());
        holder.tvCarSize.setText(exchangeRecord.getCarType());
        holder.tvTotalMiles.setText(exchangeRecord.getReferMiles());
        holder.tvCostMiles.setText(exchangeRecord.getTravelMile());
        holder.tvFee.setText(exchangeRecord.getFare());
        holder.tvCoupon.setText(exchangeRecord.getCoupon());
        holder.tvRealPay.setText(exchangeRecord.getRealFare());
        holder.tvBalance.setText(exchangeRecord.getBalance());

    }

    private String getTime(long createTime) {
        String date = DateUtil.getAppointDate(createTime, "yyyy-MM-dd HH:mm:ss");
        if (date == null && date.equals("")) {
            return "";
        }
        String[] datesplite = date.split("\\s+");
        return datesplite[0] + "\n" + datesplite[1];
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvCarNo;
        TextView tvCarSize;
        TextView tvCostMiles;
        TextView tvFee;
        TextView tvTotalMiles;
        TextView tvBalance;
        TextView tvCoupon;
        TextView tvRealPay;

        public ViewHolder(View convertView) {
            super(convertView);

            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvCarNo = (TextView) convertView.findViewById(R.id.tv_car_no);
            tvCarSize = (TextView) convertView.findViewById(R.id.tv_carsize);
            tvCostMiles = (TextView) convertView.findViewById(R.id.tv_cost_miles);
            tvFee = (TextView) convertView.findViewById(R.id.tv_fee);
            tvTotalMiles = (TextView) convertView.findViewById(R.id.tv_total_miles);
            tvBalance = (TextView) convertView.findViewById(R.id.tv_balance);
            tvCoupon = (TextView) convertView.findViewById(R.id.tv_coupon);
            tvRealPay = (TextView) convertView.findViewById(R.id.tv_real_pay);
        }

    }

}
