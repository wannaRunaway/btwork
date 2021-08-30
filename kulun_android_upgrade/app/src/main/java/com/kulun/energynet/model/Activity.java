package com.kulun.energynet.model;

import java.io.Serializable;

public class Activity implements Serializable {
    //        "id": 465,
    //        "type": 7,
    //        "name": "杭州E17 3000元8000公里包月套餐",
    //        "photo": "",
    //        "remark": "踩踩踩踩踩踩从",
    //        "startTime": "2020-12-01",
    //        "endTime": "2021-01-30"
    private int id, type;
    private String photo,remark,startTime,endTime,name;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
