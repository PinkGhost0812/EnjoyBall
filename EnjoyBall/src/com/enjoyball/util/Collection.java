package com.enjoyball.util;

import java.io.Serializable;

public class Collection implements Serializable {
    private Integer collection_id;
    private Integer news_id;
    private Integer user_id;

    public Collection() {
    }

    public Integer getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(Integer collection_id) {
        this.collection_id = collection_id;
    }

    public Integer getNews_id() {
        return news_id;
    }

    public void setNews_id(Integer news_id) {
        this.news_id = news_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "collection_id=" + collection_id +
                ", new_id=" + news_id +
                ", user_id=" + user_id +
                '}';
    }

    public Collection(Integer collection_id, Integer news_id, Integer user_id) {
        this.collection_id = collection_id;
        this.news_id = news_id;
        this.user_id = user_id;
    }
}
