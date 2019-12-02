package com.example.lenovo.entity;

public class DTeamMember {
    private Integer id;
    private Integer DTeamId;
    private Integer member_id;

    public DTeamMember(){}

    @Override
    public String toString() {
        return "DTeamMember{" +
                "id=" + id +
                ", DTeamId=" + DTeamId +
                ", member_id=" + member_id +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDTeamId() {
        return DTeamId;
    }

    public void setDTeamId(Integer DTeamId) {
        this.DTeamId = DTeamId;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public DTeamMember(Integer id, Integer DTeamId, Integer member_id) {
        this.id = id;
        this.DTeamId = DTeamId;
        this.member_id = member_id;
    }
}
