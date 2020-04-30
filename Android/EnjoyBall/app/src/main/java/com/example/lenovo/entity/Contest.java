package com.example.lenovo.entity;

import java.io.Serializable;
import java.util.Date;

public class Contest implements Serializable {

    private Integer game_id;
    private Integer game_home;
    private Integer game_away;
    private Date game_time;
    private String game_place;
    private Integer game_class;
    private Integer game_grade;
    private Integer game_status;
    private String game_result;
    private Integer game_category;

    public Integer getGame_category() {
        return game_category;
    }

    public void setGame_category(Integer game_category) {
        this.game_category = game_category;
    }

    public Contest(){}

    public Integer getGame_id() {
        return game_id;
    }

    public void setGame_id(Integer game_id) {
        this.game_id = game_id;
    }

    public Integer getGame_home() {
        return game_home;
    }

    public void setGame_home(Integer game_home) {
        this.game_home = game_home;
    }

    public Integer getGame_away() {
        return game_away;
    }

    public void setGame_away(Integer game_away) {
        this.game_away = game_away;
    }

    public Date getGame_time() {
        return game_time;
    }

    public void setGame_time(Date game_time) {
        this.game_time = game_time;
    }

    public String getGame_place() {
        return game_place;
    }

    public void setGame_place(String game_place) {
        this.game_place = game_place;
    }

    public Integer getGame_class() {
        return game_class;
    }

    public void setGame_class(Integer game_class) {
        this.game_class = game_class;
    }

    public Integer getGame_grade() {
        return game_grade;
    }

    public void setGame_grade(Integer game_grade) {
        this.game_grade = game_grade;
    }

    public Integer getGame_status() {
        return game_status;
    }

    public void setGame_status(Integer game_status) {
        this.game_status = game_status;
    }

    public String getGame_result() {
        return game_result;
    }

    public void setGame_result(String game_result) {
        this.game_result = game_result;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "game_id=" + game_id +
                ", game_home=" + game_home +
                ", game_away=" + game_away +
                ", game_time=" + game_time +
                ", game_place='" + game_place + '\'' +
                ", game_class=" + game_class +
                ", game_grade=" + game_grade +
                ", game_status=" + game_status +
                ", game_result='" + game_result + '\'' +
                '}';
    }

    public Contest(Integer game_id, Integer game_home, Integer game_away, Date game_time, String game_place, Integer game_class, Integer game_grade, Integer game_status, String game_result) {
        this.game_id = game_id;
        this.game_home = game_home;
        this.game_away = game_away;
        this.game_time = game_time;
        this.game_place = game_place;
        this.game_class = game_class;
        this.game_grade = game_grade;
        this.game_status = game_status;
        this.game_result = game_result;
    }
}
