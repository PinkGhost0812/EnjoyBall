package com.enjoyball.appointment.dao;

import java.util.ArrayList;
import java.util.List;

import com.enjoyball.entity.ApplyInfo;
import com.enjoyball.entity.DTeamMember;
import com.enjoyball.entity.DemandInfo;
import com.enjoyball.entity.Team;
import com.enjoyball.entity.TeamDemand;
import com.enjoyball.entity.User;
import com.enjoyball.util.ApplyUtil;
import com.enjoyball.util.DbUtil;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

public class AppointmentDao {
	public List<DemandInfo> list(int page){
		Page<DemandInfo> pageList = DemandInfo.dao.paginate(page, 8, "select *","from demand_info");
		List<DemandInfo> list = pageList.getList();
		return list;
	}
	
	public List<DemandInfo> find(String sql){
		List<DemandInfo> list = DemandInfo.dao.find(sql);
		return list;
	}
	
	public List<DemandInfo> findByClass(int page,String cls){
		Page<DemandInfo> pageList = DemandInfo.dao.paginate(page, 8, "select *", "from demand_info where demand_class = ?",cls);
		List<DemandInfo> list = pageList.getList();
		return list;
	}
	
	//没有进行同步控制
	public boolean add(DemandInfo info, int teama, int teamb) {
		//将DemandInfo存入数据库
		boolean flag = info.set("demand_user", info.getDemand_user()).set("demand_time", info.getDemand_time())
					.set("demand_class", info.getDemand_class()).set("demand_place", info.getDemand_place())
					.set("demand_place",info.getDemand_place()).set("demand_visibility", info.getDemand_visibility())
					.set("demand_description", info.getDemand_description()).set("demand_num", info.getDemand_num() * 2)
					.set("demand_teama", teama).set("demand_teamb", teamb)
					.set("demand_oom", info.getDemand_oom()).save();
		//将这个发起约球的人和约球的数据存入比赛
		boolean flag2 = new DTeamMember().set("dteam_id", teama).set("member_id", info.getDemand_user()).save();
		
		return flag && flag2;
	}
	
	public boolean addFormalAppointment(DemandInfo demand, List<Integer> idList) {
		int demandId = DbUtil.executeUpdate("insert into demand_info (demand_user,demand_time,demand_class,demand_place,"
				+ "demand_visibility,demand_description,demand_num,demand_teama,demand_oom) values(?,?,?,?,?,?,?,?,?)", 
				new Object[] {demand.getDemand_user(),demand.getDemand_time(),demand.getDemand_class(),demand.getDemand_place(),
						demand.getDemand_visibility(),demand.getDemand_description(),demand.getDemand_num()*2,demand.getDemand_teama(),
						demand.getDemand_oom()});
		if(idList != null) {
			for(int i = 0 ; i < idList.size() ; i++) {
				List<Object> list = DbUtil.findAllWithWhere("select * from team_info where team_captain = ? and team_class = ?", com.enjoyball.util.Team.class,
						new Object[] {idList.get(i),demand.getDemand_class()});
				int x = DbUtil.executeUpdate("insert into apply_info (sender,receiver,teamId,demandId,isInvite,handle) values (?,?,?,?,?,?)",
						new Object[] {demand.getDemand_user(),idList.get(i),((com.enjoyball.util.Team)list.get(0)).getTeam_id(),demandId,0,0}); 
				if(x == -1)
					return false;
			}
		}
		return true;
	}
	
	public boolean addAppointmentWithInvite(DemandInfo demand,List<Integer> idList) {
		int teama = DbUtil.executeUpdate("insert into demand_team (capacity , quantity) values(?,?)", 
				new Object[] {demand.getDemand_num(),1});
		int teamb = DbUtil.executeUpdate("insert into demand_team (capacity , quantity) values(?,?)", 
				new Object[] {demand.getDemand_num(),0});
		int demandId = DbUtil.executeUpdate("insert into demand_info (demand_user,demand_time,demand_class,demand_place,"
				+ "demand_visibility,demand_description,demand_num,demand_teama,demand_teamb,demand_oom) values(?,?,?,?,?,?,?,?,?,?)", 
				new Object[] {demand.getDemand_user(),demand.getDemand_time(),demand.getDemand_class(),demand.getDemand_place(),
						demand.getDemand_visibility(),demand.getDemand_description(),demand.getDemand_num()*2,teama,
						teamb,demand.getDemand_oom()});
		boolean flag = new DTeamMember().set("dteam_id", teama).set("member_id", demand.getDemand_user()).save();
		if(flag)
			System.out.println(teama + "   " + demand.getDemand_user());
		else
			System.out.println("false");
		if(idList != null) {
			for(int i = 0 ; i < idList.size() ; i++) {
				int x = DbUtil.executeUpdate("insert into apply_info (sender,receiver,teamId,demandId,isInvite,handle) values (?,?,?,?,?,?)",
						new Object[] {demand.getDemand_user(),idList.get(i),teama,demandId,0,0}); 
//				boolean flag2 = ApplyInfo.dao.set("sender", demand.getDemand_user()).set("receiver", idList.get(i)).set("teamId", teama)
//					.set("demandId", demandId).set("isInvite", 0).set("handle", 0).save();
				if( !(flag && (x != -1)))
					return false;
			}
		}
		return true;
		
	}
	
	public com.enjoyball.util.DemandInfo findById(String id){
		List<Object> list = DbUtil.findAllWithWhere("select * from demand_info where demand_id = ?", com.enjoyball.util.DemandInfo.class, 
				new Object[] {id});
		if(list.size() == 0)
			return null;
		else
			return (com.enjoyball.util.DemandInfo)list.get(0);
	}
	
	
	
