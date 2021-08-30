package com.botann.driverclient.model.loginmodel;

import java.io.Serializable;

/**
 * created by xuedi on 2019/4/22
 */
public class VerificationLoginModel implements Serializable {
    private int code;
    private boolean success;
    private String msg;
    private VerificationContent content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public VerificationContent getContent() {
        return content;
    }

    public void setContent(VerificationContent content) {
        this.content = content;
    }
}
