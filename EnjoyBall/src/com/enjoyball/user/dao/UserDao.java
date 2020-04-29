package com.enjoyball.user.dao;

import java.util.ArrayList;
import java.util.List;

import com.enjoyball.entity.DTeamMember;
import com.enjoyball.entity.TeamMember;
import com.enjoyball.entity.User;
import com.enjoyball.entity.UserFans;
import com.enjoyball.util.DbUtil;
import com.google.gson.Gson;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class UserDao {
	public String follow(String userFans) {
		UserFans ufs = new Gson().fromJson(userFans, UserFans.class);
		List<Record> list = Db.find("select * from user_fans where user_id = ? and fans_id = ?",ufs.getUser_id(),ufs.getFans_id());
		if(list.size() == 0) {
			Record uf = new Record().set("user_id", ufs.getUser_id()).set("fans_id", ufs.getFans_id());
			Db.save("user_fans", uf);
			return "true";
		}else {
			return "false";
		}
	}
	
	public int attention(String id) {
		List<UserFans> list = UserFans.dao.find("select * from user_fans where user_id = ?", id);
		return list.size();
	}
	
	public String findUserById(Integer id) {
		User u = User.dao.findById(id);
		return JsonKit.toJson(u);
	}
	
	public String login(String phone, String pwd) {
		List<Record> user = Db.find("select * from user_info where user_phonenumber = ?",phone);
		for(int i = 0 ; i < user.size() ; i++) {
			if(user.get(i).get("user_password").equals(pwd)){
				return "true";
			}
		}
		return "false";
	}
	
	public String findByPhoneNumber(String phone) {
		List<User> u = User.dao.find("select * from user_info where user_phonenumber = ?", phone);
		if(u.size() == 0)
			return "false";
		return JsonKit.toJson(u.get(0));
	}
	
	public List<User> findByTeamId(String id){
		List<User> list = new ArrayList<>();
		List<TeamMember> idList = TeamMember.dao.find("select * from team_member where team_id = ?", id);
		for(int i = 0 ; i < idList.size() ; i++) {
			User u = User.dao.findById(idList.get(i).getInt("member_id"));
			list.add(u);
		}
		return list;
	}
	
	public List<User> findByDTeamId(String id){
		List<User> list = new ArrayList<>();
		System.out.println(id);
		List<DTeamMember> idList = DTeamMember.dao.find("select * from dteam_member where dteam_id = ?", id);
		for(int i = 0 ; i < idList.size() ; i++) {
			User u = User.dao.findById(idList.get(i).getInt("member_id"));
			list.add(u);
		}
		return list;
	}
	
	public List<UserFans> followNum(String id){
		List<UserFans> list = UserFans.dao.find("select * from user_fans where fans_id = ?",id);
		return list;
	}
	
	public List<User> getfollow(String id){
		List<UserFans> idList = UserFans.dao.find("select * from user_fans where fans_id = ?", id);
		if(idList.size() == 0)
			return null;
		List<User> list = new ArrayList<User>();
		for(int i = 0 ; i < idList.size() ; i++) {
			User u = User.dao.findById(idList.get(i).getInt("user_id"));
			list.add(u);
		}
		return list;
	}
	
	public List<User> getFans(String id){
		List<UserFans> idList = UserFans.dao.find("select * from user_fans where user_id = ?", id);
		if(idList.size() == 0)
			return null;
		List<User> list = new ArrayList<User>();
		for(int i = 0 ; i < idList.size() ; i++) {
			User u = User.dao.findById(idList.get(i).getInt("fans_id"));
			list.add(u);
		}
		return list;
	}
	
	public boolean update(User u) {
		boolean flag = u.set("user_id", u.getUser_id()).set("user_nickname", u.getUser_nickname()).set("user_sex", u.getUser_sex()).set("user_age", u.getUser_age())
			.set("user_email", u.getUser_email()).set("user_signature", u.getUser_signature()).set("user_age", u.getUser_age()).update();
		return flag;
	}
	
	public boolean register(User u) {
		List<User> list = User.dao.find("select * from user_info where user_phonenumber = ?",u.getUser_phonenumber());
		if(list.size() != 0)
			return false;
		return u.set("user_namecard", 0)
				.set("user_phonenumber", u.getUser_phonenumber())
				.set("user_password", u.getUser_password())
				.set("user_age", 0)
				.set("user_sex", "不告诉你")
				.set("user_nickname", "取个名字吧")
				.set("user_email", "@lff.com")
				.set("user_address", "可能是地球吧")
				.set("user_headportrait", "img/default.png")
				.set("user_signature", "这个人很懒，什么都没留下")
				.set("user_score", 0)
				.set("user_likenum", 0)
				.set("user_vip", 0)
				.set("user_jpushid", u.getUser_jpushid())
				.save();
	}
	
	public boolean updatePwd(String phone, String pwd) {
		System.out.println("pwd = " + pwd + " phone =  " + phone);
		int x = DbUtil.executeUpdate("update user_info set user_password = ? where user_phonenumber = ?",
				new Object[] {pwd,phone});
		if(x != -1)
			return true;
		else
			return false;
	}
	
	public List<User> findManyUser(String phone){
		List<User> list = User.dao.find("select * from user_info where user_phonenumber like '%" + phone + "%' ");
		return list;
	}
	
	public List<User> findByUserName(String name){
		List<User> list = User.dao.find("select * from user_info where user_nickname like '%" + name + "%'");
		return list;
	}
	
	public boolean updateJpush(String phone, String jpushId) {
		int x = DbUtil.executeUpdate("update user_info set user_jpushId = ? where user_phonenumber = ?", new Object[] {jpushId,phone});
		if(x != -1)
			return true;
		return false;
	}
	
	public boolean stopFollow(UserFans userFans) {
		int x = DbUtil.executeUpdate("delete from user_fans where user_id = ? and fans_id = ?", 
				new Object[] {userFans.getUser_id(),userFans.getFans_id()});
		if(x != -1 && x != 0) 
			return true;
		return false;
	}
	
	public boolean buyNamecard(String id,String num){
		int tag= DbUtil.executeUpdate
				("update user_info set user_namecard =user_namecard+? where user_id = ?",
						new Object[] {num,id});
		if(tag==1){
			return true;
		}else{
			return false;
		}
	}
	
}
