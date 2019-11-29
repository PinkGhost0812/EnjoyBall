package com.example.lenovo.entity;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    private Integer id;
    private Integer author;
    private Integer cla;
    private String content;
    private Integer likeNum;
    private Date time;
    private Integer belone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Integer getCla() {
        return cla;
    }

    public void setCla(Integer cla) {
        this.cla = cla;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getBelone() {
        return belone;
    }

    public void setBelone(Integer belone) {
        this.belone = belone;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", author=" + author +
                ", cla=" + cla +
                ", content='" + content + '\'' +
                ", likeNum=" + likeNum +
                ", time=" + time +
                ", belone=" + belone +
                '}';
    }

    public Comment(Integer id, Integer author, Integer cla, String content, Integer likeNum, Date time, Integer belone) {
        this.id = id;
        this.author = author;
        this.cla = cla;
        this.content = content;
        this.likeNum = likeNum;
        this.time = time;
        this.belone = belone;
    }
}
