package com.kulun.energynet.model;

import java.io.Serializable;

public class Coupon implements Serializable {
    //[{
    //        "id": 38972,
    //        "couponName": "杭州时空充电宝优惠活动！",
    //        "amount": 11.2,
    //        "used": 0,
    //        "beginDate": "2020-11-29T18:57:00+08:00",
    //        "expireDate": "2020-12-31T08:51:02+08:00"
    //    }]
    private int id, used;
    private String couponName ,beginDate, expireDate;
    private double amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
