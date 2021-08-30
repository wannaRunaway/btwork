package com.btkj.chongdianbao.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/9/6
 */
public class MessageModel {

    /**
     * code : 200
     * content : [{"adminUserId":1,"content":"vvvvvvvvvvvvv","contentType":0,"createTime":1567402365000,"del":false,"icon":"","id":10,"msgType":0,"remark":"0","scheme":"","summary":"0","target":0,"targetId":0,"title":"","top":0},{"adminUserId":1,"content":"cccccccccccc","contentType":0,"createTime":1567401930000,"del":false,"icon":"","id":9,"msgType":0,"remark":"0","scheme":"","summary":"0","target":0,"targetId":0,"title":"","top":0}]
     * msg : 成功
     * success : true
     */

    private int code;
    private String msg;
    private boolean success;
    private List<ContentBean> content;

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

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public class ContentBean implements Serializable {
        /**
         * adminUserId : 1
         * content : vvvvvvvvvvvvv
         * contentType : 0
         * createTime : 1567402365000
         * del : false
         * icon :
         * id : 10
         * msgType : 0
         * remark : 0
         * scheme :
         * summary : 0
         * target : 0
         * targetId : 0
         * title :
         * top : 0
         */

        private int adminUserId;
        private String content;
        private int contentType;
        private long createTime;
        private boolean del;
        private String icon;
        private int id;
        private int msgType;
        private String remark;
        private String scheme;
        private String summary;
        private int target;
        private int targetId;
        private String title;
        private int top;

        public int getAdminUserId() {
            return adminUserId;
        }

        public void setAdminUserId(int adminUserId) {
            this.adminUserId = adminUserId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getContentType() {
            return contentType;
        }

        public void setContentType(int contentType) {
            this.contentType = contentType;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public boolean isDel() {
            return del;
        }

        public void setDel(boolean del) {
            this.del = del;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public int getTargetId() {
            return targetId;
        }

        public void setTargetId(int targetId) {
            this.targetId = targetId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }
    }
}
