package com.kulun.energynet.model;

import java.io.Serializable;

public class ActivityDetail implements Serializable {
    //{
    //        "ruleId": 159,
    //        "ruleType": 1,
    //        "ruleName": "苏州市-ER30-不带电车月租费",
    //        "ruleValue": 0,
    //        "flag": true,
    //        "reason": "",
    //        "startDate": "2020-12-26",
    //        "maxMonths": 4,
    //        "price": 0.1,
    //        "BuyCurrMonth": true,
    //        "currMonthDiscountPrice": 0.02
    //    }
    private int ruleId, ruleType,ruleValue,maxMonths;
    private String ruleName,reason,startDate;
    private boolean flag,BuyCurrMonth;
    private double price,currMonthDiscountPrice;

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getRuleType() {
        return ruleType;
    }

    public void setRuleType(int ruleType) {
        this.ruleType = ruleType;
    }

    public int getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(int ruleValue) {
        this.ruleValue = ruleValue;
    }

    public int getMaxMonths() {
        return maxMonths;
    }

    public void setMaxMonths(int maxMonths) {
        this.maxMonths = maxMonths;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isBuyCurrMonth() {
        return BuyCurrMonth;
    }

    public void setBuyCurrMonth(boolean buyCurrMonth) {
        BuyCurrMonth = buyCurrMonth;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCurrMonthDiscountPrice() {
        return currMonthDiscountPrice;
    }

    public void setCurrMonthDiscountPrice(double currMonthDiscountPrice) {
        this.currMonthDiscountPrice = currMonthDiscountPrice;
    }
}
