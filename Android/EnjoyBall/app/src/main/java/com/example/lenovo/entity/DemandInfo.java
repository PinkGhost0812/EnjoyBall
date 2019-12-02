package com.example.lenovo.entity;

import java.io.Serializable;
import java.util.Date;

public class DemandInfo implements Serializable {

    private Integer demand_id;
    private Integer demand_user;
    private Date demand_time;
    private Integer demand_class;
    private String demand_place;
    private Integer demand_visibility;
    private String demand_description;
    private Integer demand_num;
    private Integer demand_teama;
    private Integer demand_teamb;
    private Integer demand_oom;

    public DemandInfo(){}

    public Integer getDemand_oom() {
        return demand_oom;
    }

    public void setDemand_oom(Integer demand_oom) {
        this.demand_oom = demand_oom;
    }

    @Override
    public String toString() {
        return "DemandInfo{" +
                "demand_id=" + demand_id +
                ", demand_user=" + demand_user +
                ", demand_time=" + demand_time +
                ", demand_class=" + demand_class +
                ", demand_place='" + demand_place + '\'' +
                ", demand_visibility=" + demand_visibility +
                ", demand_description='" + demand_description + '\'' +
                ", demand_num=" + demand_num +
                ", demand_teama=" + demand_teama +
                ", demand_teamb=" + demand_teamb +
                ", demand_oom=" + demand_oom +
                '}';
    }

    public DemandInfo(Integer demand_id, Integer demand_user, Date demand_time, Integer demand_class, String demand_place, Integer demand_visibility, String demand_description, Integer demand_num, Integer demand_teama, Integer demand_teamb, Integer demand_oom) {
        this.demand_id = demand_id;
        this.demand_user = demand_user;
        this.demand_time = demand_time;
        this.demand_class = demand_class;
        this.demand_place = demand_place;
        this.demand_visibility = demand_visibility;
        this.demand_description = demand_description;
        this.demand_num = demand_num;
        this.demand_teama = demand_teama;
        this.demand_teamb = demand_teamb;
        this.demand_oom = demand_oom;
    }

    public Integer getDemand_num() {
        return demand_num;
    }

    public void setDemand_num(Integer demand_num) {
        this.demand_num = demand_num;
    }

    public Integer getDemand_teama() {
        return demand_teama;
    }

    public void setDemand_teama(Integer demand_teama) {
        this.demand_teama = demand_teama;
    }

    public Integer getDemand_teamb() {
        return demand_teamb;
    }

    public void setDemand_teamb(Integer demand_teamb) {
        this.demand_teamb = demand_teamb;
    }

    public Integer getDemand_id() {
        return demand_id;
    }

    public void setDemand_id(Integer demand_id) {
        this.demand_id = demand_id;
    }

    public Integer getDemand_user() {
        return demand_user;
    }

    public void setDemand_user(Integer demand_user) {
        this.demand_user = demand_user;
    }

    public Date getDemand_time() {
        return demand_time;
    }

    public void setDemand_time(Date demand_time) {
        this.demand_time = demand_time;
    }

    public Integer getDemand_class() {
        return demand_class;
    }

    public void setDemand_class(Integer demand_class) {
        this.demand_class = demand_class;
    }

    public String getDemand_place() {
        return demand_place;
    }

    public void setDemand_place(String demand_place) {
        this.demand_place = demand_place;
    }

    public Integer getDemand_visibility() {
        return demand_visibility;
    }

    public void setDemand_visibility(Integer demand_visibility) {
        this.demand_visibility = demand_visibility;
    }

    public String getDemand_description() {
        return demand_description;
    }

    public void setDemand_description(String demand_description) {
        this.demand_description = demand_description;
    }

    public DemandInfo(Integer demand_id, Integer demand_user, Date demand_time, Integer demand_class, String demand_place, Integer demand_visibility, String demand_description) {
        this.demand_id = demand_id;
        this.demand_user = demand_user;
        this.demand_time = demand_time;
        this.demand_class = demand_class;
        this.demand_place = demand_place;
        this.demand_visibility = demand_visibility;
        this.demand_description = demand_description;
    }

}
