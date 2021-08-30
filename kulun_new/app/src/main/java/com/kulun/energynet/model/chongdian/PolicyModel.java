package com.kulun.energynet.model.chongdian;

import java.io.Serializable;

/**
 * created by xuedi on 2019/7/15
 */
public class PolicyModel implements Serializable {
    /**
     * elecPrice : 200.0
     * servicePrice : 550.0
     * startTime : 000000
     */

    private double elecPrice;
    private double servicePrice;
    private String startTime;

    public double getElecPrice() {
        return elecPrice;
    }

    public void setElecPrice(double elecPrice) {
        this.elecPrice = elecPrice;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    /**
     * policy": {
     * 			"elecPrice": 200.0,
     * 			"servicePrice": 550.0,
     * 			"startTime": "000000"
     *                }
     */

}
