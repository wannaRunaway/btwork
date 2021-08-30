package com.botann.driverclient.model;

import com.botann.driverclient.ui.activity.login.DriverCarInfoModel;
import com.botann.driverclient.ui.activity.login.DriverinfoBean;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2020/3/17
 */
public class MyApplyModel implements Serializable{

    /**
     * code : 0
     * msg : 成功
     * content : {"total":0,"data":[{"id":102,"type":0,"accountId":394,"status":0,"name":"薛地","identity":"642221199511200691","businessType":6,"plateNumber":"苏E05EV8","vin":"ddddd","carTypeId":23,"firstMiles":20,"provinceId":460000,"cityId":460100,"photo":"394_1584432576651.jpg;394_1584432578097.jpg;394_1584432579288.jpg","checkUserId":0,"checkReason":"","remark":"首次提交","del":false,"createTime":1584432579000,"updateTime":1584432579000,"settleType":0,"role":1,"oldPlateNumber":"","phone":null,"cityName":"海口市","carTypeName":"E11K"}]}
     * success : true
     */

    private int code;
    private String msg;
    private ContentBean content;
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

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public class ContentBean implements Serializable{
        /**
         * total : 0
         * data : [{"id":102,"type":0,"accountId":394,"status":0,"name":"薛地","identity":"642221199511200691","businessType":6,"plateNumber":"苏E05EV8","vin":"ddddd","carTypeId":23,"firstMiles":20,"provinceId":460000,"cityId":460100,"photo":"394_1584432576651.jpg;394_1584432578097.jpg;394_1584432579288.jpg","checkUserId":0,"checkReason":"","remark":"首次提交","del":false,"createTime":1584432579000,"updateTime":1584432579000,"settleType":0,"role":1,"oldPlateNumber":"","phone":null,"cityName":"海口市","carTypeName":"E11K"}]
         */

        private int total;
        private List<DriverinfoBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DriverinfoBean> getData() {
            return data;
        }

        public void setData(List<DriverinfoBean> data) {
            this.data = data;
        }
    }
}
