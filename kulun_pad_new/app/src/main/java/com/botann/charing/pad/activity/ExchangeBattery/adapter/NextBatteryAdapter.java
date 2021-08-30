package com.botann.charing.pad.activity.ExchangeBattery.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.cengalabs.flatui.views.FlatEditText;
import com.cengalabs.flatui.views.FlatTextView;

import java.util.List;

import com.botann.charging.pad.R;
import com.botann.charing.pad.components.zxing.QRScanActivity;
import com.botann.charing.pad.model.BatteryItem;

/**
 * Created by mengchenyun on 2017/1/18.
 */

public class NextBatteryAdapter extends ArrayAdapter<BatteryItem> {

    private Activity mContext;

    public NextBatteryAdapter(Activity context, List<BatteryItem> batteryItems) {
        super(context, 0, batteryItems);
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BatteryItem batteryItem = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.nextbattery_item, parent, false);
        }
        FlatTextView batteryIndex = (FlatTextView) convertView.findViewById(R.id.tvBatteryIndex);
        batteryIndex.setText(batteryItem.getBatteryIndex());
        FlatEditText batteryInfo = (FlatEditText) convertView.findViewById(R.id.etNextBatteryCode);
        batteryInfo.setText(batteryItem.getBatteryInfo());
        Button scanPrev = (Button) convertView.findViewById(R.id.btn_scan_next);
        scanPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toScan = new Intent(mContext, QRScanActivity.class);
                toScan.putExtra("type", "changeBatteryNoUp");
                mContext.startActivityForResult(toScan, position + 8);
            }
        });

        return convertView;
    }
}
