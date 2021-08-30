package com.botann.charing.pad.activity.ExchangeBattery;

import java.io.Serializable;

/**
 * created by xuedi on 2020/1/7
 */
public class AccountByPlateModelContent implements Serializable {
    /**
     * {
     *             "id":195,// id
     *             "name":"打卡b",// 司机姓名
     *             "account":"2018122500000195",// 司机账户
     *             "phone":"13569875621"// 司机电话号码
     *         }
     */
    private int id;
    private String name, account, phone;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
