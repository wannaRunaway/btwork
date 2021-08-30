package com.botann.charing.pad.model.packageactivity;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/2/25
 */
public class PackageActivityContent implements Serializable {
    private List<PackageActivityinfo> activity;
    private List<PackageCarinfo> car;
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<PackageActivityinfo> getActivity() {
        return activity;
    }

    public void setActivity(List<PackageActivityinfo> activity) {
        this.activity = activity;
    }

    public List<PackageCarinfo> getCar() {
        return car;
    }

    public void setCar(List<PackageCarinfo> car) {
        this.car = car;
    }
    /**
     * {
     *      *         "activity":[
     *      *         ],
     *      *         "car":[
     *      *         ],
     *      *         "account":"2019011500000201"
     *      *     },
     */
}
