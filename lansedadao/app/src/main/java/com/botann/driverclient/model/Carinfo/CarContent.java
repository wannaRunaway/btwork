package com.botann.driverclient.model.Carinfo;

import java.io.Serializable;

/**
 * created by xuedi on 2019/2/16
 */
public class CarContent implements Serializable {
    /**
     * {
     *      *             "reason":"",
     *      *             "carType":"ER30",
     *      *             "flag":true,
     *      *             "carNo":"浙A391XC",
     *      *             "carTypeId":1,
     *      *             "startTime":1546311825440,
     *      *             "isStartCurrMonth":true,
     *      *             "carId":68
     *                     maxMonths
     *      *         }
     */
    private String reason, carType, carNo;
    private boolean flag, isStartCurrMonth;
    private int carTypeId, carId, maxMonths;
    private long startTime;

    public int getMaxMonths() {
        return maxMonths;
    }

    public void setMaxMonths(int maxMonths) {
        this.maxMonths = maxMonths;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isStartCurrMonth() {
        return isStartCurrMonth;
    }

    public void setStartCurrMonth(boolean startCurrMonth) {
        isStartCurrMonth = startCurrMonth;
    }

    public int getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(int carTypeId) {
        this.carTypeId = carTypeId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
