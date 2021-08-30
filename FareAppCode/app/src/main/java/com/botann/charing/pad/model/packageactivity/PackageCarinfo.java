package com.botann.charing.pad.model.packageactivity;

import java.io.Serializable;

/**
 * created by xuedi on 2019/2/25
 */
public class PackageCarinfo implements Serializable {
    /**
     * *      *             {
     * *      *                 "reason":"",
     * *      *                 "carType":"ER30",
     * *      *                 "flag":true,
     * *      *                 "carNo":"浙AM55J0",
     * *      *                 "carTypeId":1,
     * *      *                 "startTime":1546329538592,
     * *      *                 "isStartCurrMonth":true,
     * *      *                 "carId":43
     * *      *             }
     */
    private String reason, carType, carNo, defaultExchangeRuleName, firstExchangeRuleName;
    private boolean flag, isStartCurrMonth;
    private int carTypeId, carId;
    private long startTime;

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

    public String getDefaultExchangeRuleName() {
        return defaultExchangeRuleName;
    }

    public void setDefaultExchangeRuleName(String defaultExchangeRuleName) {
        this.defaultExchangeRuleName = defaultExchangeRuleName;
    }

    public String getFirstExchangeRuleName() {
        return firstExchangeRuleName;
    }

    public void setFirstExchangeRuleName(String firstExchangeRuleName) {
        this.firstExchangeRuleName = firstExchangeRuleName;
    }
}
