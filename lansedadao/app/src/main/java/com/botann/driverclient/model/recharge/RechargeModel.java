package com.botann.driverclient.model.recharge;

import java.io.Serializable;

/**
 * created by xuedi on 2019/7/4
 */
public class RechargeModel implements Serializable {
    private String msg;
    private boolean success;
    private int code;
    private RechargeContent content;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RechargeContent getContent() {
        return content;
    }

    public void setContent(RechargeContent content) {
        this.content = content;
    }
}
