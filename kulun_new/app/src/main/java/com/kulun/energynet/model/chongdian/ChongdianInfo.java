package com.kulun.energynet.model.chongdian;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

import java.io.Serializable;

/**
 * created by xuedi on 2019/6/27
 */
public class ChongdianInfo implements Serializable {

    /**
     * stationLng : 120.043349
     * address : 五常中学
     * parkFee : 免费停车
     * totalConnector : 38
     * slowAvailConnector : 0
     * busineHours : 00:00-23:59
     * stationName : 余杭区五常中学充电站
     * stationLat : 30.274391
     * id : 14
     * quickAvailConnector : 30
     * electricityFee : 1.6元/度
     * quickConnector 快充总数 slowConnector慢充总数
     * stationCompany
     */

    private double stationLng;
    private String address;
    private String parkFee;
    private int totalConnector;
    private int slowAvailConnector;
    private String busineHours;
    private String stationName;
    private double stationLat;
    private int id;
    private int quickAvailConnector;
    private int quickConnector, slowConnector;
    private String electricityFee, discountElectricityFee, stationCompany;
    private Integer mydistance;

    public double getStationLng() {
        return stationLng;
    }

    public void setStationLng(double stationLng) {
        this.stationLng = stationLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParkFee() {
        return parkFee;
    }

    public void setParkFee(String parkFee) {
        this.parkFee = parkFee;
    }

    public int getTotalConnector() {
        return totalConnector;
    }

    public void setTotalConnector(int totalConnector) {
        this.totalConnector = totalConnector;
    }

    public int getSlowAvailConnector() {
        return slowAvailConnector;
    }

    public void setSlowAvailConnector(int slowAvailConnector) {
        this.slowAvailConnector = slowAvailConnector;
    }

    public String getBusineHours() {
        return busineHours;
    }

    public void setBusineHours(String busineHours) {
        this.busineHours = busineHours;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public double getStationLat() {
        return stationLat;
    }

    public void setStationLat(double stationLat) {
        this.stationLat = stationLat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuickAvailConnector() {
        return quickAvailConnector;
    }

    public void setQuickAvailConnector(int quickAvailConnector) {
        this.quickAvailConnector = quickAvailConnector;
    }

    public String getElectricityFee() {
        return electricityFee;
    }

    public void setElectricityFee(String electricityFee) {
        this.electricityFee = electricityFee;
    }

    public String getDiscountElectricityFee() {
        return discountElectricityFee;
    }

    public void setDiscountElectricityFee(String discountElectricityFee) {
        this.discountElectricityFee = discountElectricityFee;
    }

    public int getQuickConnector() {
        return quickConnector;
    }

    public void setQuickConnector(int quickConnector) {
        this.quickConnector = quickConnector;
    }

    public int getSlowConnector() {
        return slowConnector;
    }

    public void setSlowConnector(int slowConnector) {
        this.slowConnector = slowConnector;
    }

    public String getStationCompany() {
        return stationCompany;
    }

    public void setStationCompany(String stationCompany) {
        this.stationCompany = stationCompany;
    }

    public Integer getMydistance(LatLng start, LatLng end) {
        return (int) AMapUtils.calculateLineDistance(start, end);
    }

    public void setMydistance(Integer mydistance) {
        this.mydistance = mydistance;
    }
}
