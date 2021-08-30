package com.botann.charing.pad.model;

/**
 * Created by liushanguo on 2017/8/21.
 */

public class ExchangeOrder {

    private String id;
    private String plateNumber = "";
    private String driverName = "";
    private long createTime = 0;
    private String account;
    private String accountBalance;


    public String getDiffSeconds(){
        long time = (System.currentTimeMillis()-createTime)/1000;
        if (time<0) time = 0;
        return time + "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }
}
