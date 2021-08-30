package com.botann.driverclient.model.couponrecord;

import java.io.Serializable;

/**
 * created by xuedi on 2019/2/19
 */
public class CouponData implements Serializable {
    private int id, type, accountId, amount, cashAmount, used, status, carTypeId;
    private String couponName, remark, carType;
    private long beginDate, expireDate, createTime;
    private boolean del;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(int cashAmount) {
        this.cashAmount = cashAmount;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(int carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }
    /**
     *             {
     *                 "id":2624,
     *                 "type":0,
     *                 "couponName":"你充值，我送礼，好礼不停送到底！",
     *                 "accountId":174,
     *                 "amount":120,
     *                 "cashAmount":0,
     *                 "used":115,
     *                 "status":1,
     *                 "beginDate":1544601223000,
     *                 "expireDate":1547193223000,
     *                 "remark":"",
     *                 "createTime":1544601218000,
     *                 "del":false,
     *                 "carTypeId":1,
     *                 "carType":"ER30"
     *             }
     */
}
