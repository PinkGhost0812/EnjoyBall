package com.example.lenovo.enjoyball.entity;

public class UserFans {
    private Integer id;
    private Integer user_id;
    private Integer fans_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getFans_id() {
        return fans_id;
    }

    public void setFans_id(Integer fans_id) {
        this.fans_id = fans_id;
    }

    @Override
    public String toString() {
        return "UserFans{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", fans_id=" + fans_id +
                '}';
    }

    public UserFans(Integer id, Integer user_id, Integer fans_id) {
        this.id = id;
        this.user_id = user_id;
        this.fans_id = fans_id;
    }
}
