package com.enjoyball.entity;

import com.jfinal.plugin.activerecord.Model;

public class Collection extends Model<Collection>{
    private Integer collection_id;
    private Integer new_id;
    private Integer user_id;
    
    public static final Collection dao = new Collection().dao();

    public Collection(){}

    public Integer getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(Integer collection_id) {
        this.collection_id = collection_id;
    }

    public Integer getNew_id() {
        return new_id;
    }

    public void setNew_id(Integer new_id) {
        this.new_id = new_id;
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
                ", new_id=" + new_id +
                ", user_id=" + user_id +
                '}';
    }

    public Collection(Integer collection_id, Integer new_id, Integer user_id) {
        this.collection_id = collection_id;
        this.new_id = new_id;
        this.user_id = user_id;
    }
}