	public String apply(String userId, String teamId, String applyId) {
		System.out.println("userId = " + userId);
		int x = 0;
		TeamDemand team = TeamDemand.dao.findById(teamId);
		int capacity = team.get("capacity");
		int quantity = team.get("quantity");
		if(quantity < capacity) {
			team.set("quantity", quantity+1).update();
			x = DbUtil.executeUpdate("insert into dteam_member (dteam_id,member_id) values(?,?)",new Object[] {teamId,userId});
			DbUtil.executeUpdate("update apply_info set handle = 1 where id = ?",new Object[] {applyId});
		}else {
			return "false";
		}
		if((x != -1))
			return "true";
		else
			return "false";
	}
	
	public String findByUserId(String id) {
		List<Object> list = DbUtil.findAllWithWhere("select * from demand_info where demand_user = ?", com.enjoyball.util.DemandInfo.class,
				new Object[] {id});
		if(list.size() == 0)
			return "false";
		return JsonKit.toJson(list);
	}
	
	public List<TeamDemand> getDemandTeam(String id){
		List<TeamDemand> list = TeamDemand.dao.find("select * from demand_team where id = ?",id);
		if(list.size() == 0)
			return null;
		return list;
	}
	
	public String applyToLeader(com.enjoyball.util.ApplyInfo apply) {
		List<Object> list = DbUtil.findAllWithWhere("select * from apply_info where sender = ? and receiver = ? and teamId = ? and demandId = ?",
				com.enjoyball.util.ApplyInfo.class,new Object[] {apply.getSender(),apply.getReceiver(),apply.getTeamId(),apply.getDemandId()});
		if(list.size() != 0 && ((com.enjoyball.util.ApplyInfo)list.get(0)).getHandle() == 0) {
			return "请不要重复提交申请";
		}
		int x = DbUtil.executeUpdate("insert into apply_info (sender,receiver,teamId,demandId,isInvite,handle) values(?,?,?,?,?,?)",
				new Object[] {apply.getSender(),apply.getReceiver(),apply.getTeamId(),apply.getDemandId(),apply.getIsInvite(),apply.getHandle()});
		if(x != -1)
			return "true";
		else
			return "网络开小差了";
	}

	public List<ApplyUtil> messageList(String id) {
		List<ApplyUtil> list = new ArrayList<>();
		List<Object> applyList = DbUtil.findAllWithWhere("select * from apply_info where receiver = ? and handle != 1", 
				com.enjoyball.util.ApplyInfo.class, new Object[] {id});
		if(applyList.size() == 0)
			return null;
		for(int i = 0 ; i < applyList.size() ; i++) {
			ApplyUtil util = new ApplyUtil();
			com.enjoyball.util.ApplyInfo app = (com.enjoyball.util.ApplyInfo)applyList.get(i);
			List<Object> senderList = DbUtil.findAllWithWhere("select * from user_info where user_id = ?", 
					com.enjoyball.util.User.class, new Object[] {app.getSender()});
			List<Object> demandList = DbUtil.findAllWithWhere("select * from demand_info where demand_id = ?"
					, com.enjoyball.util.DemandInfo.class, new Object[] {app.getDemandId()});
			util.setApplyInfo(app);
			if(senderList != null && senderList.size() != 0)
				util.setUser((com.enjoyball.util.User)senderList.get(0));
			if(demandList != null && demandList.size() != 0)
				util.setDemand((com.enjoyball.util.DemandInfo)demandList.get(0));
			list.add(util);
		}
		
		return list;
	}
	
	public String teamA(String id) {
		int teamAId = DemandInfo.dao.findById(id).getInt("demand_teama");
		List<DTeamMember> memberList = DTeamMember.dao.find("select * from dteam_member where dteam_id = ?",teamAId);
		List<User> list = new ArrayList<User>();
		if(memberList == null || memberList.size() == 0)
			return "false";
		for(int i = 0 ; i < memberList.size() ; i++) {
			User u = User.dao.findById(memberList.get(i).getInt("member_id"));
			list.add(u);
		}
		return JsonKit.toJson(list);
	}
	
	public String teamB(String id) {
		int teamAId = DemandInfo.dao.findById(id).getInt("demand_teamb");
		List<DTeamMember> memberList = DTeamMember.dao.find("select * from dteam_member where dteam_id = ?",teamAId);
		List<User> list = new ArrayList<User>();
		if(memberList == null || memberList.size() == 0)
			return "false";
		for(int i = 0 ; i < memberList.size() ; i++) {
			User u = User.dao.findById(memberList.get(i).getInt("member_id"));
			list.add(u);
		}
		return JsonKit.toJson(list);
	}
	
	public boolean acceptInvite(String teamId, String demandId, String applyInfo) {
		boolean flag = DemandInfo.dao.findById(demandId).set("demand_teamb", teamId).update();
		boolean flag2 = ApplyInfo.dao.findById(applyInfo).set("handle", 1).update();
		return flag && flag2;
		
	}
	
	public boolean refuse(String id) {
		boolean flag = ApplyInfo.dao.findById(id).set("handle", 1).update();
		return flag;
	}
	
	public boolean change(String teama, String teamb, String userId) {
		int x = DbUtil.executeUpdate("delete from dteam_member where dteam_id = ? and member_id = ?", new Object[] {teama,userId});
		if(x == -1)
			return false;
		x = DbUtil.executeUpdate("insert into dteam_member (dteam_id, member_id) values (?,?)", new Object[] {teamb,userId});
		if(x == -1)
			return false;
		return true;
	}
	
}
