package com.enjoyball.team.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enjoyball.entity.Team;
import com.enjoyball.entity.TeamMember;
import com.enjoyball.entity.User;
import com.enjoyball.team.dao.TeamDao;
import com.enjoyball.util.JPushUtils;
import com.enjoyball.util.UserAndTeam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.kit.JsonKit;

public class TeamService {
	public String find(String teamName) {
		return new TeamDao().find(teamName);
	}
	
	public String register(Team team) {
		int x = new TeamDao().regist(team);
		if(x != -1) 
			return x+"";
		else
			return "false";
	}
	
	public String findByIdCls(String id,String cls) {
		Team t = new TeamDao().findByIdCls(id, cls);
		if(t == null)
			return "false";
		return JsonKit.toJson(t);
	}
	
	public String findById(String id) {
		Team t = new TeamDao().findById(id);
		return JsonKit.toJson(t);
	}
	
	public String findByPerson(String id) {
		List<UserAndTeam> list = new TeamDao().findByPerson(id);
		if(list == null || list.size() == 0)
			return "false";
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.toJson(list);
	}
	
	public String findMember(String id) {
		return new TeamDao().findMember(id);
	}
	
	public String dissolve(String id) {
		if(new TeamDao().dissolve(id))
			return "true";
		return "false";
	}
	
	public String update(String info) {
		Team team = new Gson().fromJson(info, Team.class);
		if(new TeamDao().update(team))
			return "true";
		else
			return "false";
	}
	
	public void invite(String teamId, String userId) {
		List<TeamMember> list = TeamMember.dao.find("select * from team_member where team_id = ? and member_id = ?", teamId, userId);
		if(list.size() != 0)
			return;
		Team team = Team.dao.findById(teamId);
		User user = User.dao.findById(userId);
		String message = "队伍" + team.getStr("team_name") + "邀请你加入";
		Map<String ,String> hash = new HashMap<String ,String>();
		hash.put("message", message);
		hash.put("invite", "inviteJoin");
		hash.put("team", JsonKit.toJson(team));
		System.out.println("userId  " + userId);
		System.out.println("jpushId  " + user.getStr("user_jpushid"));
		JPushUtils.sendAllNotification(message, hash, user.getStr("user_jpushid"));
	}
	
	public String inviteOk(String teamId,String userId) {
		if(new TeamDao().inviteOk(teamId, userId))
			return "true";
		return "false";
	}
	
	public String gun(String teamId, String userId) {
		Team team = Team.dao.findById(teamId);
		User user = User.dao.findById(userId);
		if(new TeamDao().gun(teamId, userId)) {
			String message = "你已经被请出" + team.getStr("team_name") + "队";
			JPushUtils.sendNotification(message, "通知", user.getStr("user_jpushid"));
			return "true";
		}
		return "false";
	}
	
	public String out(String teamId, String userId) {
		Team team = Team.dao.findById(teamId);
		User user = User.dao.findById(userId);
		User leader = User.dao.findById(team.getInt("team_captain"));
		if(new TeamDao().gun(teamId, userId)) {
			String message = user.getStr("user_nickname") + "已经退出了你的" + team.getStr("team_name") + "队";
			JPushUtils.sendNotification(message, "通知", leader.getStr("user_jpushid"));
			return "true";
		}
		return "false";
	}
	
}
