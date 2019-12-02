package com.example.lenovo.entity;

import java.io.Serializable;
import java.util.Date;

<<<<<<< Updated upstream
public class Comment implements Serializable {
    private Integer id;
    private Integer author;
    private Integer cla;
    private String content;
    private Integer likeNum;
    private Date time;
    private Integer belone;
=======
public class Comment {
    private Integer comment_id;
    private Integer comment_author;
    private Integer comment_class;
    private String comment_content;
    private Integer comment_likenum;
    private Date comment_time;
    private Integer comment_belone;
>>>>>>> Stashed changes

    public Comment(){}

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public Integer getComment_author() {
        return comment_author;
    }

    public void setComment_author(Integer comment_author) {
        this.comment_author = comment_author;
    }

    public Integer getComment_class() {
        return comment_class;
    }

    public void setComment_class(Integer comment_class) {
        this.comment_class = comment_class;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public Integer getComment_likenum() {
        return comment_likenum;
    }

    public void setComment_likenum(Integer comment_likenum) {
        this.comment_likenum = comment_likenum;
    }

    public Date getComment_time() {
        return comment_time;
    }

    public void setComment_time(Date comment_time) {
        this.comment_time = comment_time;
    }

    public Integer getComment_belone() {
        return comment_belone;
    }

    public void setComment_belone(Integer comment_belone) {
        this.comment_belone = comment_belone;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment_id=" + comment_id +
                ", comment_author=" + comment_author +
                ", comment_class=" + comment_class +
                ", comment_content='" + comment_content + '\'' +
                ", comment_likeNum=" + comment_likenum +
                ", comment_time=" + comment_time +
                ", comment_belone=" + comment_belone +
                '}';
    }

    public Comment(Integer comment_id, Integer comment_author, Integer comment_class, String comment_content, Integer comment_likenum, Date comment_time, Integer comment_belone) {
        this.comment_id = comment_id;
        this.comment_author = comment_author;
        this.comment_class = comment_class;
        this.comment_content = comment_content;
        this.comment_likenum = comment_likenum;
        this.comment_time = comment_time;
        this.comment_belone = comment_belone;
    }
}
