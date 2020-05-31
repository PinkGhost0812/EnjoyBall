package com.example.lenovo.entity;

public class PackageMap {
    private int id;
    private Integer package_id;
    private Integer stuff_id;
    private Integer number;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PackageMap{" +
                "id=" + id +
                ", package_id=" + package_id +
                ", stuff_id=" + stuff_id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public Integer getStuff_id() {
        return stuff_id;
    }

    public void setStuff_id(Integer stuff_id) {
        this.stuff_id = stuff_id;
    }

    public PackageMap(int id, Integer package_id, Integer stuff_id) {
        this.id = id;
        this.package_id = package_id;
        this.stuff_id = stuff_id;
    }
}