package com.kulun.kulunenergy.modelnew;

import java.io.Serializable;
import java.util.List;

public class CarModel implements Serializable {
    /**
     * total : 0
     * data : [{"id":102,"type":0,"accountId":394,"status":0,"name":"薛地","identity":"642221199511200691","businessType":6,"plateNumber":"苏E05EV8","vin":"ddddd","carTypeId":23,"firstMiles":20,"provinceId":460000,"cityId":460100,"photo":"394_1584432576651.jpg;394_1584432578097.jpg;394_1584432579288.jpg","checkUserId":0,"checkReason":"","remark":"首次提交","del":false,"createTime":1584432579000,"updateTime":1584432579000,"settleType":0,"role":1,"oldPlateNumber":"","phone":null,"cityName":"海口市","carTypeName":"E11K"}]
     */

    private int total;
    private List<Car> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Car> getData() {
        return data;
    }

    public void setData(List<Car> data) {
        this.data = data;
    }
}
