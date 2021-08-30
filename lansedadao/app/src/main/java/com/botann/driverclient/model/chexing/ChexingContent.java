package com.botann.driverclient.model.chexing;

import java.io.Serializable;

/**
 * created by xuedi on 2019/7/1
 */
public class ChexingContent implements Serializable {
    /**
     * 返回：数组
     *  字段：
     *   `id` int(11) NOT NULL AUTO_INCREMENT,
     *   `name` varchar(50) NOT NULL DEFAULT '' COMMENT 'ER30等',
     *   `battery_count` int(2) NOT NULL DEFAULT '0' COMMENT '电池数量',
     *   `seat_count` int(2) NOT NULL DEFAULT '0' COMMENT '座位数',
     *   `price` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '工人换电价(元)',
     *   `create_time` datetime NOT NULL COMMENT '创建时间',
     *   `del` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除位 0未删除 1 已删除',
     */
    private int id, battery_count, seat_count;
    private String name;
    private double price;
    private long create_time;
    private boolean del;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBattery_count() {
        return battery_count;
    }

    public void setBattery_count(int battery_count) {
        this.battery_count = battery_count;
    }

    public int getSeat_count() {
        return seat_count;
    }

    public void setSeat_count(int seat_count) {
        this.seat_count = seat_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }
}
