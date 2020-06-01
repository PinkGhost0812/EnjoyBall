package com.example.lenovo.entity;

import java.util.List;

public class PYQ {
    private Integer id;
    private Integer UserId;
    private String time;
    private Integer good;
    private Integer number;
    private List<PYQComment> comments;

    public PYQ(){}

    public List<PYQComment> getComments() {
        return comments;
    }

    public void setComments(List<PYQComment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "PYQ{" +
                "id=" + id +
                ", UserId=" + UserId +
                ", time='" + time + '\'' +
                ", good=" + good +
                ", number=" + number +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public PYQ(Integer id, Integer userId, String time, Integer good, Integer number, List<PYQComment> comments) {
        this.id = id;
        UserId = userId;
        this.time = time;
        this.good = good;
        this.number = number;
        this.comments = comments;
    }
}
