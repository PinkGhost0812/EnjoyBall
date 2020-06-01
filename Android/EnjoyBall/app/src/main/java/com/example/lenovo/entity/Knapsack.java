package com.example.lenovo.entity;

public class Knapsack {
    private Integer knapsack_id;
    private String knapsack_image;
    private String knapsack_name;
    private Integer knapsack_num = 0;
    private Integer knapsack_img;

    public Knapsack() {
    }

    public Integer getKnapsack_id() {
        return knapsack_id;
    }

    public void setKnapsack_id(Integer knapsack_id) {
        this.knapsack_id = knapsack_id;
    }

    public String getKnapsack_image() {
        return knapsack_image;
    }

    public void setKnapsack_image(String knapsack_image) {
        this.knapsack_image = knapsack_image;
    }

    public String getKnapsack_name() {
        return knapsack_name;
    }

    public void setKnapsack_name(String knapsack_name) {
        this.knapsack_name = knapsack_name;
    }

    public Integer getKnapsack_num() {
        return knapsack_num;
    }

    public void setKnapsack_num(Integer knapsack_num) {
        this.knapsack_num = knapsack_num;
    }

    public Integer getKnapsack_img() {
        return knapsack_img;
    }

    public void setKnapsack_img(Integer knapsack_img) {
        this.knapsack_img = knapsack_img;
    }

    @Override
    public String toString() {
        return "Knapsack{" +
                "knapsack_id=" + knapsack_id +
                ", knapsack_image='" + knapsack_image + '\'' +
                ", knapsack_name='" + knapsack_name + '\'' +
                ", knapsack_num=" + knapsack_num +
                ", knapsack_img=" + knapsack_img +
                '}';
    }
}
