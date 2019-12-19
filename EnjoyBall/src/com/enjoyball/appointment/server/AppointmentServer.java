package com.enjoyball.appointment.server;

import java.util.List;

import com.enjoyball.appointment.dao.AppointmentDao;
import com.enjoyball.entity.ApplyInfo;
import com.enjoyball.entity.DemandInfo;
import com.enjoyball.entity.Team;
import com.enjoyball.entity.TeamDemand;
import com.enjoyball.entity.User;
import com.enjoyball.util.ApplyUtil;
import com.enjoyball.util.DbUtil;
import com.enjoyball.util.JPushUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfinal.kit.JsonKit;

public class AppointmentServer {
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	public String list(int page) {
		List<DemandInfo> list = new AppointmentDao().list(page);
		return JsonKit.toJson(list);
	}
	
	public String find(String sql) {
		List<DemandInfo> list = new AppointmentDao().find(sql);
		return JsonKit.toJson(list);
	}
	
	public String findByClass(int page, String cls) {
		List<DemandInfo> list = new AppointmentDao().findByClass(page, cls);
		return JsonKit.toJson(list);
	}
	
	/**
	 * 创建约球
	 * @param info  DemandInfo类的对象的Json串
	 * @return  true / false
	 * 先获得DemandInfo 然后给这个约球创建两个队伍，A对有一个人为创建该约球的人
	 * 
	 * 
	 */
	public String add(String info) {
		DemandInfo appointment = gson.fromJson(info, DemandInfo.class);
		int teama = DbUtil.executeUpdate("insert into demand_team (capacity , quantity) values(?,?)", 
				new Object[] {appointment.getDemand_num(),1});
		int teamb = DbUtil.executeUpdate("insert into demand_team (capacity , quantity) values(?,?)", 
				new Object[] {appointment.getDemand_num(),0});
		System.out.println("demand_num = " + appointment.getDemand_num());
		System.out.println("teama = " + teama + "  teamb = " + teamb);
		if(teama == -1 || teamb == -1)
			return "false";
		if(new AppointmentDao().add(appointment, teama, teamb))
			return "true";
		else
			return "false";
	}
	
	public String addFormalAppointment(String info, String idList) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		DemandInfo appointment = gson.fromJson(info, DemandInfo.class);
		List<Integer> list = gson.fromJson(idList, new TypeToken<List<Integer>>() {}.getType());
		if(new AppointmentDao().addFormalAppointment(appointment, list)) {
			if(list != null) {
				for(int i = 0 ; i < list.size() ; i++) {
					System.out.println("userId = " + User.dao.findById(list.get(i)).get("user_id"));
					System.out.println("jpushId = " + User.dao.findById(list.get(i)).get("user_jpushid"));
					JPushUtils.sendNotification("你收到了一个新的约球邀请", "New Message", User.dao.findById(list.get(i)).get("user_jpushid"));
				}
			}
			return "true";
		}
		return "false";
	}
	
	public String addAppointmentWithInvite(String demandInfo, String idList) {
		DemandInfo appointment = gson.fromJson(demandInfo, DemandInfo.class);
		List<Integer> list = gson.fromJson(idList, new TypeToken<List<Integer>>() {}.getType());
		if(new AppointmentDao().addAppointmentWithInvite(appointment, list)) {
			if(list != null) {
				for(int i = 0 ; i < list.size() ; i++) {
					System.out.println("userId = " + User.dao.findById(list.get(i)).get("user_id"));
					System.out.println("jpushId = " + User.dao.findById(list.get(i)).get("user_jpushid"));
					JPushUtils.sendNotification("你收到了一个新的约球邀请", "New Message", User.dao.findById(list.get(i)).get("user_jpushid"));
				}
			}
			return "true";
		}
		return "false";
	}
	
	public String findById(String id) {
		com.enjoyball.util.DemandInfo demand = new AppointmentDao().findById(id);
		if(demand != null)
			return gson.toJson(demand);
		else
			return 
				"fasle";
	}
	
	public String apply(String userId,String teamId,String applyId) {
		return new AppointmentDao().apply(userId, teamId,applyId);
	}
	
	public String findByUserId(String id) {
		return new AppointmentDao().findByUserId(id);
	}
	
	public String getDemandTeam(String id) {
		List<TeamDemand> list = new AppointmentDao().getDemandTeam(id);
		if(list == null || list.size() == 0)
			return "false";
		return JsonKit.toJson(list);
	}
	
	public String applyToLeader(String info, String id) {
		com.enjoyball.util.ApplyInfo apply = gson.fromJson(info, com.enjoyball.util.ApplyInfo.class);
		System.out.println(apply.toString());
		String ans = new AppointmentDao().applyToLeader(apply);
		if(ans.equals("true")) {
			List<Object> list = DbUtil.findAllWithWhere("select * from user_info where user_id = ?", com.enjoyball.util.User.class,
					new Object[] {apply.getReceiver()});
			System.out.println(apply.getReceiver());
			com.enjoyball.util.User user = (com.enjoyball.util.User) list.get(0);
			String idd = user.getUser_jpushid();

			JPushUtils.sendNotification("你收到了约球消息，快去查看吧", "New Message", idd);
			return "true";
		}
		else
			return ans;
	}
	
	public String messageList(String id) {
		List<ApplyUtil> list = new AppointmentDao().messageList(id);
		if(list == null || list.size() == 0)
			return "false";
		return gson.toJson(list);
	}
	
	public String teamA(String id) {
		return new AppointmentDao().teamA(id);
	}
	
	public String teamB(String id) {
		return new AppointmentDao().teamB(id);
	}
	
	
//	
//	public String acceptInvite(String teamId, String demandId) {
//		if(new AppointmentDao().acceptInvite(teamId, demandId)) {
//			int userId = DemandInfo.dao.findById(demandId).getInt("demand_user");
//			String jpushId = User.dao.findById(userId).get("user_jpushid");
//			JPushUtils.sendNotification("对方接受了你的邀请", "New Message", jpushId);
//			return "true";
//		}
//		return "false";
//	}
	
	public String acceptApply(String teamId, String demandId, String applyInfo) {
		if(new AppointmentDao().acceptInvite(teamId, demandId, applyInfo)) {
			int demandUser = DemandInfo.dao.findById(demandId).getInt("demand_user");
			int isInvite = ApplyInfo.dao.findById(applyInfo).getInt("isInvite");
			String jpushId = "";
			if(isInvite == 0) {
//			int userId = Team.dao.findById(teamId).getInt("team_captain");
			jpushId += User.dao.findById(demandUser).get("user_jpushid");
			}else {
				int userId = Team.dao.findById(teamId).getInt("team_captain");
				jpushId += User.dao.findById(userId).get("user_jpushid");
			}
			JPushUtils.sendNotification("你的约球有了最新的进度", "New Message", jpushId);
			return "true";
		}
		return "false";
	}
	
	public String refuse(String id) {
		if(new AppointmentDao().refuse(id))
			return "true";
		return "false";
	}
	
	public String change(String teama, String teamb, String userId) {
		if(new AppointmentDao().change(teama, teamb, userId))
			return "true";
		return "false";
	}
	
}
