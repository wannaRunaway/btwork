package com.kulun.kulunenergy.model;

import android.app.Activity;
import android.content.Context;

import com.kulun.kulunenergy.modelnew.StationInfo;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.SharePref;
import com.kulun.kulunenergy.utils.Utils;

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
     *  reserverId
     */
    private int id, noReaded;
    private String name, phone, wxappid, accountNo, cityName;
    private double balance = 0, mylongtitude, mylatitude, companyBalance;
//    private boolean islogin;
    private StationInfo station;
//    private boolean isYuyue, isDelay, isneedlogin,isneedcheckIdcard;
    private boolean isneedlogin;
    private String token, bucketName;
    private int accountId;
    private int cityId;
    private double latitude, longtitude;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public double getCompanyBalance() {
        return companyBalance;
    }

    public void setCompanyBalance(double companyBalance) {
        this.companyBalance = companyBalance;
    }
    //    public int getReserverId() {
//        return reserverId;
//    }
//
//    public void setReserverId(int reserverId) {
//        this.reserverId = reserverId;
//    }

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

    public StationInfo getStation() {
        return station;
    }

    public void setStation(StationInfo station) {
        this.station = station;
    }

    //    public boolean isYuyue() {
//        return isYuyue;
//    }
//
//    public void setYuyue(boolean yuyue) {
//        isYuyue = yuyue;
//    }
//
//    public boolean isDelay() {
//        return isDelay;
//    }
//
//    public void setDelay(boolean delay) {
//        isDelay = delay;
//    }

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

//    public boolean isIsneedcheckIdcard() {
//        return isneedcheckIdcard;
//    }
//
//    public void setIsneedcheckIdcard(boolean isneedcheckIdcard) {
//        this.isneedcheckIdcard = isneedcheckIdcard;
//    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAccountId(Activity context) {
        int accountId = this.accountId == 0 ? (int) SharePref.get(context, API.accountId, 0) : this.accountId;
        if (accountId == 0) {
            Utils.snackbar(context, "账号不存在或账号没有对应换电站运营公司");
            return 0;
        }
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
