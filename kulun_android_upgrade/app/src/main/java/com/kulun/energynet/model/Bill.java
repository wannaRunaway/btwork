package com.kulun.energynet.model;

import java.io.Serializable;

public class Bill implements Serializable {
    //{
    //        "bid": 2819562,
    //        "cType": -1,
    //        "type": 3,
    //        "change_balance": "0.00",
    //        "create_time": "2020年12月26日 15:06:34"
    //    },
    private int bid, cType,type,payType;
    private String create_time ,change_balance;

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getcType() {
        return cType;
    }

    public void setcType(int cType) {
        this.cType = cType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setChange_balance(String change_balance) {
        this.change_balance = change_balance;
    }

    public String getChange_balance() {
        return change_balance;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
