package com.kulun.energynet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * created by xuedi on 2019/8/26
 */
public class WxPayModel implements Serializable {

    /**
     * code : 200
     * content : {"package":"Sign=WXPay","appid":"wxfeda8ed1ffad13af","sign":"35D38A0F68ECC49B0A62443137AADDB4","partnerid":"1552253491","prepayid":"wx260946363605301a211dcfb11798612400","noncestr":"bmEWkk7rHbPL","timestamp":"1566783996"}
     * msg : 成功
     * success : true
     */

//    private int code;
//    private ContentBean content;
//    private String msg;
//    private boolean success;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public ContentBean getContent() {
//        return content;
//    }
//
//    public void setContent(ContentBean content) {
//        this.content = content;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }

    public class ContentBean implements Serializable{
        /**
         * package : Sign=WXPay
         * appid : wxfeda8ed1ffad13af
         * sign : 35D38A0F68ECC49B0A62443137AADDB4
         * partnerid : 1552253491
         * prepayid : wx260946363605301a211dcfb11798612400
         * noncestr : bmEWkk7rHbPL
         * timestamp : 1566783996
         */

        @SerializedName("package")
        private String packageX;
        private String appid;
        private String sign;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String timestamp;

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
