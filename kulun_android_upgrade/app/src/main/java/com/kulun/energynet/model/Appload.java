package com.kulun.energynet.model;

import java.io.Serializable;

public class Appload implements Serializable {
    //"Name": "updateContentAndroid",
    //        "Value": "0",
    //        "Remark": "增加了站详情和在线退款功能"
    private String Name,Value,Remark;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
    //响应body: {
    //	"code": 0,
    //	"data": [{
    //		"Name": "updateContentIOS",
    //		"Value": "0",
    //		"Remark": "有新版本APP需要更新"
    //	}, {
    //		"Name": "updateContentAndroid",
    //		"Value": "0",
    //		"Remark": "增加了站详情和在线退款功能"
    //	}, {
    //		"Name": "workTime",
    //		"Value": "0",
    //		"Remark": "09:30~22:00"
    //	}, {
    //		"Name": "workDay",
    //		"Value": "0",
    //		"Remark": "周一到周五"
    //	}, {
    //		"Name": "serviceLine",
    //		"Value": "0",
    //		"Remark": "15757129606"
    //	}, {
    //		"Name": "androidLink",
    //		"Value": "0",
    //		"Remark": "https://botann-app-packages.oss-cn-shanghai.aliyuncs.com/coolene/kulun-1.0.1.apk"
    //	}, {
    //		"Name": "latestVersionIOS",
    //		"Value": "0",
    //		"Remark": "2.3.0"
    //	}, {
    //		"Name": "latestVersionAndroid",
    //		"Value": "0",
    //		"Remark": "2.9"
    //	}, {
    //		"Name": "checkUpdateIOS",
    //		"Value": "0",
    //		"Remark": "0"
    //	}, {
    //		"Name": "checkUpdateAndroid",
    //		"Value": "0",
    //		"Remark": "0"
    //	}, {
    //		"Name": "forceUpdateIOS",
    //		"Value": "0",
    //		"Remark": "0"
    //	}, {
    //		"Name": "forceUpdateAndroid",
    //		"Value": "0",
    //		"Remark": "0" 强制是1  不强制是0
    //	}]
    //}
}
