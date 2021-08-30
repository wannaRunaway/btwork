package com.btkj.chongdianbao.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/8/1
 */
public class User implements Serializable {
    private static User user;
    public static User getInstance(){
        if (null == user){
            synchronized (User.class){
                if (null == user){
                    user = new User();
                }
            }
        }
        return user;
    }
    /**
     * balance : 1110919
     * id : 41
     * name : 薛地
     * noReaded : 4
     * phone : 15209603592
     *  accountNo
     */
    private int id, noReaded, reserverId;
    private String name, phone, wxappid, accountNo, cityName;
    private double balance, mylongtitude, mylatitude;
//    private boolean islogin;
    private Station station;
    private boolean isYuyue, isDelay, isneedlogin,isneedcheckIdcard;
    public int getReserverId() {
        return reserverId;
    }

    public void setReserverId(int reserverId) {
        this.reserverId = reserverId;
    }

    public double getMylongtitude() {
        return mylongtitude;
    }

    public void setMylongtitude(double mylongtitude) {
        this.mylongtitude = mylongtitude;
    }

    public double getMylatitude() {
        return mylatitude;
    }

    public void setMylatitude(double mylatitude) {
        this.mylatitude = mylatitude;
    }

    public String getWxappid() {
        return wxappid;
    }

    public void setWxappid(String wxappid) {
        this.wxappid = wxappid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getNoReaded() {
        return noReaded;
    }

    public void setNoReaded(int noReaded) {
        this.noReaded = noReaded;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public boolean isYuyue() {
        return isYuyue;
    }

    public void setYuyue(boolean yuyue) {
        isYuyue = yuyue;
    }

    public boolean isDelay() {
        return isDelay;
    }

    public void setDelay(boolean delay) {
        isDelay = delay;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isIsneedlogin() {
        return isneedlogin;
    }

    public void setIsneedlogin(boolean isneedlogin) {
        this.isneedlogin = isneedlogin;
    }

    public boolean isIsneedcheckIdcard() {
        return isneedcheckIdcard;
    }

    public void setIsneedcheckIdcard(boolean isneedcheckIdcard) {
        this.isneedcheckIdcard = isneedcheckIdcard;
    }
}
