package com.botann.charing.pad.model;

/**
 * Created by mengchenyun on 2017/1/18.
 */

public class PayRecord {

    private int payType;

    private String account;

    private String changeBalance;

    private String name;

    private long createTime;

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getChangeBalance() {
        return changeBalance;
    }

    public void setChangeBalance(String changeBalance) {
        this.changeBalance = changeBalance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
