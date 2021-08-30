package com.btkj.chongdianbao.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/9/6
 */
public class ActivityModel {

    /**
     * code : 200
     * content : [{"activityNo":193,"adminUserId":1,"cityId":0,"conditions":[],"createTime":1567495401000,"del":0,"detail":"sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss","endTime":1570636800000,"icon":"","id":194,"level":0,"name":"test","open":1,"remark":"","rewards":[],"startTime":1567353600000,"status":1,"type":0},{"activityNo":189,"adminUserId":1,"cityId":0,"conditions":[],"createTime":1567478790000,"del":0,"detail":"","endTime":1569772800000,"icon":"","id":189,"level":0,"name":"邀请绑定送九月份","open":1,"remark":"","rewards":[],"startTime":1567440000000,"status":1,"type":1}]
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
         * activityNo : 193
         * adminUserId : 1
         * cityId : 0
         * conditions : []
         * createTime : 1567495401000
         * del : 0
         * detail : sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss
         * endTime : 1570636800000
         * icon :
         * id : 194
         * level : 0
         * name : test
         * open : 1
         * remark :
         * rewards : []
         * startTime : 1567353600000
         * status : 1
         * type : 0
         */

        private int activityNo;
        private int adminUserId;
        private int cityId;
        private long createTime;
        private int del;
        private String detail;
        private long endTime;
        private String icon;
        private int id;
        private int level;
        private String name;
        private int open;
        private String remark;
        private long startTime;
        private int status;
        private int type;
        private List<?> conditions;
        private List<?> rewards;

        public int getActivityNo() {
            return activityNo;
        }

        public void setActivityNo(int activityNo) {
            this.activityNo = activityNo;
        }

        public int getAdminUserId() {
            return adminUserId;
        }

        public void setAdminUserId(int adminUserId) {
            this.adminUserId = adminUserId;
        }

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getDel() {
            return del;
        }

        public void setDel(int del) {
            this.del = del;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
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

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOpen() {
            return open;
        }

        public void setOpen(int open) {
            this.open = open;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<?> getConditions() {
            return conditions;
        }

        public void setConditions(List<?> conditions) {
            this.conditions = conditions;
        }

        public List<?> getRewards() {
            return rewards;
        }

        public void setRewards(List<?> rewards) {
            this.rewards = rewards;
        }
    }
}
