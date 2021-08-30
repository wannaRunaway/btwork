package com.btkj.chongdianbao.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/8/13
 */
public class CityListModel implements Serializable {

    /**
     * success : true
     * code : 200
     * error : 成功
     * data : [{"id":330100,"name":"杭州市","parentId":330000,"shortName":"杭州","level":"2","cityCode":"0571","zipCode":"310026","mergerName":"中国,浙江省,杭州市","lng":"120.153576","lat":"30.287459","pinyin":"Hangzhou"}]
     */

    private boolean success;
    private int code;
    private String error;
    private List<DataBean> content;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<DataBean> getContent() {
        return content;
    }

    public void setContent(List<DataBean> content) {
        this.content = content;
    }

    public class DataBean implements Serializable {
        /**
         * id : 330100
         * name : 杭州市
         * parentId : 330000
         * shortName : 杭州
         * level : 2
         * cityCode : 0571
         * zipCode : 310026
         * mergerName : 中国,浙江省,杭州市
         * lng : 120.153576
         * lat : 30.287459
         * pinyin : Hangzhou
         */

        private int id;
        private String name;
        private int parentId;
        private String shortName;
        private String level;
        private String cityCode;
        private String zipCode;
        private String mergerName;
        private String lng;
        private String lat;
        private String pinyin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getMergerName() {
            return mergerName;
        }

        public void setMergerName(String mergerName) {
            this.mergerName = mergerName;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }
    }
}
