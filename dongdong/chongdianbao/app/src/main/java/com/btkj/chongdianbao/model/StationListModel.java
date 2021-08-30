package com.btkj.chongdianbao.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/8/13
 */
public class StationListModel implements Serializable {

    /**
     * code : 200
     * content : [{"accountMain":null,"address":"杭州市冰江","appointmentBatteryCountMap":{"kwh10":0,"kwh12":0,"kwh15":0},"availBatteryCountMap":{"kwh10":0,"kwh12":0,"kwh15":0},"channelCount":12,"cityId":330100,"companyId":17,"contact":"zwj","createTime":1563187277000,"del":false,"distance":0,"exStationId":0,"exchangeRuleDetail":null,"fullElectBatteryCountMap":{"kwh10":0,"kwh12":0,"kwh15":0},"id":1,"latitude":30.261591,"locLatitude":null,"locLongitude":null,"longitude":120.089918,"name":"下沙站","number":"12wdw","place":"浙江省 杭州市 西湖区","shareRule":null,"status":0,"statusApp":1,"telephone":"13018757865","totalBatteryCount":0,"type":0,"updateTime":1563187277000,"workTime":"09:00-24:00","zoneId":330106},{"accountMain":null,"address":"浙江省杭州市下沙区海达南路与银沙路交叉口东北200米（厉萍粮油调味品附近）","appointmentBatteryCountMap":{"kwh10":0,"kwh12":0,"kwh15":0},"availBatteryCountMap":{"kwh10":5,"kwh12":3,"kwh15":15},"channelCount":0,"cityId":330100,"companyId":17,"contact":"王一波","createTime":1565773482000,"del":false,"distance":0,"exStationId":33,"exchangeRuleDetail":null,"fullElectBatteryCountMap":{"kwh10":5,"kwh12":3,"kwh15":15},"id":14,"latitude":30.303097,"locLatitude":null,"locLongitude":null,"longitude":120.339617,"name":"银沙站","number":"","place":"浙江省 杭州市 滨江区","shareRule":null,"status":0,"statusApp":0,"telephone":"13296727082","totalBatteryCount":144,"type":0,"updateTime":1565773482000,"workTime":"00:00~23:59","zoneId":330108},{"accountMain":null,"address":"杭州市西湖区西溪路830号","appointmentBatteryCountMap":{"kwh10":0,"kwh12":0,"kwh15":0},"availBatteryCountMap":{"kwh10":0,"kwh12":0,"kwh15":0},"channelCount":60,"cityId":330100,"companyId":17,"contact":"姚国华","createTime":1563264809000,"del":false,"distance":0,"exStationId":2,"exchangeRuleDetail":null,"fullElectBatteryCountMap":{"kwh10":0,"kwh12":0,"kwh15":0},"id":2,"latitude":30.261591,"locLatitude":null,"locLongitude":null,"longitude":120.089918,"name":"西站","number":"00002","place":"浙江省 杭州市 西湖区","shareRule":null,"status":0,"statusApp":0,"telephone":"13296721197","totalBatteryCount":60,"type":2,"updateTime":1563264809000,"workTime":"04:01~22:57","zoneId":330106}]
     */

    private int code;
    private List<Station> content;
    private boolean success;
    private String msg;

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

    public List<Station> getContent() {
        return content;
    }

    public void setContent(List<Station> content) {
        this.content = content;
    }
}
