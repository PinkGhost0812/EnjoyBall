package com.example.lenovo.entity;

public class TeamMember {
    private Integer id;
    private Integer teamId;
    private Integer memberId;

    @Override
    public String toString() {
        return "TeamMember{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", memberId=" + memberId +
                '}';
    }

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

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public TeamMember(Integer id, Integer teamId, Integer memberId) {        this.id = id;
        this.teamId = teamId;
        this.memberId = memberId;
    }
}
