package com.botann.charing.pad.activity.ExchangeOrders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.botann.charging.pad.R;

import java.util.ArrayList;

import com.botann.charing.pad.callbacks.SGClickCellInsideCallBack;
import com.botann.charing.pad.model.ExchangeOrder;
import com.botann.charing.pad.utils.DateUtil;

/**
 * Created by liushanguo on 2017/8/21.
 */

public class ExchangeOrderAdapter extends RecyclerView.Adapter<ExchangeOrderAdapter.ViewHolder> {
    public ArrayList<ExchangeOrder> datas = null;
    public SGClickCellInsideCallBack callBack;
    public ExchangeOrderAdapter(ArrayList<ExchangeOrder> datas){
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_change_battery_order,parent,false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ExchangeOrder data = datas.get(position);
        holder.tvCarNo.setText(data.getPlateNumber());
        holder.tvDriver.setText(data.getDriverName());
        holder.tvTime.setText(DateUtil.getAppointDate(data.getCreateTime(),"HH:mm:ss"));
        holder.tvWaitTime.setText(data.getDiffSeconds());
        holder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.click(data);
            }
        });
        holder.btnChangePower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.changePower(data);
            }
        });
        holder.tvAccountBalance.setText(String.valueOf(data.getAccountBalance()));
    }

    @Override
    public int getItemCount() {
//        Log.i("数量：：：",datas.size() + "");
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCarNo;
        public TextView tvDriver;
        public TextView tvTime;
        public TextView tvWaitTime;
        public Button btnCancelOrder;
        public Button btnChangePower;
        public TextView tvAccountBalance;
        public ViewHolder(View convertView){
            super(convertView);
            tvCarNo = (TextView) convertView.findViewById(R.id.tv_car_no);
            tvDriver = (TextView) convertView.findViewById(R.id.tv_driver);
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvWaitTime = (TextView) convertView.findViewById(R.id.tv_wait_time);
            btnCancelOrder = (Button) convertView.findViewById(R.id.btn_cancel_order);
            btnChangePower = (Button) convertView.findViewById(R.id.btn_change_power);
            tvAccountBalance = (TextView) convertView.findViewById(R.id.tv_account_balance);
        }

    }
}
