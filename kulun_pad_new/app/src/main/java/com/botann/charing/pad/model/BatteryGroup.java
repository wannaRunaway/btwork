package com.botann.charing.pad.model;

/**
 * Created by mengchenyun on 2017/2/24.
 */

public class BatteryGroup {

    private String batteryCodeIndex;

    private String batteryCodeInfo;

    private String batteryHighestVoltIndex;

    private String batteryHighestVoltInfo;

    private String batteryLowestVoltIndex;

    private String batteryLowestVoltInfo;

    public BatteryGroup(String batteryCodeIndex, String batteryCodeInfo, String batteryHighestVoltIndex, String batteryHighestVoltInfo, String batteryLowestVoltIndex, String batteryLowestVoltInfo) {
        this.batteryCodeIndex = batteryCodeIndex;
        this.batteryCodeInfo = batteryCodeInfo;
        this.batteryHighestVoltIndex = batteryHighestVoltIndex;
        this.batteryHighestVoltInfo = batteryHighestVoltInfo;
        this.batteryLowestVoltIndex = batteryLowestVoltIndex;
        this.batteryLowestVoltInfo = batteryLowestVoltInfo;
    }

    public String getBatteryCodeIndex() {
        return batteryCodeIndex;
    }

    public void setBatteryCodeIndex(String batteryCodeIndex) {
        this.batteryCodeIndex = batteryCodeIndex;
    }

    public String getBatteryCodeInfo() {
        return batteryCodeInfo;
    }

    public void setBatteryCodeInfo(String batteryCodeInfo) {
        this.batteryCodeInfo = batteryCodeInfo;
    }

    public String getBatteryHighestVoltIndex() {
        return batteryHighestVoltIndex;
    }

    public void setBatteryHighestVoltIndex(String batteryHighestVoltIndex) {
        this.batteryHighestVoltIndex = batteryHighestVoltIndex;
    }

    public String getBatteryHighestVoltInfo() {
        return batteryHighestVoltInfo;
    }

    public void setBatteryHighestVoltInfo(String batteryHighestVoltInfo) {
        this.batteryHighestVoltInfo = batteryHighestVoltInfo;
    }

    public String getBatteryLowestVoltIndex() {
        return batteryLowestVoltIndex;
    }

    public void setBatteryLowestVoltIndex(String batteryLowestVoltIndex) {
        this.batteryLowestVoltIndex = batteryLowestVoltIndex;
    }

    public String getBatteryLowestVoltInfo() {
        return batteryLowestVoltInfo;
    }

    public void setBatteryLowestVoltInfo(String batteryLowestVoltInfo) {
        this.batteryLowestVoltInfo = batteryLowestVoltInfo;
    }
}
