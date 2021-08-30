package com.kulun.energynet.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/9/29
 */
public class BaseObject implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * content : null
     * success : true
     */

    private int code;
    private String msg;
    private Object content;
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

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
