package com.kulun.energynet.model.consume;
import java.io.Serializable;
/**
 * created by xuedi on 2019/7/4
 */
public class ConsumeModel implements Serializable {
    private String msg;
    private boolean success;
    private int code;
    private ConsumeContent content;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ConsumeContent getContent() {
        return content;
    }

    public void setContent(ConsumeContent content) {
        this.content = content;
    }
}
