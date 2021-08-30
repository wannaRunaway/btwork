package com.btkj.chongdianbao.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/8/7
 */
public class BaseObject implements Serializable {
    /**
     * {
     *     "success":true,
     *     "code":200,
     *     "error":"成功",
     *     "data":null
     * }
     */
    private boolean success;
    private String msg;
    private int code;
    private String content;

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
}
