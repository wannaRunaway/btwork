package com.kulun.energynet.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/2/18
 */
public class RecordData implements Serializable {
    /**
     *  "activity_recore_id":331,
     *      *      *      *                 "activity_time":1547536166119,
     *      *      *      *                 "activity_name":"你充值，我送礼，好礼不停送到底！",
     *      *      *      *                 "package_end_time":null,
     *      *      *      *                 "car_plate_number":null,
     *      *      *      *                 "gift_type":1,
     *      *      *      *                 "site_name":null,
     *      *      *      *                 "package_month":null,
     *      *      *      *                 "change_balance":"100.00",
     *      *      *      *                 "activity_type":1,
     *      *      *      *                 "account_name":"aaa",
     *      *      *      *                 "pay_type":1,
     *      *      *      *                 "package_start_time":null
     *       gift_left
     */
    private long activity_time;
    private int gift_type,activity_type,pay_type, activity_recore_id, gift_left;
    private String activity_name, package_end_time, car_plate_number,site_name,package_month,change_balance,account_name,package_start_time;

    public int getGift_left() {
        return gift_left;
    }

    public void setGift_left(int gift_left) {
        this.gift_left = gift_left;
    }

    public long getActivity_time() {
        return activity_time;
    }

    public void setActivity_time(long activity_time) {
        this.activity_time = activity_time;
    }

    public int getGift_type() {
        return gift_type;
    }

    public void setGift_type(int gift_type) {
        this.gift_type = gift_type;
    }

    public int getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(int activity_type) {
        this.activity_type = activity_type;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getPackage_end_time() {
        return package_end_time;
    }

    public void setPackage_end_time(String package_end_time) {
        this.package_end_time = package_end_time;
    }

    public String getCar_plate_number() {
        return car_plate_number;
    }

    public void setCar_plate_number(String car_plate_number) {
        this.car_plate_number = car_plate_number;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getPackage_month() {
        return package_month;
    }

    public void setPackage_month(String package_month) {
        this.package_month = package_month;
    }

    public String getChange_balance() {
        return change_balance;
    }

    public void setChange_balance(String change_balance) {
        this.change_balance = change_balance;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getPackage_start_time() {
        return package_start_time;
    }

    public void setPackage_start_time(String package_start_time) {
        this.package_start_time = package_start_time;
    }

    public int getActivity_recore_id() {
        return activity_recore_id;
    }

    public void setActivity_recore_id(int activity_recore_id) {
        this.activity_recore_id = activity_recore_id;
    }
}
