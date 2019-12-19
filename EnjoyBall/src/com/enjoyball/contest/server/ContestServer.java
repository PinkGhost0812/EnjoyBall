package com.enjoyball.contest.server;

import java.util.List;

import com.enjoyball.contest.dao.ContestDao;
import com.enjoyball.entity.Contest;
import com.enjoyball.util.TeamAndContest;
import com.google.gson.Gson;
import com.jfinal.kit.JsonKit;

public class ContestServer {
	public String list(int page) {
		List<TeamAndContest> list = new ContestDao().list(page);
		return JsonKit.toJson(list);
	}
	
	public String find(String sql,int page) {
		List<TeamAndContest> list = new ContestDao().find(sql,page);
		return JsonKit.toJson(list);
	}
	
	public String add(String info) {
		Contest contest = new Gson().fromJson(info, Contest.class);
		if( new ContestDao().add(contest) )
			return "true";
		else
			return "false";
	}
	
	public String findById(String id) {
		return new ContestDao().findById(id);
	}
}
