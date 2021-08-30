package com.btkj.millingmachine.model.uploadconfig;

import java.io.Serializable;

/**
 * created by xuedi on 2019/5/23
 */
public class UploadConfigModel implements Serializable {
    private int code;
    private boolean success;
    private String error;
    private UploadConfig data;
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
    public UploadConfig getData() {
        return data;
    }
    public void setData(UploadConfig data) {
        this.data = data;
    }
}
