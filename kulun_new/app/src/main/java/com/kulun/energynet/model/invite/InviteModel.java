package com.kulun.energynet.model.invite;
import java.io.Serializable;

/**
 * created by xuedi on 2019/4/19
 */
public class InviteModel implements Serializable {
    /**
     * 	"code": 0,
     * 	"msg": "成功",
     * 	"content":
     * 	"success": true
     */
    private int code;
    private String msg;
    private boolean success;
    private InviteContent content;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public InviteContent getContent() {
        return content;
    }

    public void setContent(InviteContent content) {
        this.content = content;
    }
}
