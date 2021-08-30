package com.kulun.energynet.model.loginmodel;
import java.io.Serializable;
/**
 * created by xuedi on 2019/4/22
 */
public class SmscodeModel implements Serializable {
    //json:{"code":-1,"msg":"同一手机号验证码短信发送超出5条","content":null,"success":false}
    private int code;
    private String msg, content;
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
