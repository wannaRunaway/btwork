package com.btkj.millingmachine.model.aliwxpay;

import java.io.Serializable;

/**
 * created by xuedi on 2019/5/24
 */
public class AliWxPayModel implements Serializable {
    private boolean success;
    private int code;
    private String error;
    private AliWxPay data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public AliWxPay getData() {
        return data;
    }

    public void setData(AliWxPay data) {
        this.data = data;
    }
    /**
     * {
     *     "success":true,
     *     "code":200,
     *     "error":"成功",
     *     "data":
     * }
     */
}
