package com.kulun.energynet.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/1/18
 */
public class Promotions implements Serializable {
    /**
     * {
     * "name": "包月套餐附加费用",
     * "packagePrice": 20000000,
     * "startTime": 1546272000000,
     * "id": 661,
     * "endTime": 1548864000000,
     * "type": 7
     * maxMonths
     * limitMaxMonths
     * packageTime
     *
     * *    }
     */
    private String name;
    private int id, type, limitMaxMonths, packageTime;
    private long startTime, endTime;
    private double packagePrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(double packagePrice) {
        this.packagePrice = packagePrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }


    public int getLimitMaxMonths() {
        return limitMaxMonths;
    }

    public void setLimitMaxMonths(int limitMaxMonths) {
        this.limitMaxMonths = limitMaxMonths;
    }

    public int getPackageTime() {
        return packageTime;
    }

    public void setPackageTime(int packageTime) {
        this.packageTime = packageTime;
    }
}
