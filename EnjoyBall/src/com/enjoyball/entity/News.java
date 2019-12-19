package com.enjoyball.entity;

import java.util.Date;

import com.jfinal.plugin.activerecord.Model;

public class News extends Model<News>{
    private Integer news_id;
    private Integer news_class;
    private String news_title;
    private Integer news_author;
    private Date news_time;
    private String news_image;
    private String news_content;
    private Integer news_heat;
    private Integer news_likenum;
    private Integer news_game;
    
    public static final News dao = new News().dao();

    public News(){}

    public Integer getNews_id() {
        return news_id;
    }

    public void setNews_id(Integer news_id) {
        this.news_id = news_id;
    }

    public Integer getNews_class() {
        return news_class;
    }

    public void setNews_class(Integer news_class) {
        this.news_class = news_class;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public Integer getNews_author() {
        return news_author;
    }

    public void setNews_author(Integer news_author) {
        this.news_author = news_author;
    }

    public Date getNews_time() {
        return news_time;
    }

    public void setNews_time(Date news_time) {
        this.news_time = news_time;
    }

    public String getNews_image() {
        return news_image;
    }

    public void setNews_image(String news_image) {
        this.news_image = news_image;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public Integer getNews_heat() {
        return news_heat;
    }

    public void setNews_heat(Integer news_heat) {
        this.news_heat = news_heat;
    }

    public Integer getNews_likenum() {
        return news_likenum;
    }

    public void setNews_likenum(Integer news_likenum) {
        this.news_likenum = news_likenum;
    }

    public Integer getNews_game() {
        return news_game;
    }

    public void setNews_game(Integer news_game) {
        this.news_game = news_game;
    }

    @Override
    public String toString() {
        return "News{" +
                "news_id=" + news_id +
                ", news_class=" + news_class +
                ", news_title='" + news_title + '\'' +
                ", news_author=" + news_author +
                ", news_time=" + news_time +
                ", news_image='" + news_image + '\'' +
                ", news_content='" + news_content + '\'' +
                ", news_heat=" + news_heat +
                ", news_likenum=" + news_likenum +
                ", news_game=" + news_game +
                '}';
    }

    public News(Integer news_id, Integer news_class, String news_title, Integer news_author, Date news_time, String news_image, String news_content, Integer news_heat, Integer news_likenum, Integer news_game) {
        this.news_id = news_id;
        this.news_class = news_class;
        this.news_title = news_title;
        this.news_author = news_author;
        this.news_time = news_time;
        this.news_image = news_image;
        this.news_content = news_content;
        this.news_heat = news_heat;
        this.news_likenum = news_likenum;
        this.news_game = news_game;
    }
}
