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

public class NextBatteryGroupAdapter extends ArrayAdapter<BatteryGroup> {

    private Activity mContext;

    public NextBatteryGroupAdapter(Activity context, List<BatteryGroup> batteryGroups) {
        super(context, 0, batteryGroups);
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BatteryGroup batteryGroup = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.nextbatterygroup_item, parent, false);
        }
        TextView nextBatteryCodeIndex = (TextView) convertView.findViewById(R.id.tvNextBatteryCode);
        nextBatteryCodeIndex.setText(batteryGroup.getBatteryCodeIndex());
        EditText nextBatteryCodeInfo = (EditText) convertView.findViewById(R.id.etNextBatteryCode);
        nextBatteryCodeInfo.setText(batteryGroup.getBatteryCodeInfo());
        Button nextBatteryScan = (Button) convertView.findViewById(R.id.btn_scan_next);
        nextBatteryScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toScan = new Intent(mContext, QRScanActivity.class);
                toScan.putExtra("type", "changeBatteryNoUp");
                mContext.startActivityForResult(toScan, position + 10);
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
