package com.example.lenovo.entity;

import java.io.Serializable;
import java.util.Date;

public class Contest implements Serializable {
    private Integer id;
    private Integer home;
    private Integer away;
    private Date time;
    private String place;
    private Integer cla;
    private Integer grade;
    private Integer status;
    private String result;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHome() {
        return home;
    }

    public void setHome(Integer home) {
        this.home = home;
    }

    public Integer getAway() {
        return away;
    }

    public void setAway(Integer away) {
        this.away = away;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getCla() {
        return cla;
    }

    public void setCla(Integer cla) {
        this.cla = cla;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "id=" + id +
                ", home=" + home +
                ", away=" + away +
                ", time=" + time +
                ", place='" + place + '\'' +
                ", cla=" + cla +
                ", grade=" + grade +
                ", status=" + status +
                ", result='" + result + '\'' +
                '}';
    }

    public Contest(Integer id, Integer home, Integer away, Date time, String place, Integer cla, Integer grade, Integer status, String result) {
        this.id = id;
        this.home = home;
        this.away = away;
        this.time = time;
        this.place = place;
        this.cla = cla;
        this.grade = grade;
        this.status = status;
        this.result = result;
    }
}
