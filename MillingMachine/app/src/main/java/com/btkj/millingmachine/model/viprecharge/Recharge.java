package com.btkj.millingmachine.model.viprecharge;

import java.io.Serializable;

/**
 * created by xuedi on 2019/5/24
 */
public class Recharge implements Serializable {
    /**
     * {
     *      *         "body":"weixin://wxpay/bizpayurl?pr=KB6kNQr",
     *      *         "serialNo":"RZPUNL1558427953032"
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
