package com.botann.charing.pad.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mengchenyun on 2017/2/25.
 */

public class LsBatteryInfoPack {

    @SerializedName("batteryNumber")
    @Expose
    private String batteryNumber;
    @SerializedName("highestSingletonVolt")
    @Expose
    private String highestSingletonVolt;
    @SerializedName("lowestSingletonVolt")
    @Expose
    private String lowestSingletonVolt;

    public String getBatteryNumber() {
        return batteryNumber;
    }

    public void setBatteryNumber(String batteryNumber) {
        this.batteryNumber = batteryNumber;
    }

    public String getHighestSingletonVolt() {
        return highestSingletonVolt;
    }

    public void setHighestSingletonVolt(String highestSingletonVolt) {
        this.highestSingletonVolt = highestSingletonVolt;
    }

    public String getLowestSingletonVolt() {
        return lowestSingletonVolt;
    }

    public void setLowestSingletonVolt(String lowestSingletonVolt) {
        this.lowestSingletonVolt = lowestSingletonVolt;
    }
}
