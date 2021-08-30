package com.botann.driverclient.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/10/29
 */
public class MyObject implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * content : null
     * success : true
     */

    private int code;
    private String msg;
    private int content;
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

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}