package com.botann.driverclient.model.loginmodel;

import java.io.Serializable;

/**
 * created by xuedi on 2019/4/24
 */
public class CityNametoIdModel implements Serializable {
    private int code;
    private boolean success;
    private String msg;
    private CityNametoIdContent content;

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

    public CityNametoIdContent getContent() {
        return content;
    }

    public void setContent(CityNametoIdContent content) {
        this.content = content;
    }
}
