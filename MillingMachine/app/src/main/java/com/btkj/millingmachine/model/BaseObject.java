package com.btkj.millingmachine.model;
import java.io.Serializable;
/**
 * created by xuedi on 2019/5/23
 */
public class BaseObject implements Serializable {
    private int code;
    private boolean success;
    private String error;
    private String data;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
