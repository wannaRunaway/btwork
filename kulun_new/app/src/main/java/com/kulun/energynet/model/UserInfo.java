package com.kulun.energynet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orion on 2017/7/17.
 */

/**
 * onSuccess json = {
 * 	"code": 0,
 * 	"msg": "成功",
 * 	"content": {
 * 		"account": "2018091300000016",
 * 		"name": "测试司机f1",
 * 		"balance": 0.00,
 * 		"couponNum": 0,
 * 		"messageNum": 0,
 * 		"city": "杭州市",
 * 		"appCanRecharge": 0,
 * 		"bindCarList": [{
 * 			"id": 12,
 * 			"plate_number": "苏E05EV8",
 * 			"status": 1
 *                }]    * 	},
 * 	"success": true
 * }
 */
public class UserInfo implements Serializable {
    private String account;
    private String name;
    private Float balance;
    private Integer couponNum, mile;
    private Integer messageNum;
    private String city;
    //新加入appCanRecharge(充值)、bindCarList(车牌列表)
    private int appCanRecharge;
    private ArrayList<BindCar> bindCarList;
    /**
     * boolean hasChargeAccount; // true有充电账户 false没有充电账户
     * List<Map> chargeBindCarList; // 充电车绑定(Map和换电车一致)
     * BigDecimal chargeBalance; // 充电余额
     */
    private boolean hasChargeAccount, hasAuthAccount;
    private ArrayList<BindCar> chargeBindCarList;
    private float chargeBalance;
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Integer getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(Integer couponNum) {
        this.couponNum = couponNum;
    }

    public Integer getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(Integer messageNum) {
        this.messageNum = messageNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAppCanRecharge() {
        return appCanRecharge;
    }

    public void setAppCanRecharge(int appCanRecharge) {
        this.appCanRecharge = appCanRecharge;
    }

    public ArrayList<BindCar> getBindCarList() {
        return bindCarList;
    }

    public void setBindCarList(ArrayList<BindCar> bindCarList) {
        this.bindCarList = bindCarList;
    }

    public int getMile() {
        return mile;
    }

    public void setMile(int mile) {
        this.mile = mile;
    }

    public void setMile(Integer mile) {
        this.mile = mile;
    }

    public boolean isHasChargeAccount() {
        return hasChargeAccount;
    }

    public void setHasChargeAccount(boolean hasChargeAccount) {
        this.hasChargeAccount = hasChargeAccount;
    }

    public ArrayList<BindCar> getChargeBindCarList() {
        return chargeBindCarList;
    }

    public void setChargeBindCarList(ArrayList<BindCar> chargeBindCarList) {
        this.chargeBindCarList = chargeBindCarList;
    }

    public float getChargeBalance() {
        return chargeBalance;
    }

    public void setChargeBalance(float chargeBalance) {
        this.chargeBalance = chargeBalance;
    }

    public boolean isHasAuthAccount() {
        return hasAuthAccount;
    }

    public void setHasAuthAccount(boolean hasAuthAccount) {
        this.hasAuthAccount = hasAuthAccount;
    }
}
