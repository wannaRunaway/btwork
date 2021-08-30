package com.kulun.energynet.model.chongdian;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/7/15
 */
public class StationModel implements Serializable {
    /**
     * id : 13
     * stationId : 654
     * operatorId : MA1MY0GF9
     * equipmentOwnerId : 111111111
     * stationName : 立际充
     * countryCode : CN
     * areaCode : 320114
     * address : 云密城
     * serviceTel : 4008288517
     * stationType : 1
     * stationStatus : 50
     * parkNums : 6
     * stationLng : 118.779073
     * stationLat : 31.991347
     * construction : 2
     * busineHours : 全天开放
     * electricityFee : 00:00-16:30 200.0元/度;16:30-18:00 500.0元/度;18:00-18:30 550.0元/度;18:30-19:00 200.0元/度;19:00-19:30 130.0元/度;19:30-24:00 500.0元/度
     * serviceFee : 00:00-16:30 550.0元/度;16:30-18:00 550.0元/度;18:00-18:30 500.0元/度;18:30-19:00 550.0元/度;19:00-19:30 540.0元/度;19:30-24:00 550.0元/度
     * parkFee : 免费停车
     * payMent : 线上、刷卡、现金
     * supportOrder : 0
     * createTime : 1561558441000
     * updateTime : 1562554440000
     * del : false
     * chargeEquipmentList : []
     */

    private int id;
    private String stationId;
    private String operatorId;
    private String equipmentOwnerId;
    private String stationName;
    private String countryCode;
    private String areaCode;
    private String address;
    private String serviceTel;
    private int stationType;
    private int stationStatus;
    private int parkNums;
    private double stationLng;
    private double stationLat;
    private int construction;
    private String busineHours;
    private String electricityFee;
    private String serviceFee;
    private String parkFee;
    private String payMent;
    private int supportOrder;
    private long createTime;
    private long updateTime;
    private boolean del;
    private List<?> chargeEquipmentList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getEquipmentOwnerId() {
        return equipmentOwnerId;
    }

    public void setEquipmentOwnerId(String equipmentOwnerId) {
        this.equipmentOwnerId = equipmentOwnerId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServiceTel() {
        return serviceTel;
    }

    public void setServiceTel(String serviceTel) {
        this.serviceTel = serviceTel;
    }

    public int getStationType() {
        return stationType;
    }

    public void setStationType(int stationType) {
        this.stationType = stationType;
    }

    public int getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(int stationStatus) {
        this.stationStatus = stationStatus;
    }

    public int getParkNums() {
        return parkNums;
    }

    public void setParkNums(int parkNums) {
        this.parkNums = parkNums;
    }

    public double getStationLng() {
        return stationLng;
    }

    public void setStationLng(double stationLng) {
        this.stationLng = stationLng;
    }

    public double getStationLat() {
        return stationLat;
    }

    public void setStationLat(double stationLat) {
        this.stationLat = stationLat;
    }

    public int getConstruction() {
        return construction;
    }

    public void setConstruction(int construction) {
        this.construction = construction;
    }

    public String getBusineHours() {
        return busineHours;
    }

    public void setBusineHours(String busineHours) {
        this.busineHours = busineHours;
    }

    public String getElectricityFee() {
        return electricityFee;
    }

    public void setElectricityFee(String electricityFee) {
        this.electricityFee = electricityFee;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getParkFee() {
        return parkFee;
    }

    public void setParkFee(String parkFee) {
        this.parkFee = parkFee;
    }

    public String getPayMent() {
        return payMent;
    }

    public void setPayMent(String payMent) {
        this.payMent = payMent;
    }

    public int getSupportOrder() {
        return supportOrder;
    }

    public void setSupportOrder(int supportOrder) {
        this.supportOrder = supportOrder;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    public List<?> getChargeEquipmentList() {
        return chargeEquipmentList;
    }

    public void setChargeEquipmentList(List<?> chargeEquipmentList) {
        this.chargeEquipmentList = chargeEquipmentList;
    }

    /**
     * {
     * 			"id": 13,
     * 			"stationId": "654",
     * 			"operatorId": "MA1MY0GF9",
     * 			"equipmentOwnerId": "111111111",
     * 			"stationName": "立际充",
     * 			"countryCode": "CN",
     * 			"areaCode": "320114",
     * 			"address": "云密城",
     * 			"serviceTel": "4008288517",
     * 			"stationType": 1,
     * 			"stationStatus": 50,
     * 			"parkNums": 6,
     * 			"stationLng": 118.779073,
     * 			"stationLat": 31.991347,
     * 			"construction": 2,
     * 			"busineHours": "全天开放",
     * 			"electricityFee": "00:00-16:30 200.0元/度;16:30-18:00 500.0元/度;18:00-18:30 550.0元/度;18:30-19:00 200.0元/度;19:00-19:30 130.0元/度;19:30-24:00 500.0元/度",
     * 			"serviceFee": "00:00-16:30 550.0元/度;16:30-18:00 550.0元/度;18:00-18:30 500.0元/度;18:30-19:00 550.0元/度;19:00-19:30 540.0元/度;19:30-24:00 550.0元/度",
     * 			"parkFee": "免费停车",
     * 			"payMent": "线上、刷卡、现金",
     * 			"supportOrder": 0,
     * 			"createTime": 1561558441000,
     * 			"updateTime": 1562554440000,
     * 			"del": false,
     * 			"chargeEquipmentList": []
     *                },
     */
}
