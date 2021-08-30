package com.kulun.kulunenergy.modelnew;

import java.io.Serializable;

public class Car implements Serializable {
    /**
     * id : 27
     * accountId : 365
     * status : 0
     * name : 大宝
     * identity : 的
     * businessType : 6
     * plateNumber : 的
     * vin : 的
     * carTypeId : 3
     * firstMiles : 0
     * provinceId : 330000
     * cityId : 330100
     * photo : 365_1577266982526.jpg;365_1577266984622.jpg;365_1577266986123.jpg;365_1577266987244.jpg
     * checkUserId : 0
     * checkReason :
     * remark : 首次提交
     * del : false
     * createTime : 1577266989000
     * updateTime : 1577266989000
     * phone : null
     * cityName : 杭州市
     * carTypeName : 8849
     */

    private int id;
    private int accountId;
    private int status;
    private String name;
    private String identity;
    private int businessType;
    private String plateNumber;
    private String vin;
    private int carTypeId;
    private int firstMiles;
    private int provinceId;
    private int cityId;
    private String photo;
    private int checkUserId;
    private String checkReason;
    private String remark;
    private boolean del;
    private long createTime;
    private long updateTime;
    private Object phone;
    private String cityName;
    private String carTypeName;
    /**
     * type : 0
     * settleType : 0
     * role : 1
     * oldPlateNumber :
     * phone : null
     */

    private int type;
    private int settleType;
    private int role;
    private String oldPlateNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
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

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(int carTypeId) {
        this.carTypeId = carTypeId;
    }

    public int getFirstMiles() {
        return firstMiles;
    }

    public void setFirstMiles(int firstMiles) {
        this.firstMiles = firstMiles;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(int checkUserId) {
        this.checkUserId = checkUserId;
    }

    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSettleType() {
        return settleType;
    }

    public void setSettleType(int settleType) {
        this.settleType = settleType;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getOldPlateNumber() {
        return oldPlateNumber;
    }

    public void setOldPlateNumber(String oldPlateNumber) {
        this.oldPlateNumber = oldPlateNumber;
    }
}
