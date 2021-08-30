package com.kulun.energynet.ui.activity.login;

import java.io.Serializable;

/**
 * created by xuedi on 2019/12/25
 */
public class DriverCarInfoModel implements Serializable {

    /**
     * code : 0
     * msg : 您的资料信息已提交，将于1-3个工作日内完成审核，若有疑问请联系888-8888。
     * content : {"id":27,"accountId":365,"status":0,"name":"大宝","identity":"的","businessType":6,"plateNumber":"的","vin":"的","carTypeId":3,"firstMiles":0,"provinceId":330000,"cityId":330100,"photo":"365_1577266982526.jpg;365_1577266984622.jpg;365_1577266986123.jpg;365_1577266987244.jpg","checkUserId":0,"checkReason":"","remark":"首次提交","del":false,"createTime":1577266989000,"updateTime":1577266989000,"phone":null,"cityName":"杭州市","carTypeName":"8849"}
     * success : true
     */

    private int code;
    private String msg;
    private DriverinfoBean content;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DriverinfoBean getContent() {
        return content;
    }

    public void setContent(DriverinfoBean content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
