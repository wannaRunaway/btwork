package com.btkj.chongdianbao.model.login;

import java.io.Serializable;

/**
 * created by xuedi on 2019/8/1
 */
public class LoginPasswordModel implements Serializable {
    /**
     * {
     *     "success":true,
     *     "code":200,
     *     "msg":"成功",
     *     "content":
     * }
     */
    private boolean success;
    private int code;
    private String msg;
    private LoginContent content;

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

    public LoginContent getContent() {
        return content;
    }

    public void setContent(LoginContent content) {
        this.content = content;
    }
}
