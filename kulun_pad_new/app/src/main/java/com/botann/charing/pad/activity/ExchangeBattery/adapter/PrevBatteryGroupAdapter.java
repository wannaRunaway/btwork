package com.botann.charing.pad.activity.ExchangeBattery.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.List;

import com.botann.charging.pad.R;
import com.botann.charing.pad.components.zxing.QRScanActivity;
import com.botann.charing.pad.model.BatteryGroup;

/**
 * Created by mengchenyun on 2017/2/24.
 */

public class PrevBatteryGroupAdapter extends ArrayAdapter<BatteryGroup> {

    private Activity mContext;

    public PrevBatteryGroupAdapter(Activity context, List<BatteryGroup> batteryGroups) {
        super(context, 0, batteryGroups);
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BatteryGroup batteryGroup = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.prevbatterygroup_item, parent, false);
        }
        TextView prevBatteryCodeIndex = (TextView) convertView.findViewById(R.id.tvPrevBatteryCode);
        prevBatteryCodeIndex.setText(batteryGroup.getBatteryCodeIndex());
        EditText prevBatteryCodeInfo = (EditText) convertView.findViewById(R.id.etPrevBatteryCode);
        prevBatteryCodeInfo.setText(batteryGroup.getBatteryCodeInfo());
        Button prevBatteryScan = (Button) convertView.findViewById(R.id.btn_scan_prev);
        prevBatteryScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toScan = new Intent(mContext, QRScanActivity.class);
                toScan.putExtra("type", "changeBatteryNo");
                mContext.startActivityForResult(toScan, position + 2);
            }
        });

        TextView highestVoltIndex = (TextView) convertView.findViewById(R.id.tvHighestVolt);
        highestVoltIndex.setText(batteryGroup.getBatteryHighestVoltIndex());
        EditText highestVoltInfo = (EditText) convertView.findViewById(R.id.etHighestSingleVoltage);
        highestVoltInfo.setText(batteryGroup.getBatteryHighestVoltInfo());

        TextView lowestVoltIndex = (TextView) convertView.findViewById(R.id.tvLowestVolt);
        lowestVoltIndex.setText(batteryGroup.getBatteryLowestVoltIndex());
        EditText lowestVoltInfo = (EditText) convertView.findViewById(R.id.etLowestSingleVoltage);
        lowestVoltInfo.setText(batteryGroup.getBatteryLowestVoltInfo());

        return convertView;
    }
}
