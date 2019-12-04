package com.example.lenovo.entity;

import java.io.Serializable;
import java.util.Date;

public class Team implements Serializable {
<<<<<<< Updated upstream
    private Integer team_id;
    private String team_name;
    private String team_region;
    private Date team_time;
    private String team_logo;
    private String team_slogan;
    private Integer team_score;
    private Integer team_captain;
    private Integer team_state;
    private Integer team_class;
=======

    private Integer id;
    private String name;
    private String region;
    private Date time;
    private String logo;
    private String slogan;
    private Integer score;
    private Integer captain;
    private Integer coach;
>>>>>>> Stashed changes

    public Team(){}

    public Integer getTeam_class() {
        return team_class;
    }

    public void setTeam_class(Integer team_class) {
        this.team_class = team_class;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_region() {
        return team_region;
    }

    public void setTeam_region(String team_region) {
        this.team_region = team_region;
    }

    public Date getTeam_time() {
        return team_time;
    }

    public void setTeam_time(Date team_time) {
        this.team_time = team_time;
    }

    public String getTeam_logo() {
        return team_logo;
    }

    public void setTeam_logo(String team_logo) {
        this.team_logo = team_logo;
    }

    public String getTeam_slogan() {
        return team_slogan;
    }

    public void setTeam_slogan(String team_slogan) {
        this.team_slogan = team_slogan;
    }

    public Integer getTeam_score() {
        return team_score;
    }

    public void setTeam_score(Integer team_score) {
        this.team_score = team_score;
    }

    public Integer getTeam_captain() {
        return team_captain;
    }

    public void setTeam_captain(Integer team_captain) {
        this.team_captain = team_captain;
    }

    public Integer getTeam_state() {
        return team_state;
    }

    public void setTeam_state(Integer team_state) {
        this.team_state = team_state;
    }

    @Override
    public String toString() {
        return "Team{" +
                "team_id=" + team_id +
                ", team_name='" + team_name + '\'' +
                ", team_region='" + team_region + '\'' +
                ", team_time=" + team_time +
                ", team_logo='" + team_logo + '\'' +
                ", team_slogan='" + team_slogan + '\'' +
                ", team_score=" + team_score +
                ", team_captain=" + team_captain +
                ", team_state=" + team_state +
                ", team_class=" + team_class +
                '}';
    }

    public Team(Integer team_id, String team_name, String team_region, Date team_time, String team_logo, String team_slogan, Integer team_score, Integer team_captain, Integer team_state, Integer team_class) {
        this.team_id = team_id;
        this.team_name = team_name;
        this.team_region = team_region;
        this.team_time = team_time;
        this.team_logo = team_logo;
        this.team_slogan = team_slogan;
        this.team_score = team_score;
        this.team_captain = team_captain;
        this.team_state = team_state;
        this.team_class = team_class;
    }
}
