package com.example.lenovo.entity;

public class PYQComment {
    private Integer id;
    private String content;
    private Integer author;
    private Integer belong;
    private String userImg;
    private String userName;

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PYQComment(){}

    @Override
    public String toString() {
        return "PYQComment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", belong=" + belong +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Integer getBelong() {
        return belong;
    }

    public void setBelong(Integer belong) {
        this.belong = belong;
    }

    public PYQComment(Integer id, String content, Integer author, Integer belong) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.belong = belong;
    }
}
