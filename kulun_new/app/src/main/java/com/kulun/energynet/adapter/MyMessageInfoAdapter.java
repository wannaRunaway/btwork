package com.kulun.energynet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kulun.energynet.R;
import com.kulun.energynet.adapter.adapterinter.MessageInfoInterface;
import com.kulun.energynet.model.MessageInfo;

import java.text.SimpleDateFormat;

/**
 * Created by Orion on 2017/8/4.
 */
public class MyMessageInfoAdapter extends BaseAdapter<MessageInfo> {
    private static SimpleDateFormat format =  new SimpleDateFormat("yyyy.MM.dd");
    private Context mContext;
    public MessageInfoInterface infoInterface;

    public MyMessageInfoAdapter(Context context, MessageInfoInterface infoInterface) {
        mContext = context;
        this.infoInterface = infoInterface;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_list_item, parent, false);
        return new MyMessageInfoAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyMessageInfoAdapter.MyViewHolder) holder).bind(getDataSet().get(position));
        MyViewHolder holder1 = (MyViewHolder) holder;
        holder1.llcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sstring = getDataSet().get(position).getContent();
                if (sstring.indexOf("&&http") != -1){
                    String ss[] = sstring.split("&&");
                    infoInterface.tourl(ss[1]);
                }
            }
        });
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        //TextView mUnread;
        TextView mContent;
        TextView mTime;
        private LinearLayout llcontent;

        public MyViewHolder(View itemView) {
            super(itemView);

            //mUnread = (TextView) itemView.findViewById(R.id.unread);
            mContent = (TextView) itemView.findViewById(R.id.content);
            mTime = (TextView) itemView.findViewById(R.id.message_time);
            llcontent = itemView.findViewById(R.id.station_list_item);
        }

        public void bind(MessageInfo content) {
            String ss = content.getContent();
            if (ss.indexOf("&&http") != -1){
                String strings[] = ss.split("&&");
                mContent.setText(strings[0]);
            }else {
                mContent.setText(content.getContent());
            }
            mTime.setText(format.format(content.getCreateTime()));
        }
    }
}
