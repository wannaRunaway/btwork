package com.kulun.kulunenergy.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/9/2
 */
public class InviteListModel implements Serializable {
    /**
     * {
     *     "code":200,
     *     "content":[
     *
     *     ],
     *     "msg":"成功",
     *     "success":true
     * }
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

    public class ContentBean implements Serializable{
        /**
         * {
         *      *             "reward":0,//奖励，double
         *      *             "name":"dqn",//司机姓名
         *      *             "id":16,// 司机id
         *      *             "inviteTime":1563330431000// 邀请时间
         *      *         }
         */
        private double reward;
        private String name;
        private int id;
        private long inviteTime;

        public double getReward() {
            return reward;
        }

        public void setReward(double reward) {
            this.reward = reward;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getInviteTime() {
            return inviteTime;
        }

        public void setInviteTime(long inviteTime) {
            this.inviteTime = inviteTime;
        }
    }
}
