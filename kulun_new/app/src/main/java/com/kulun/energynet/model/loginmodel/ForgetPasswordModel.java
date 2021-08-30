package com.kulun.energynet.model.loginmodel;

import java.io.Serializable;

/**
 * created by xuedi on 2019/4/22
 */
public class ForgetPasswordModel implements Serializable {
    //{"code":-1,"msg":"签名异常","success":false}
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
