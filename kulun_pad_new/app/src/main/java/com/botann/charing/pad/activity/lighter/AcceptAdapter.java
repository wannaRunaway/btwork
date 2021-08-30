package com.botann.charing.pad.activity.lighter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.botann.charging.pad.R;
import com.botann.charing.pad.model.batterylighter.BatteryLighter;
import com.botann.charing.pad.utils.DateUtils;

import java.util.List;

/**
 * created by xuedi on 2018/12/3
 */
public class AcceptAdapter extends RecyclerView.Adapter<AcceptAdapter.AcceptViewHolder> {
    private Context context;
    private List<BatteryLighter> list;
    private AcceptClickInter clickInter;
    public AcceptAdapter(Context context, List<BatteryLighter> list, AcceptClickInter clickInter) {
        this.context = context;
        this.list = list;
        this.clickInter = clickInter;
    }

    @Override
    public AcceptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_accept, null);
        AcceptViewHolder holder = new AcceptViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AcceptViewHolder holder, int position) {
        final BatteryLighter batteryLighter = list.get(position);
        holder.transferStation.setText("移交站点:"+batteryLighter.getTransferSiteName());
        holder.transferTime.setText("移交时间"+DateUtils.stampToDate(batteryLighter.getTransferTime()));
        holder.status.setText("状态:"+(batteryLighter.getStatus()==1?"已接收":"待接收"));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickInter.Click(batteryLighter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AcceptViewHolder extends RecyclerView.ViewHolder {
        private TextView transferStation, transferTime, status;
        private RelativeLayout relativeLayout;
        public AcceptViewHolder(View itemView) {
            super(itemView);
            transferStation = (TextView) itemView.findViewById(R.id.tv_transfer_station);
            transferTime = (TextView) itemView.findViewById(R.id.tv_transfer_time);
            status = (TextView) itemView.findViewById(R.id.tv_status);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.re_accept);
        }
    }
}
