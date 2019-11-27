package com.example.lenovo.enjoyball.entity;

public class TeamDemand {
    private Integer id;
    private Integer teamId;
    private Integer isHome;
    private Integer member;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getIsHome() {
        return isHome;
    }

    public void setIsHome(Integer isHome) {
        this.isHome = isHome;
    }

    public Integer getMember() {
        return member;
    }

    public void setMember(Integer member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "TeamDemand{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", isHome=" + isHome +
                ", member=" + member +
                '}';
    }

    public TeamDemand(Integer id, Integer teamId, Integer isHome, Integer member) {
        this.id = id;
        this.teamId = teamId;
        this.isHome = isHome;
        this.member = member;
    }
}
