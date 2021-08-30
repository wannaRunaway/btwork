package com.botann.driverclient.model.loginmodel;

import java.io.Serializable;

/**
 * created by xuedi on 2019/4/19
 */
public class MyInviteData implements Serializable {
    /**
     * {
     *      *      *                 "addParentTime":1555638535000,
     *      *      *                 "phone":"13606547694",
     *      *      *                 "name":"136测试司机",
     *      *      *                 "id":10596,
     *      *      *                 "account":"2019041900010596"
     *      totalReward
     *      *      *             }
     */
    private String phone, name , account, totalReward;
    private int id;
    private long addParentTime;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAddParentTime() {
        return addParentTime;
    }

    public void setAddParentTime(long addParentTime) {
        this.addParentTime = addParentTime;
    }

    public String getTotalReward() {
        return totalReward;
    }

    public void setTotalReward(String totalReward) {
        this.totalReward = totalReward;
    }
}
