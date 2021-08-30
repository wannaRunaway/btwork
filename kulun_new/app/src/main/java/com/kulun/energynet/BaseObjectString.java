package com.kulun.energynet;

import java.io.Serializable;

/**
 * created by xuedi on 2019/11/27
 */
public class BaseObjectString implements Serializable {
    /**
     * code : 0
     * msg : 成功
     * content : null
     * success : true
     */

    private int code;
    private String msg;
    private String content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
