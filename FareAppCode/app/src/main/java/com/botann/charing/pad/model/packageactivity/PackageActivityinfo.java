package com.botann.charing.pad.model.packageactivity;

import java.io.Serializable;

/**
 * created by xuedi on 2019/2/25
 */
public class PackageActivityinfo implements Serializable {
    private String name;
    private int id, type, maxMonths;
    private long startTime, endTime;
    private double packagePrice;

    public int getMaxMonths() {
        return maxMonths;
    }

    public void setMaxMonths(int maxMonths) {
        this.maxMonths = maxMonths;
    }

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
    /**
     *      *      *             {
     *      *      *                 "name":"包月套餐附加费用",
     *      *      *                 "packagePrice":20000000,
     *      *      *                 "startTime":1546272000000,
     *      *      *                 "id":661,
     *      *      *                 "endTime":1548864000000,
     *      *      *                 "type":7
     *      *      *             }
     */
}
