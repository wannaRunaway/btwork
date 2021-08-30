package com.botann.charing.pad.model;

/**
 * Created by mengchenyun on 2017/2/16.
 */

public class BatteryItem {

    private String batteryIndex;

    private String batteryInfo;

    public BatteryItem(String batteryIndex, String batteryInfo) {
        this.batteryIndex = batteryIndex;
        this.batteryInfo = batteryInfo;
    }

    public String getBatteryIndex() {
        return batteryIndex;
    }

    public void setBatteryIndex(String batteryIndex) {
        this.batteryIndex = batteryIndex;
    }

    public String getBatteryInfo() {
        return batteryInfo;
    }

    public void setBatteryInfo(String batteryInfo) {
        this.batteryInfo = batteryInfo;
    }
}
