package com.kulun.energynet.model;

import java.io.Serializable;

public class Message implements Serializable {
    // "msg_id": 7395936,
    //        "type": 1,
    //        "content": "【换电提醒】尊敬的车主：您尾号B104车辆于2020-12-23 13:49:03，在西斗门换电，计费里程120公里，换电账户消费40.00元，优惠券消费2.00元，换电账户余额272.83元",
    //        "reader": true,
    //        "is_top": false,
    //        "create_time": "2020年12月23日"
    private int msg_id,type;
    private String content,create_time;
    private boolean reader, is_top;

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public boolean isReader() {
        return reader;
    }

    public void setReader(boolean reader) {
        this.reader = reader;
    }

    public boolean isIs_top() {
        return is_top;
    }

    public void setIs_top(boolean is_top) {
        this.is_top = is_top;
    }
}
