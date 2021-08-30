package com.kulun.energynet.model;

import java.io.Serializable;

/**
 * created by xuedi on 2018/11/12
 */
public class CustomerServicebean implements Serializable {
    /**
     * {
     *   "code": 0,
     *   "msg": "成功",
     *   "content": [
     *     {
     *       "id": 3,
     *       "sameId": 2,
     *       "siteCompanyId": 2,
     *       "cityId": 110000,
     *       "description": "北京客服",
     *       "phone": "18000000000",
     *       "qq": "",
     *       "remark": "北京客服电话",
     *       "createBy": "",
     *       "createTime": 1541567357000,
     *       "del": 0,
     *       "cityName": "北京",
     *       "siteCompanyName": "耀顶"
     *       workTime
     *     }
     *   ],
     *   "success": true
     * }
     */
    private String description, phone, qq, remark, createBy, cityName, siteCompanyName, workTime;
    private int id, sameId, siteCompanyId, cityId, del;
    private long createTime;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSiteCompanyName() {
        return siteCompanyName;
    }

    public void setSiteCompanyName(String siteCompanyName) {
        this.siteCompanyName = siteCompanyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSameId() {
        return sameId;
    }

    public void setSameId(int sameId) {
        this.sameId = sameId;
    }

    public int getSiteCompanyId() {
        return siteCompanyId;
    }

    public void setSiteCompanyId(int siteCompanyId) {
        this.siteCompanyId = siteCompanyId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }
}
