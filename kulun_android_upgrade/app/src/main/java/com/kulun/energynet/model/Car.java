package com.kulun.energynet.model;

import java.io.Serializable;

public class Car implements Serializable {
    // json:{"code":0,"data":[{"id":15977,"car_id":13580,"
    // plate_number":"浙AD53772","car_type":"","bind_status":1,"left_mile":0,
    // "month_mile":0,"inuse":false,"battery_count":4,"battery_status":true,"soc":0}]}
    private int id, car_id, bind_status, left_mile, month_mile,battery_count,soc;
    private String plate_number,car_type;
    private boolean inuse,battery_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getBind_status() {
        return bind_status;
    }

    public void setBind_status(int bind_status) {
        this.bind_status = bind_status;
    }

    public int getLeft_mile() {
        return left_mile;
    }

    public void setLeft_mile(int left_mile) {
        this.left_mile = left_mile;
    }

    public int getMonth_mile() {
        return month_mile;
    }

    public void setMonth_mile(int month_mile) {
        this.month_mile = month_mile;
    }

    public int getBattery_count() {
        return battery_count;
    }

    public void setBattery_count(int battery_count) {
        this.battery_count = battery_count;
    }

    public int getSoc() {
        return soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public boolean isInuse() {
        return inuse;
    }

    public void setInuse(boolean inuse) {
        this.inuse = inuse;
    }

    public boolean isBattery_status() {
        return battery_status;
    }

    public void setBattery_status(boolean battery_status) {
        this.battery_status = battery_status;
    }
}
