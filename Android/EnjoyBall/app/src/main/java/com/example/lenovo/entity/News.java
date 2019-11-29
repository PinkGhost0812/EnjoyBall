package com.example.lenovo.entity;
<<<<<<< Updated upstream

=======
import java.io.Serializable;
>>>>>>> Stashed changes
import java.util.Date;

public class News implements Serializable {
    private Integer id;
    private Integer cla;
    private String title;
    private Integer author;
    private Date time;
    private String image;
    private String content;
    private Integer heat;
    private Integer likeNum;
    private Integer game;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCla() {
        return cla;
    }

    public void setCla(Integer cla) {
        this.cla = cla;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getHeat() {
        return heat;
    }

    public void setHeat(Integer heat) {
        this.heat = heat;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getGame() {
        return game;
    }

    public void setGame(Integer game) {
        this.game = game;
    }

    public News(Integer id, Integer cla, String title, Integer author, Date time, String image, String content, Integer heat, Integer likeNum, Integer game) {
        this.id = id;
        this.cla = cla;
        this.title = title;
        this.author = author;
        this.time = time;
        this.image = image;
        this.content = content;
        this.heat = heat;
        this.likeNum = likeNum;
        this.game = game;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", cla=" + cla +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", time=" + time +
                ", image='" + image + '\'' +
                ", content='" + content + '\'' +
                ", heat=" + heat +
                ", likeNum=" + likeNum +
                ", game=" + game +
                '}';
    }
}
