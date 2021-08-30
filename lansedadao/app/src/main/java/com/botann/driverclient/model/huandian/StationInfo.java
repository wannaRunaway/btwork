package com.botann.driverclient.model.huandian;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.utils.SharePref;

import java.io.Serializable;

/**
 * Created by Orion on 2017/7/26.
 */
public class StationInfo implements Serializable {
    private String stationName;
    private String address;
    private String phone;
    private String beginTime;
    private String endTime;
    private Double longitude;
    private Double latitude;
    private Double longitudeBefore;
    private Double latitudeBefore;
    private Double distance;
    private String status;
    private Integer lineCount;
    private Integer lineLevel;
    private Integer batteryCount;
    private Integer monitorStatus;
    private Integer mydistance;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getLineLevel() {
        return lineLevel;
    }

    public void setLineLevel(Integer lineLevel) {
        this.lineLevel = lineLevel;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitudeBefore() {
        return longitudeBefore;
    }

    public void setLongitudeBefore(Double longitudeBefore) {
        this.longitudeBefore = longitudeBefore;
    }

    public Double getLatitudeBefore() {
        return latitudeBefore;
    }

    public void setLatitudeBefore(Double latitudeBefore) {
        this.latitudeBefore = latitudeBefore;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLineCount() {
        return lineCount;
    }

    public void setLineCount(Integer lineCount) {
        this.lineCount = lineCount;
    }

    public Integer getBatteryCount() {
        return batteryCount;
    }

    public void setBatteryCount(Integer batteryCount) {
        this.batteryCount = batteryCount;
    }

    public Integer getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(Integer monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    public Integer getMydistance(LatLng start, LatLng end) {
        return (int)AMapUtils.calculateLineDistance(start, end);
    }

    public void setMydistance(Integer mydistance) {
        this.mydistance = mydistance;
    }
}
