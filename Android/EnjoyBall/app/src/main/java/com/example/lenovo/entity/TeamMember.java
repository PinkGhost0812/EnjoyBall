package com.example.lenovo.entity;

<<<<<<< Updated upstream
import java.io.Serializable;

public class TeamMember implements Serializable {
    private Integer id;
    private Integer teamId;
    private Integer memberId;
=======
public class TeamMember {
    private Integer team_memberid;
    private Integer team_id;
    private Integer member_id;
>>>>>>> Stashed changes

    public TeamMember(){}

    public Integer getTeam_memberid() {
        return team_memberid;
    }

    public void setTeam_memberid(Integer team_memberid) {
        this.team_memberid = team_memberid;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    @Override
    public String toString() {
        return "TeamMember{" +
                "team_memberid=" + team_memberid +
                ", team_id=" + team_id +
                ", member_id=" + member_id +
                '}';
    }

    public TeamMember(Integer team_memberid, Integer team_id, Integer member_id) {
        this.team_memberid = team_memberid;
        this.team_id = team_id;
        this.member_id = member_id;
    }
}
