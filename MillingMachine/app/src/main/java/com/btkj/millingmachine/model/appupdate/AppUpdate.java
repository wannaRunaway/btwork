package com.btkj.millingmachine.model.appupdate;

import java.io.Serializable;

/**
 * created by xuedi on 2019/7/25
 */
public class AppUpdate implements Serializable {
    private boolean success;
    private int code;
    private String error;
    private UpdateData data;

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

    public UpdateData getData() {
        return data;
    }

    public void setData(UpdateData data) {
        this.data = data;
    }
    /**
 * {    
 * 	"success": true,
 * 	    "code": 200,
 * 	    "error": "成功",
 * 	    "data":
 * }
 */
}
