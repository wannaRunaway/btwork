package com.kulun.energynet.model.chexing;

import java.io.Serializable;

/**
 * created by xuedi on 2019/7/1
 */
public class BindCarModel implements Serializable {
    /**
     * 	"code": 0,
     * 	"msg": "成功",
     * 	"success": true
     */
    private int code;
    private String msg;
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
