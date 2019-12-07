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
<<<<<<< Updated upstream
=======

    public static final int MESSAGE_RECEIVE_SUCCESS=1;

    //当前用户头像存储地址
    public static final String HEADPORTRAIT_URL= "/data/user/0/com.example.lenovo.enjoyball/files/HeadPortrait.jpg";
>>>>>>> Stashed changes
}
