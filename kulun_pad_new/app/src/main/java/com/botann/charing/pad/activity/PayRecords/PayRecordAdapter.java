package com.botann.charing.pad.activity.PayRecords;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.botann.charging.pad.R;
import com.botann.charing.pad.model.PayRecord;
import com.botann.charing.pad.utils.DateUtil;

/**
 * Created by mengchenyun on 2017/1/18.
 */

public class PayRecordAdapter  extends RecyclerView.Adapter<PayRecordAdapter.ViewHolder> {

    public ArrayList<PayRecord> datas = null;
    public PayRecordAdapter(ArrayList<PayRecord> datas) {
        this.datas = datas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pay_record_item,parent,false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PayRecord payRecord = datas.get(position);
        holder.tvPayTime.setText(DateUtil.getAppointDate(payRecord.getCreateTime(),"yyyy-MM-d HH:mm"));
        holder.tvAccount.setText(payRecord.getAccount());
        holder.tvDriverName.setText(payRecord.getName());
        holder.tvPayWay.setText(payRecord.getPayType()==1?"支付宝":"微信");
        holder.tvPayAmount.setText(payRecord.getChangeBalance());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPayTime;
        TextView tvAccount;
        TextView tvDriverName;
        TextView tvPayAmount;
        TextView tvPayWay;

        public ViewHolder(View convertView){
            super(convertView);
            tvPayTime = (TextView) convertView.findViewById(R.id.tv_pay_time);
            tvAccount = (TextView) convertView.findViewById(R.id.tv_account);
            tvDriverName = (TextView) convertView.findViewById(R.id.tv_driver_name);
            tvPayWay = (TextView) convertView.findViewById(R.id.tv_pay_way);
            tvPayAmount = (TextView) convertView.findViewById(R.id.tv_pay_amount);
        }

    }
}
