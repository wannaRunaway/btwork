package com.btkj.millingmachine.model.aliwxpay;

import java.io.Serializable;

/**
 * created by xuedi on 2019/5/24
 */
public class AliWxPay implements Serializable {
    /**
     * {
     *      *         "body":"weixin://wxpay/bizpayurl?pr=M03YPLP",
     *      *         "serialNo":"CJPGLG1558421906687"
     *      *     }
     */
    private String body, serialNo;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
