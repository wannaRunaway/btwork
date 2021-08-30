package com.kulun.energynet.model;

import java.io.Serializable;

/**
 * created by xuedi on 2018/10/17
 */
public class BindCar implements Serializable{
    /**
     * "bindCarList": [{
     *  * 			"id": 12,
     *  * 			"plate_number": "苏E05EV8",
     *  * 			"status": 1
     *  account_type
     *  *                }]
     */
    private int id, status, account_type, business_type;
    private String plate_number, vin;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }

    public int getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(int business_type) {
        this.business_type = business_type;
    }
}
