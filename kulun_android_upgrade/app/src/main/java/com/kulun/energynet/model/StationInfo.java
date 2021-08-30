package com.kulun.energynet.model;

import java.io.Serializable;
/**
 * Created by Orion on 2017/7/26.
 */
public class StationInfo implements Serializable,Comparable<StationInfo>{

    //"id": 99,
    //		"type": 1,
    //		"name": "西溪福堤1号",
    //		"address": "杭州市文二西路西溪湿地北门福堤1号",
    //		"phone": "13296723879",
    //		"contact": "胡志林",
    //		"channel": 15,
    //		"status": 0,
    //		"latitude": 30.278,
    //		"longitude": 120.07,
    //		"start_time": "2018-04-12T00:00:48+08:00",
    //		"end_time": "2018-04-12T23:59:48+08:00",
    //		"appointment": true,
    //		"waiting": 0,
    //		"distance": 6.32,
    //		"battery": 0
    private int id, type,channel,status,battery,waiting;
    private String name,address,phone,contact,start_time,end_time;
    private double latitude,longitude, distances;
    private boolean appointment,listclick;

    public boolean isIslastclick() {
        return islastclick;
    }

    public void setIslastclick(boolean islastclick) {
        this.islastclick = islastclick;
    }

    private boolean islastclick;

    public boolean isListclick() {
        return listclick;
    }

    public void setListclick(boolean listclick) {
        this.listclick = listclick;
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

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getStatus() {
        return status;
    }

    public double getDistances() {
        return distances;
    }

    public void setDistances(double distances) {
        this.distances = distances;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    public boolean isAppointment() {
        return appointment;
    }

    public void setAppointment(boolean appointment) {
        this.appointment = appointment;
    }

    @Override
    public int compareTo(StationInfo o) {
        return (int) (this.distances-o.getDistances());
    }
}
