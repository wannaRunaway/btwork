package com.botann.charing.pad.model;

/**
 * Created by liushanguo on 2018/4/18.
 */

public class Car {
    private int id;
    private int businessType;
    private String plateNumber;
    private Long plateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Long getPlateDate() {
        return plateDate;
    }

    public void setPlateDate(Long plateDate) {
        this.plateDate = plateDate;
    }
}
