package com.btkj.chongdianbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.utils.Constants;
import com.btkj.chongdianbao.utils.L;
import com.bumptech.glide.Glide;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList;
    private LayoutInflater inflater;
    private DeletePicCallBack callBack;

    public GridViewAdapter(Context mContext, List<String> mList,DeletePicCallBack callBack) {
        this.mContext = mContext;
        this.mList = mList;
        this.callBack=callBack;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        //return mList.size() + 1;//因为最后多了一个添加图片的ImageView
        int count = mList == null ? 1 : mList.size() + 1;
        if (count > Constants.MAX_SELECT_PIC_NUM) {
            return mList.size();
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_refundpic, parent,false);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
        RelativeLayout delRl=convertView.findViewById(R.id.rl_delete);
        if (position < mList.size()) {
            //代表+号之前的需要正常显示图片
            String picUrl = mList.get(position); //图片路径
            Glide.with(mContext).load(picUrl).into(iv);
            delRl.setVisibility(View.VISIBLE);
            delRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callBack!=null){
                        callBack.del(position);
                    }
                }
            });
        } else {
            delRl.setVisibility(View.GONE);
            iv.setImageResource(R.mipmap.img_refundadd);//最后一个显示加号图片
        }
        return convertView;
    }
    public interface DeletePicCallBack{
        void del(int index);
    }
}
