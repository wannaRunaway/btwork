package com.botann.driverclient.model.loginmodel;

import java.io.Serializable;

/**
 * created by xuedi on 2019/4/24
 */
public class CityNametoIdContent implements Serializable {
    private int id, parentId;
    private String name, shortName, level, cityCode, zipCode, mergerName, lng, lat, pinyin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMergerName() {
        return mergerName;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
    /**
     * "id": 330100,
     * 		"name": "杭州市",
     * 		"parentId": 330000,
     * 		"shortName": "杭州",
     * 		"level": "2",
     * 		"cityCode": "0571",
     * 		"zipCode": "310026",
     * 		"mergerName": "中国,浙江省,杭州市",
     * 		"lng": "120.153576",
     * 		"lat": "30.287459",
     * 		"pinyin": "Hangzhou"
     */
}
