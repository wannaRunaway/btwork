package com.botann.charing.pad.model.carinfo;

import com.botann.charing.pad.model.LsBatteryInfoPack;

import java.io.Serializable;
import java.util.List;

public class Carmodule implements Serializable {
    private String username;
    private String carplate;
    private String totalMiles;
    private String oldTotalMiles;

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    private String soc;
    private List<LsBatteryInfoPack> lsBatteryInfoPack;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCarplate() {
        return carplate;
    }

    public void setCarplate(String carplate) {
        this.carplate = carplate;
    }

    public String getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(String totalMiles) {
        this.totalMiles = totalMiles;
    }

    public String getOldTotalMiles() {
        return oldTotalMiles;
    }

    public void setOldTotalMiles(String oldTotalMiles) {
        this.oldTotalMiles = oldTotalMiles;
    }

    public List<LsBatteryInfoPack> getLsBatteryInfoPack() {
        return lsBatteryInfoPack;
    }

    public void setLsBatteryInfoPack(List<LsBatteryInfoPack> lsBatteryInfoPack) {
        this.lsBatteryInfoPack = lsBatteryInfoPack;
    }
}
