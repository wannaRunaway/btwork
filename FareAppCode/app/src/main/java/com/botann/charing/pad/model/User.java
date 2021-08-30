package com.botann.charing.pad.model;


import java.util.List;

import com.botann.charing.pad.base.GreenDaoHelper;

/**
 * Created by liushanguo on 2017/8/11.
 */

public class User {

    private Integer id = -1;
    private String  username = "";
    private String  password = "";
    private String  name;
    private String  phone;
    private Integer roleId;
    private Integer companyId;

    private Integer userId;
    private String station;
    private Integer stationId;


    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public User(){
        try {
            List<UserEntity> users = GreenDaoHelper.getInstance().queryAll(UserEntity.class);
            if (users != null && users.size()>0) {
                UserEntity user =  users.get(users.size()-1);
                this.id = user.getUserId().intValue();
                this.username = user.getUsername();
                this.password = user.getPassword();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void update(){
        GreenDaoHelper.getInstance().singleUpdate(getUserEntity(null));
    }


    public void saveOrUpdateSelf(){
        try {
            UserEntity oldUser = null;
            GreenDaoHelper.getInstance().deleteAll(UserEntity.class);
//            List<UserEntity> users = GreenDaoHelper.getInstance().queryAll(UserEntity.class);
//            for (UserEntity userEntity: users){
//                if (userEntity.getUserId() == Long.valueOf(id)) {
//                    oldUser = userEntity;
//                }
//                GreenDaoHelper.getInstance().deleteAll(UserEntity.class);
//            }
//            if (oldUser == null) {
                oldUser = getUserEntity(oldUser);
                GreenDaoHelper.getInstance().singleInsert(oldUser);
//            } else {
//                oldUser = getUserEntity(oldUser);
//                GreenDaoHelper.getInstance().singleUpdate(oldUser);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UserEntity getUserEntity(UserEntity userEntity){
        if (userEntity == null) userEntity = new UserEntity();
        userEntity.setUserId(Long.valueOf(id));
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        return userEntity;
    }



    public void saveOrUpdateUser(Integer userId, String username, String password, String station) {
        this.id = userId;
        this.username = username;
        this.password = password;
        saveOrUpdateSelf();
    }

    public void deleteSelf(){
        GreenDaoHelper.getInstance().deleteByKey(UserEntity.class,Long.valueOf(id));
    }

    /**
     * 单例
     */
    private static User instance;
    public static User shared(){
        if (instance == null) instance = new User();
        return instance;
    }

    public boolean isLogin() {
        return getId()!= null && getId()>0 && !password.isEmpty();
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
