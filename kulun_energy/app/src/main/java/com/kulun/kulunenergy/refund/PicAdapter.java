package com.kulun.kulunenergy.refund;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kulun.kulunenergy.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.PicViewHolder>{
    private OnButtonListener buttonListener;
    private List<String> list;
    private Context context;
    public PicAdapter(Context context, List<String> list, OnButtonListener buttonListener){
        this.buttonListener=buttonListener;
        this.list=list;
        this.context=context;
    }
    @Override
    public PicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refundpic,null);
        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PicViewHolder holder, int position) {
        if(!TextUtils.isEmpty(list.get(position))){
            Glide.with(context).load(list.get(position)).into(holder.iv);
        }
        holder.iv.setOnClickListener(view->{
            if(buttonListener!=null){
                buttonListener.onButtonClick(holder.iv,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PicViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        PicViewHolder(View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
        }
    }
    public interface OnButtonListener{
        void onButtonClick(View view,int position);
    }
}
