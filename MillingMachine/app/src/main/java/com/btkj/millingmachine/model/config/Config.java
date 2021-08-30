package com.btkj.millingmachine.model.config;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/5/23
 */
public class Config implements Serializable {
    private int fetchCmdPeriod, isShowDeviceLogo, conStatus, firmwareUpdate;
    private double ricePrice;
    private String logoImgUrl, logoTitle, serviceCall, supportCall, repairCall, deviceLogoImgUrl,
            deviceLogoTitle, startRunTime, endRunTime, wxPlatformUrl, wxMiniprogramUrl;
    private boolean changed, helpChanged, openDoor;
    public boolean isOpenDoor() {
        return openDoor;
    }

    public void setOpenDoor(boolean openDoor) {
        this.openDoor = openDoor;
    }

    //"openDoor":true
    //"startRunTime": "06:00",
    //		"endRunTime": "09:15", "conStatus": 1,
    private List<CompanyHelpList> companyHelpList;

    public List<CompanyHelpList> getCompanyHelpList() {
        return companyHelpList;
    }

    public void setCompanyHelpList(List<CompanyHelpList> companyHelpList) {
        this.companyHelpList = companyHelpList;
    }

    public int getFetchCmdPeriod() {
        return fetchCmdPeriod;
    }

    public void setFetchCmdPeriod(int fetchCmdPeriod) {
        this.fetchCmdPeriod = fetchCmdPeriod;
    }

    public int getIsShowDeviceLogo() {
        return isShowDeviceLogo;
    }

    public void setIsShowDeviceLogo(int isShowDeviceLogo) {
        this.isShowDeviceLogo = isShowDeviceLogo;
    }

    public double getRicePrice() {
        return ricePrice;
    }

    public void setRicePrice(double ricePrice) {
        this.ricePrice = ricePrice;
    }

    public String getLogoImgUrl() {
        return logoImgUrl;
    }

    public void setLogoImgUrl(String logoImgUrl) {
        this.logoImgUrl = logoImgUrl;
    }

    public String getLogoTitle() {
        return logoTitle;
    }

    public void setLogoTitle(String logoTitle) {
        this.logoTitle = logoTitle;
    }

    public String getServiceCall() {
        return serviceCall;
    }

    public void setServiceCall(String serviceCall) {
        this.serviceCall = serviceCall;
    }

    public String getSupportCall() {
        return supportCall;
    }

    public void setSupportCall(String supportCall) {
        this.supportCall = supportCall;
    }

    public String getRepairCall() {
        return repairCall;
    }

    public void setRepairCall(String repairCall) {
        this.repairCall = repairCall;
    }

    public String getDeviceLogoImgUrl() {
        return deviceLogoImgUrl;
    }

    public void setDeviceLogoImgUrl(String deviceLogoImgUrl) {
        this.deviceLogoImgUrl = deviceLogoImgUrl;
    }

    public String getDeviceLogoTitle() {
        return deviceLogoTitle;
    }

    public void setDeviceLogoTitle(String deviceLogoTitle) {
        this.deviceLogoTitle = deviceLogoTitle;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isHelpChanged() {
        return helpChanged;
    }

    public void setHelpChanged(boolean helpChanged) {
        this.helpChanged = helpChanged;
    }

    public int getConStatus() {
        return conStatus;
    }

    public void setConStatus(int conStatus) {
        this.conStatus = conStatus;
    }

    public String getStartRunTime() {
        return startRunTime;
    }

    public void setStartRunTime(String startRunTime) {
        this.startRunTime = startRunTime;
    }

    public String getEndRunTime() {
        return endRunTime;
    }

    public void setEndRunTime(String endRunTime) {
        this.endRunTime = endRunTime;
    }

    public int getFirmwareUpdate() {
        return firmwareUpdate;
    }

    public void setFirmwareUpdate(int firmwareUpdate) {
        this.firmwareUpdate = firmwareUpdate;
    }

    public String getWxPlatformUrl() {
        return wxPlatformUrl;
    }

    public void setWxPlatformUrl(String wxPlatformUrl) {
        this.wxPlatformUrl = wxPlatformUrl;
    }

    public String getWxMiniprogramUrl() {
        return wxMiniprogramUrl;
    }

    public void setWxMiniprogramUrl(String wxMiniprogramUrl) {
        this.wxMiniprogramUrl = wxMiniprogramUrl;
    }

    /**
 *   "fetchCmdPeriod":3000,
 *         "ricePrice":8.9,
 *         "logoImgUrl":"http://faithrent.qiniu.botann.com/1.0.3faithrent_invite.png",
 *         "logoTitle":"众邦碾米销售",
 *         "serviceCall":"13856785678",
 *         "supportCall":"19900001234",
 *         "repairCall":"19900001234",
 *         "isShowDeviceLogo":0,
 *         "deviceLogoImgUrl":"http://faithrent.qiniu.botann.com/1.0.3faithrent_invite.png",
 *         "deviceLogoTitle":"碾米",
 *         "changed":true,
 *         "helpChanged":true
 *     }
 */
}
