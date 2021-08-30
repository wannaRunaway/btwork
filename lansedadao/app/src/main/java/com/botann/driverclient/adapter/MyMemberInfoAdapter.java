package com.botann.driverclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.R;
import com.botann.driverclient.model.MemberInfo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Orion on 2017/9/21.
 */
public class MyMemberInfoAdapter extends BaseAdapter<MemberInfo> {
    private static DecimalFormat df = new DecimalFormat("######0.00");
    private static SimpleDateFormat format =  new SimpleDateFormat("MM-dd");
    private Context mContext;

    public MyMemberInfoAdapter(Context context) { mContext = context; }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.team_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyMemberInfoAdapter.MyViewHolder) holder).bind(getDataSet().get(position));
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mPhone;
        TextView mYesterdayCurrent;
        TextView mServerRank;

        public MyViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.name);
            mPhone = (TextView) itemView.findViewById(R.id.phone);
            mYesterdayCurrent = (TextView) itemView.findViewById(R.id.yesterday_current);
            mServerRank = (TextView) itemView.findViewById(R.id.server_rank);
        }

        public void bind(MemberInfo content) {
            mName.setText(content.getName());
            mPhone.setText(content.getPhone());
            DecimalFormat df = new DecimalFormat("######0.00");
            Double yCurrent = Double.valueOf(content.getYesterdayCurrent()) / 100;
            mYesterdayCurrent.setText(df.format(yCurrent) + "元/" + format.format(content.getCatchDate()));
//            if(content.getYesterdayCurrent()%100>=50) {
//                mYesterdayCurrent.setText((content.getYesterdayCurrent()/100+1)+"元/"+format.format(content.getCatchDate()));
//            }else{
//                mYesterdayCurrent.setText((content.getYesterdayCurrent()/100)+"元/"+format.format(content.getCatchDate()));
//            }
            mServerRank.setText(content.getServerRank().equals("") || content.getServerRank() == null ? "无" : content.getServerRank());
        }
    }
}
