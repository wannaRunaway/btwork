package com.kulun.energynet.model;

import java.io.Serializable;

public class CarApply implements Serializable {
    //{
    //    "code": 0,
    //    "data": [{
    //        "plate": "浙AD00819",
    //        "car_type": "E17",
    //        "status": 0
    //    }]
   // {"code":0,"data":[{"apply_type":0,"plate":"浙ARB405","car_type":"ER30","status":1,"apply_time":"2020年12月28日 14:57:06"}]}
    //}
    private String plate, car_type,apply_time,reason;
    private int status,apply_type;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public int getApply_type() {
        return apply_type;
    }

    public void setApply_type(int apply_type) {
        this.apply_type = apply_type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
