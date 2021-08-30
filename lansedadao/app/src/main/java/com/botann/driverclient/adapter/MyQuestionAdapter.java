package com.botann.driverclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.botann.driverclient.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyQuestionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;

    public MyQuestionAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();//注意此处
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.photo_item_detail, null);
        mImageView = (ImageView) convertView.findViewById(R.id.photo_item_detail);

        Glide.with(context).load(list.get(position)).into(mImageView);

        return convertView;
    }

}
