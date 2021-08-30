package com.kulun.energynet.model;

import java.io.Serializable;
import java.util.List;

public class StationContent implements Serializable {
    //:{"code":0,"data":{"recId":0,"sites":[{"id":99,"type":1,"name":"西溪福堤1号","address":"杭州市文二西路西溪湿地北门福堤1号","phone":"13296723879","contact":"胡志林","channel":15,"status":0,"latitude":30.278,"longitude":120.07,"start_time":"2018-04-12T00:00:48+08:00","end_time":"2018-04-12T23:59:48+08:00","appointment":true,"waiting":0,"distance":6.33,"battery":0},{"id":94,"type":1,"name":"留下","address":"杭州市西湖区留和路129号安能物流","phone":"13296728167","contact":"杨康","channel":192,"status":0,"latitude":30.23132,"longitude":120.05118,"start_time":"2018-04-12T00:00:48+08:00","end_time":"2018-04-12T23:59:48+08:00","appointment":true,"waiting":0,"distance":9.26,"battery":84},{"id":95,"type":0,"name":"电竞小镇站","address":"杭州市下城区中国杭州电竞数娱小镇","phone":"15558160972","contact":"孔浩","channel":60,"status":0,"latitude":30.3390498351,"longitude":120.2066495322,"start_time":"2018-04-12T00:00:48+08:00","end_time":"2018-04-12T23:59:48+08:00","appointment":false,"waiting":0,"distance":10.14,"battery":0}]}}
    private int recId;
    private List<StationInfo> sites;

    public int getRecId() {
        return recId;
    }

    public void setRecId(int recId) {
        this.recId = recId;
    }

    public List<StationInfo> getSites() {
        return sites;
    }

    public void setSites(List<StationInfo> sites) {
        this.sites = sites;
    }
}
