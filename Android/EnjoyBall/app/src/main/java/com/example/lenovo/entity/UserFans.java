package com.example.lenovo.entity;

<<<<<<< Updated upstream
import java.io.Serializable;

public class UserFans implements Serializable {
    private Integer id;
=======
public class UserFans {
    private Integer user_fansid;
>>>>>>> Stashed changes
    private Integer user_id;
    private Integer fans_id;

    public UserFans(){}

    public Integer getId() {
        return user_fansid;
    }

    public void setId(Integer id) {
        this.user_fansid = id;
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
                "id=" + user_fansid +
                ", user_id=" + user_id +
                ", fans_id=" + fans_id +
                '}';
    }

    public UserFans(Integer id, Integer user_id, Integer fans_id) {
        this.user_fansid = id;
        this.user_id = user_id;
        this.fans_id = fans_id;
    }
}
