package com.botann.driverclient.model.chongdian;

import java.io.Serializable;

/**
 * created by xuedi on 2019/7/15
 */
public class ChongdianZhuangInforContent implements Serializable {
    private Connector connector;
    private StationModel station;
    private PolicyModel policy;

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public StationModel getStation() {
        return station;
    }

    public void setStation(StationModel station) {
        this.station = station;
    }

    public PolicyModel getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyModel policy) {
        this.policy = policy;
    }
}
