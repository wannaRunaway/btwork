package com.btkj.millingmachine.model.config;

import java.io.Serializable;

/**
 * created by xuedi on 2019/5/23
 */
public class ConfigModel implements Serializable {
    private int code;
    private String error;
    private boolean success;
    private Config data;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Config getData() {
        return data;
    }

    public void setData(Config data) {
        this.data = data;
    }
}
