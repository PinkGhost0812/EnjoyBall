package com.example.lenovo.entity;

public class PYQComment {
    private Integer id;
    private String content;
    private Integer author;
    private Integer belong;

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
