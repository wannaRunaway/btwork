package com.btkj.millingmachine.model.vippay;

import java.io.Serializable;

/**
 * created by xuedi on 2019/5/24
 */
public class VipPay implements Serializable {
    /**
     * {
     *      *         "serialNo":"RBOOXJ1558428014210",
     *      *         "amount":"4.45",
     *      *         "balance":"495.55",
     *      *         "discont":"5",
     *      *         "member":true
     *      *     }
     */
    private String serialNo, amount, balance, discont;
    private boolean member;
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }
    public String getDiscont() {
        return discont;
    }
    public void setDiscont(String discont) {
        this.discont = discont;
    }
    public boolean isMember() {
        return member;
    }
    public void setMember(boolean member) {
        this.member = member;
    }
}
