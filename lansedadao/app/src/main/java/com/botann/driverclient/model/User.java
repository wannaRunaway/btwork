package com.botann.driverclient.model;

import com.botann.driverclient.utils.GsonUtils;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Orion on 2017/7/11.
 */
public class User implements Serializable{
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
    private String token;
    private int accountId;
    private String phone;
    private String cityName;
    private int cityId;
    private double latitude, longtitude;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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
