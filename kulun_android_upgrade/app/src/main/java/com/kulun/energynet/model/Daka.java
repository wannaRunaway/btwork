package com.kulun.energynet.model;

import java.io.Serializable;

public class Daka implements Serializable {
    //{
    //        "id": 1027,
    //        "type": 0,
    //        "status": 4,
    //        "accountId": 55079,
    //        "accountName": "高华",
    //        "carNumber": "琼AD29616",
    //        "carMile": 1000,
    //        "soc": 50,
    //        "createTime": "2020年12月27日 19:32:44"
    //    }
    private int id, type, status, accountId, carMile, soc;
    private String accountName, carNumber, createTime, account;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCarMile() {
        return carMile;
    }

    public void setCarMile(int carMile) {
        this.carMile = carMile;
    }

    public int getSoc() {
        return soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
