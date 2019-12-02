package com.example.lenovo.entity;

import java.io.Serializable;

public class TeamDemand implements Serializable{
    private Integer id;
    private Integer capacity;
    private Integer quantity;

    public TeamDemand(){}

    public TeamDemand(Integer capacity, Integer quantity) {
        this.capacity = capacity;
        this.quantity = quantity;
    }

    public TeamDemand(Integer id, Integer capacity, Integer quantity) {
        this.id = id;
        this.capacity = capacity;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "TeamDemand{" +
                "id=" + id +
                ", capacity=" + capacity +
                ", quantity=" + quantity +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
