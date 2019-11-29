package com.example.lenovo.entity;

import java.io.Serializable;

public class Collection implements Serializable {
    private Integer id;
    private Integer newsId;
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", newsId=" + newsId +
                ", userId=" + userId +
                '}';
    }

    public Collection(Integer id, Integer newsId, Integer userId) {
        this.id = id;
        this.newsId = newsId;
        this.userId = userId;
    }
}
