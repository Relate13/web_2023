package com.example.nju_tube;

import android.app.Application;

public class NJUTube extends Application {
    // 用户token 用户登录后修改为服务端发回的用户token
    private String token = "Invalid Token";
    private int userId = 0;
    private String userName = "";

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
