package com.botann.driverclient.model.invite;
import com.botann.driverclient.model.BindCar;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * created by xuedi on 2019/4/19
 */
public class AccountInfo implements Serializable {
    /**
     *  {
     * 			"account": "2019041800010595",
     * 			"name": "张三",
     * 			"balance": 0.01,
     * 			"couponNum": 0,
     * 			"messageNum": 0,
     * 			"city": "杭州市",
     * 			"phone": "15757129606",
     * 			"appCanRecharge": 1,
     * 			"bindCarList": []
     *                },
     */
    private String account, name, city, phone;
    private double balance;
    private int couponNum, messageNum, appCanRecharge;
    private ArrayList<BindCar> bindCarList;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(int messageNum) {
        this.messageNum = messageNum;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
