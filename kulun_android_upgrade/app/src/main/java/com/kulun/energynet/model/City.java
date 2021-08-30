package com.kulun.energynet.model;

import java.io.Serializable;

public class City implements Serializable {
    // [{
    //		"id": 330100,
    //		"name": "杭州市",
    //		"longitude": 120.153576,
    //		"latitude": 30.287459
    //	}, {
    //		"id": 110100,
    //		"name": "北京市",
    //		"longitude": 116.405285,
    //		"latitude": 39.904989
    //	}]
    private int id;
    private String name;
    private double longitude,latitude;

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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
