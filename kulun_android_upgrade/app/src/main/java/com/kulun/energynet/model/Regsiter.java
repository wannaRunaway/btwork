package com.kulun.energynet.model;

import java.io.Serializable;

public class Regsiter implements Serializable {
    // {
    //	"code": 0,
    //	"data": {
    //		"balance": 0,
    //		"phone": "15209603593",
    //		"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MTg0NDQyMTUsImlhdCI6MTYwODQ0NDIxNSwidXNlcmlkIjowLCJ1c2VybmFtZSI6IiJ9.rLyDDRbCc2kjqDflnwmOiZxOdQjW0yS-KPL0YA4WqJw",
    //		"username": ""
    //	}
    //} {
    //	"code": 0,
    //	"message": "用户注册成功，请完善个人信息"
    //}
    public String phone,token,username;
    public double balance;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
