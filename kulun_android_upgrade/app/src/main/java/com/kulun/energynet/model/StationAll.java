package com.kulun.energynet.model;

import java.io.Serializable;

public class StationAll implements Serializable {
    //[{
    //		"id": 4,
    //		"type": 0,
    //		"name": "会岸路站",
    //		"address": "无锡梁溪区会岸路87号",
    //		"phone": "17318806653",
    //		"start_time": "00:00",
    //		"end_time": "23:59",
    //		"latitude": 31.61895,
    //		"longitude": 120.2551
    //	}
    private int id, type;
    private String name,address,phone,start_time,end_time;
    private double latitude, longitude;
    private boolean islastclick;

    public boolean isIslastclick() {
        return islastclick;
    }

    public void setIslastclick(boolean islastclick) {
        this.islastclick = islastclick;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
