package com.example.lenovo.entity;

import java.util.Date;

public class DemandInfo {
    private Integer id;
    private Integer userId;
    private Date time;
    private Integer cla;
    private String place;
    private Integer visibility;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getCla() {
        return cla;
    }

    public void setCla(Integer cla) {
        this.cla = cla;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DemandInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", time=" + time +
                ", cla=" + cla +
                ", place='" + place + '\'' +
                ", visibility=" + visibility +
                ", description='" + description + '\'' +
                '}';
    }

    public DemandInfo(Integer id, Integer userId, Date time, Integer cla, String place, Integer visibility, String description) {
        this.id = id;
        this.userId = userId;
        this.time = time;
        this.cla = cla;
        this.place = place;
        this.visibility = visibility;
        this.description = description;
    }
}
