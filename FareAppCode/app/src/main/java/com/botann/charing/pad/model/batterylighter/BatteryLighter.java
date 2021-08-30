package com.botann.charing.pad.model.batterylighter;

import java.io.Serializable;

/**
 * created by xuedi on 2018/12/3
 */
public class BatteryLighter implements Serializable {
    private String transferSiteName,transferTimeStr,receiveSiteName,carNumber,remark,batteryDetail;
    private int status, id; //0待接受 1已接收
    private long transferTime, receiveTime;
    private boolean isgive = false;

    public boolean isIsgive() {
        return isgive;
    }

    public void setIsgive(boolean isgive) {
        this.isgive = isgive;
    }

    public String getTransferSiteName() {
        return transferSiteName;
    }

    public void setTransferSiteName(String transferSiteName) {
        this.transferSiteName = transferSiteName;
    }

    public String getTransferTimeStr() {
        return transferTimeStr;
    }

    public void setTransferTimeStr(String transferTimeStr) {
        this.transferTimeStr = transferTimeStr;
    }

    public String getReceiveSiteName() {
        return receiveSiteName;
    }

    public void setReceiveSiteName(String receiveSiteName) {
        this.receiveSiteName = receiveSiteName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBatteryDetail() {
        return batteryDetail;
    }

    public void setBatteryDetail(String batteryDetail) {
        this.batteryDetail = batteryDetail;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(long transferTime) {
        this.transferTime = transferTime;
    }
}
