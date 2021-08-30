package com.btkj.millingmachine.model.VipInquire;

import java.io.Serializable;

/**
 * created by xuedi on 2019/5/26
 */
public class VipInquire implements Serializable {
    /**
     *      *         "name":"lsxj",
     *      *         "phone":"18329111248",
     *      *         "balance":"495.55元",
     *      *         "discount":"5折"
     */
    private String name, phone, balance, discount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
