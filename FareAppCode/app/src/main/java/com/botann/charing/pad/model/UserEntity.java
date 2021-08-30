package com.botann.charing.pad.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by liushanguo on 2017/8/11.
 */

@Entity
public class UserEntity {

    @Id(autoincrement = false)
    @Property(nameInDb = "USER_ID")
    private Long userId = -1L;

    private String  username = "";
    private String  password = "";
    private String  station = "";

    @Generated(hash = 2019234763)
    public UserEntity(Long userId, String username, String password, String station) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.station = station;
    }

    @Generated(hash = 1433178141)
    public UserEntity() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
