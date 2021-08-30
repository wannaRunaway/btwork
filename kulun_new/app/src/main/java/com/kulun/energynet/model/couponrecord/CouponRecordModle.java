package com.kulun.energynet.model.couponrecord;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/2/19
 */
public class CouponRecordModle implements Serializable {
    private int code;
    private String msg;
    private boolean success;
    private List<CouponData> content;
    /**
     * {
     *     "code":0,
     *     "msg":"成功",
     *     "content":{
     *     },
     *     "success":true
     * }
     */
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<CouponData> getContent() {
        return content;
    }

    public void setContent(List<CouponData> content) {
        this.content = content;
    }
}
