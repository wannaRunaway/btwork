package com.kulun.energynet.model;

import java.io.Serializable;

public class RechargeBill implements Serializable {
    //2{"bnumber":"UUIDGLixQ20201229144511","changeType":2,"payType":2,"amount":100,"btime":"2020年12月29日 14:45:12"}}
    private String bnumber,btime;
    private int changeType, payType;
    private double amount;

    public String getBnumber() {
        return bnumber;
    }

    public void setBnumber(String bnumber) {
        this.bnumber = bnumber;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public int getChangeType() {
        return changeType;
    }

    public void setChangeType(int changeType) {
        this.changeType = changeType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
