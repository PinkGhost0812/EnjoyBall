package com.example.lenovo.entity;

import java.util.Map;

public class TeamAndContest {
    Map<String, String> teamMap;
    Contest contest;

    public Map<String, String> getTeamMap() {
        return teamMap;
    }

    public void setTeamMap(Map<String, String> teamMap) {
        this.teamMap = teamMap;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }
}