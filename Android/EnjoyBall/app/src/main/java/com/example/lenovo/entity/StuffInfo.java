package com.example.lenovo.entity;

import java.io.Serializable;

public class StuffInfo implements Serializable{
    private Integer id;
    private String img;
    private String name;
    private Integer price;
    private String content;
    private int number = 1;

    @Override
    public String toString() {
        return "StuffInfo{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", content='" + content + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public StuffInfo(Integer id, String img, String name, Integer price, String content) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.price = price;
        this.content = content;
    }
}
