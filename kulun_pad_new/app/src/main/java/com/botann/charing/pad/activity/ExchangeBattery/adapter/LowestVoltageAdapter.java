package com.botann.charing.pad.activity.ExchangeBattery.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.cengalabs.flatui.views.FlatTextView;

import java.util.List;

import com.botann.charging.pad.R;
import com.botann.charing.pad.model.BatteryItem;

/**
 * Created by mengchenyun on 2017/1/18.
 */

public class LowestVoltageAdapter extends ArrayAdapter<BatteryItem> {

    public LowestVoltageAdapter(Activity context, List<BatteryItem> batteryItems) {
        super(context, 0, batteryItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BatteryItem batteryItem = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lowestbattery_item, parent, false);
        }
        FlatTextView batteryIndex = (FlatTextView) convertView.findViewById(R.id.tvBatteryIndex);
        batteryIndex.setText(batteryItem.getBatteryIndex());
        return convertView;
    }
}
