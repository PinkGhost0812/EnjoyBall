package com.enjoyball.contest.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enjoyball.entity.Contest;
import com.enjoyball.entity.Team;
import com.enjoyball.util.TeamAndContest;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

public class ContestDao {
	public List<TeamAndContest> list(int page) {
		List<TeamAndContest> list = new ArrayList<TeamAndContest>();
		Page<Contest> pageList = Contest.dao.paginate(page, 8, "select *", "from game_info");
		List<Contest> contestList = pageList.getList();
		for(int i = 0 ; i < contestList.size() ; i++) {
			Team a = Team.dao.findById(contestList.get(i).get("game_home"));
			Team b = Team.dao.findById(contestList.get(i).get("game_away"));
			Map<String, String> teamMap = new HashMap<String, String>();
			teamMap.put("nameA", a.getStr("team_name"));
			teamMap.put("imgA", a.getStr("team_logo"));
			teamMap.put("nameB", b.getStr("team_name"));
			teamMap.put("imgB", b.getStr("team_logo"));
			Contest contest = contestList.get(i);
			TeamAndContest tac = new TeamAndContest();
			tac.setTeamMap(teamMap);
			tac.setContest(contest);
			list.add(tac);
		}
		return list;
	}
	
	public List<TeamAndContest> find(String cls, int page){
		List<TeamAndContest> list = new ArrayList<TeamAndContest>();
		Page<Contest> pageList = Contest.dao.paginate(page, 8, "select *","from game_info where game_class = ?",cls);
		List<Contest> contestList = pageList.getList();
		for(int i = 0 ; i < contestList.size() ; i++) {
			Team a = Team.dao.findById(contestList.get(i).get("game_home"));
			Team b = Team.dao.findById(contestList.get(i).get("game_away"));
			Map<String, String> teamMap = new HashMap<String, String>();
			teamMap.put("nameA", a.getStr("team_name"));
			teamMap.put("imgA", a.getStr("team_logo"));
			teamMap.put("nameB", b.getStr("team_name"));
			teamMap.put("imgB", b.getStr("team_logo"));
			Contest contest = contestList.get(i);
			TeamAndContest tac = new TeamAndContest();
			tac.setTeamMap(teamMap);
			tac.setContest(contest);
			list.add(tac);
		}
		return list;
	}
	
	public boolean add(Contest contest) {
		return contest.dao().save();
	}
	
	public String findById(String id) {
		Contest contest = Contest.dao.findById(id);
		return JsonKit.toJson(contest);
	}
}
