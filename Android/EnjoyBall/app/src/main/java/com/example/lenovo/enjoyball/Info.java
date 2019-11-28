package com.example.lenovo.enjoyball;

import com.example.lenovo.entity.*;

public class Info {
    public static final String BASE_URL = "http://10.7.88.233:8080/EnjoyBallServer/";

    //当前登录的用户的信息
    private User user;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
