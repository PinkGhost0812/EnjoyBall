package com.enjoyball.team.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.enjoyball.entity.Team;
import com.enjoyball.entity.TeamMember;
import com.enjoyball.entity.User;
import com.enjoyball.util.DbUtil;
import com.enjoyball.util.UserAndTeam;
import com.jfinal.kit.JsonKit;

public class TeamDao {
	
	
	public String find(String teamName) {
		List<Team> team = Team.dao.find("select * from team_info where team_name = ?", teamName);
		return JsonKit.toJson(team.get(0));
	}
	
	public int regist(Team team) {
		int x = DbUtil.executeUpdate("insert into team_info (team_name,team_slogan,team_time,team_class,team_region,team_number,team_captain,team_state,team_score) values(?,?,?,?,?,?,?,?,?)",
				new Object[] {team.getTeam_name(),team.getTeam_slogan(),new Date(),team.getTeam_class(),"待定",1,team.getTeam_captain(),1,0});
		int y = 0;
		if(x != -1) {
			Team.dao.findById(x).set("team_logo", "img/t"+x+".png").update();
			y = DbUtil.executeUpdate("insert into team_member (team_id,member_id) values (?,?)", new Object[] {x,team.getTeam_captain()});
		}
		if(y != -1)
			return x;
		else
			return -1;
	}
	
	//size=0;
	public Team findByIdCls(String id, String cls) {
		List<Team> team = Team.dao.find("select * from team_info where team_captain = ? and team_class = ?", id,cls);
		if(team.size() == 0)
			return null;
		return team.get(0);
	}
	
	public Team findById(String id) {
		Team t = Team.dao.findById(id);
		return t;
	}
	
	public List<UserAndTeam> findByPerson(String id) {
		List<TeamMember> teamList = TeamMember.dao.find("select * from team_member where member_id = ?",id);
		if(teamList.size() == 0)
			return null;
		List<UserAndTeam> list = new ArrayList<UserAndTeam>();
		System.out.println("teamlist"+teamList);
		
		for(int i = 0 ; i < teamList.size() ; i++) {
			List<Object> team = DbUtil.findAllWithWhere("select * from team_info where team_id = ?", com.enjoyball.util.Team.class,
					new Object[] {teamList.get(i).get("team_id")});
			System.out.println("team"+team.toString());
			List<Object> user = DbUtil.findAllWithWhere("select * from user_info where user_id = ?", com.enjoyball.util.User.class,
					new Object[] { ((com.enjoyball.util.Team)team.get(0)).getTeam_captain()});
			if(team.size() == 0 || user.size() == 0)
				return null;
			if( ((com.enjoyball.util.Team) team.get(0)).getTeam_state() != 0) {
				UserAndTeam uat = new UserAndTeam();
				uat.setUser((com.enjoyball.util.User) user.get(0));
				uat.setTeam((com.enjoyball.util.Team) team.get(0));
				list.add(uat);
			}
			
		}
		return list;
	}

	public String findMember(String id) {
		List<User> list = new ArrayList<>();
		List<TeamMember> memberList = TeamMember.dao.find("select * from team_member where team_id = ?",id);
		if(memberList == null || memberList.size() == 0)
			return "false";
		for(int i = 0 ; i < memberList.size() ; i++) {
			User u = User.dao.findById(memberList.get(i).get("member_id"));
			list.add(u);
		}
		return JsonKit.toJson(list);
	}
	
	public boolean dissolve(String id) {
		return Team.dao.findById(id).set("team_state", 0).update();
	}
	
	
	public boolean update(Team team) {
		boolean flag = team.set("team_id", team.getTeam_id()).set("team_name", team.getTeam_name()).set("team_slogan", team.getTeam_slogan()).update();
//		int x = DbUtil.executeUpdate("update team_info set team_name = ?, team_slogan = ? where team_id = ",
//				new Object[] {team.getTeam_name(),team.getTeam_slogan(),team.getTeam_id()});
		if(flag)
			return true;
		return false;
	}
	
	public boolean inviteOk(String teamId, String userId) {
		int ans = DbUtil.executeUpdate("insert into team_member (team_id,member_id) values (?,?)", new Object[] {teamId,userId});
		int x = 0;
		if(ans != -1) {
			List<Object> list = DbUtil.findAllWithWhere("select * from team_info where team_id = ?", com.enjoyball.util.Team.class
					,new Object[] {teamId});
			int number = ((com.enjoyball.util.Team)list.get(0)).getTeam_number();
//			number = number+1;
			System.out.println(number);
			x = DbUtil.executeUpdate("update team_info set team_number = ? where team_id = ?", new Object[] {++number,teamId});
//			Team team = Team.dao.findById(teamId);
//			int number = team.getInt("team_number");
//			team.set("team_number", number+1).update();	
		}
		if(x != -1)
			return true;
		return false;
	}
	
	public boolean gun(String teamId, String userId) {
		int x = DbUtil.executeUpdate("delete from team_member where team_id = ? and member_id = ?", new Object[] {teamId,userId});
		if(x != -1) {
			Team team = Team.dao.findById(teamId);
			int num = team.getInt("team_number");
			if(num == 1) {
				team.set("team_state", 0).update();
				return true;
			}
			team.set("team_number", num-1).update();
			return true;
		}
		return false;
	}
	
}
