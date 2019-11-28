package com.enjoyball.entity;

import java.util.Date;

public class Team {
    private Integer id;
    private String name;
    private String region;
    private Date time;
    private String logo;
    private String slogan;
    private Integer score;
    private Integer captain;
    private Integer coach;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCaptain() {
        return captain;
    }

    public void setCaptain(Integer captain) {
        this.captain = captain;
    }

    public Integer getCoach() {
        return coach;
    }

    public void setCoach(Integer coach) {
        this.coach = coach;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", time=" + time +
                ", logo='" + logo + '\'' +
                ", slogan='" + slogan + '\'' +
                ", score=" + score +
                ", captain=" + captain +
                ", coach=" + coach +
                '}';
    }

    public Team(Integer id, String name, String region, Date time, String logo, String slogan, Integer score, Integer captain, Integer coach) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.time = time;
        this.logo = logo;
        this.slogan = slogan;
        this.score = score;
        this.captain = captain;
        this.coach = coach;
    }
}
