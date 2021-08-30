package com.botann.driverclient.model.huandian;
import com.botann.driverclient.model.huandian.StationContent;

import java.io.Serializable;
/**
 * created by xuedi on 2019/4/24
 */
public class StationlistModel implements Serializable {
    private String msg;
    private boolean success;
    private int code;
    private StationContent content;

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

    public StationContent getContent() {
        return content;
    }

    public void setContent(StationContent content) {
        this.content = content;
    }
}
