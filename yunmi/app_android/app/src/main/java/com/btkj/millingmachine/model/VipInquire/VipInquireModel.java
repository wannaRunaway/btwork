package com.btkj.millingmachine.model.VipInquire;

import java.io.Serializable;

/**
 * created by xuedi on 2019/5/26
 */
public class VipInquireModel implements Serializable {
    private boolean success;
    private int code;
    private String error;
    private VipInquire data;

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

    public VipInquire getData() {
        return data;
    }

    public void setData(VipInquire data) {
        this.data = data;
    }
    /**
     * {
     *     "success":true,
     *     "code":200,
     *     "error":"成功",
     *     "data":{
     *     }
     * }
     */
}
