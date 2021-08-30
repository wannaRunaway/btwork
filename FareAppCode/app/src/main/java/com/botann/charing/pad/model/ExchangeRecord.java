package com.botann.charing.pad.model;

/**
 * Created by liushanguo on 2018/4/19.
 */

public class ExchangeRecord {
    private String serialNum;
    private String carNumber;
    private String balance;
    private String fare;// 应付
    private String realFare; //实付，需支付
    private String coupon;
    private String travelMile;
    private String referMiles;  // 总里程
    private long createDate;
    private String carType;

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getRealFare() {
        return realFare;
    }

    public void setRealFare(String realFare) {
        this.realFare = realFare;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getTravelMile() {
        return travelMile;
    }

    public void setTravelMile(String travelMile) {
        this.travelMile = travelMile;
    }

    public String getReferMiles() {
        return referMiles;
    }

    public void setReferMiles(String referMiles) {
        this.referMiles = referMiles;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
