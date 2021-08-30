package com.botann.charing.pad.activity.malfunctionrecord;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.botann.charging.pad.R;
import com.botann.charing.pad.model.repairlist.RepairData;
import com.botann.charing.pad.utils.DateUtils;

import java.util.List;

/**
 * created by xuedi on 2018/11/1
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {
    private Context context;
    private List<RepairData> list;
    private RecordInter inter;

    public RecordAdapter(List<RepairData> list, Context context, RecordInter inter) {
        this.context = context;
        this.list = list;
        this.inter = inter;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_malrecord, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        int resId = 0;
        final RepairData repairData = list.get(position);
        //1 已报修   2已维修   3已完成\已修复     4未修复
        String status = "";
        switch (repairData.getStatus()) {
            case 1:
                status = "已报修";
                resId = R.drawable.station_status_crimson_bg;
                break;
            case 4:
                status = "未修复";
                resId = R.drawable.station_status_crimson_bg;
                break;
            case 2:
                status = "已维修";
                resId = R.drawable.station_status_yellow_bg;
                break;
            case 3:
                status = "已修复";
                resId = R.drawable.station_status_green;
                break;
            default:
                break;
        }
        holder.re.setBackgroundResource(resId);
        holder.re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inter.recordClick(repairData);
            }
        });
        holder.tv_upperson.setText("上报人:"+repairData.getFaultReportPersion());
        holder.tv_status.setText("状态:"+status);
        holder.tv_malcontent.setText("故障内容:"+repairData.getFaultPhenomenon());
        holder.tv_date.setText("提交时间:"+DateUtils.stampToDate(repairData.getSendRepairTime()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_upperson, tv_status, tv_malcontent, tv_date;
        private RelativeLayout re;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_upperson = (TextView) itemView.findViewById(R.id.tv_upperson);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_malcontent = (TextView) itemView.findViewById(R.id.tv_malcontent);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            re = (RelativeLayout) itemView.findViewById(R.id.re);
        }
    }
}
