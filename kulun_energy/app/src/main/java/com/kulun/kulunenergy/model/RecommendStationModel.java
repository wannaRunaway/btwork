package com.kulun.kulunenergy.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/8/19
 */
public class RecommendStationModel implements Serializable {

    /**
     * code : 200
     * content : [{"accountMain":null,"address":"杭州市西湖区留和路129号安能物流","appointmentBatteryCountMap":{"kwh12":0},"availBatteryCountMap":{"kwh12":32},"channelCount":92,"cityId":330100,"companyId":17,"contact":"杨康","createTime":1563330068000,"del":false,"distance":9255,"exStationId":3,"exchangeRuleDetail":null,"fullElectBatteryCountMap":{"kwh12":32},"id":4,"latitude":30.23132,"locLatitude":null,"locLongitude":null,"longitude":120.05118,"name":"留下","number":"00003","place":"浙江省 杭州市 西湖区","shareRule":null,"status":0,"statusApp":0,"telephone":"13296728167","totalBatteryCount":35,"type":0,"updateTime":1563330068000,"workTime":"00:00~23:59","zoneId":330106}]
     * msg : 成功
     * success : true
     */

    private int code;
    private String msg;
    private boolean success;
    private List<Station> content;

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

    public List<Station> getContent() {
        return content;
    }

    public void setContent(List<Station> content) {
        this.content = content;
    }
}
