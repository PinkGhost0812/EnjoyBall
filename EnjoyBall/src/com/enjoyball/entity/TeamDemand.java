package com.enjoyball.entity;

import com.jfinal.plugin.activerecord.Model;

public class TeamDemand extends Model<TeamDemand>{
    private Integer id;
    private Integer capacity;
    private Integer quantity;

    public static final TeamDemand dao = new TeamDemand().dao(); 
    
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
