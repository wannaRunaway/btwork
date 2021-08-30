package com.btkj.chongdianbao.model.login;

import java.io.Serializable;

/**
 * created by xuedi on 2019/8/1
 */
public class LoginContent implements Serializable {
    /**
     * {
     *      *         "id":9,
     *      *         "name":"",
     *      *         "phone":"18329111248"
     *      *     }
     */
    private int id;
    private String name, phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
