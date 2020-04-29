package com.enjoyball.user.server;

import java.util.List;

import com.enjoyball.entity.User;
import com.enjoyball.entity.UserFans;
import com.enjoyball.user.dao.UserDao;
import com.google.gson.Gson;
import com.jfinal.kit.JsonKit;

public class UserServer {
	public String follow(String jsonStr) {
		return new UserDao().follow(jsonStr);
	}
	
	public int attention(String id) {
		return new UserDao().attention(id);
	}
	
	public String findUser(String id) {
		Integer i = Integer.parseInt(id);
		return new UserDao().findUserById(i);
	}
	
	public String login(String phone, String pwd) {
		return new UserDao().login(phone, pwd);
	}
	
	public String findByPhoneNumber(String phone) {
		return new UserDao().findByPhoneNumber(phone);
	}
	
	public String findByTeamId(String id) {
		List<User> list = new UserDao().findByTeamId(id);
		return JsonKit.toJson(list);
	}
	
	public String findByDTeamId(String id) {
		List<User> list = new UserDao().findByDTeamId(id);
		return JsonKit.toJson(list);
	}
	
	public String followNum(String id) {
		return new UserDao().followNum(id).size() + "";
	}
	
	public String getfollow(String id) {
		List<User> list = new UserDao().getfollow(id);
		if(list == null || list.size() == 0)
			return "false";
		return JsonKit.toJson(list);
	}
	
	public String getFans(String id) {
		List<User> list = new UserDao().getFans(id);
		if(list == null || list.size() == 0)
			return "false";
		return JsonKit.toJson(list);
	}
	
	public String update(String info) {
		User u = new Gson().fromJson(info, User.class);
		if(new UserDao().update(u))
			return "true";
		else
			return "false";
	}

	public String register(String info) {
		User u = new Gson().fromJson(info,User.class);
		if(new UserDao().register(u))
			return "true";
		else
			return "false";
	}
	
	public String updatePwd(String phone, String pwd) {
		if(new UserDao().updatePwd(phone, pwd))
			return "true";
		else
			return "false";
	}
	
	public String findManyUser(String phone) {
		List<User> list = new UserDao().findManyUser(phone);
		if(list == null || list.size() == 0)
			return "false";
		return JsonKit.toJson(list);
	}
	
	public String findByUserName(String name) {
		List<User> list = new UserDao().findByUserName(name);
		if(list == null || list.size() == 0)
			return "false";
		return JsonKit.toJson(list);
	}
	
	public String updateJpush(String phone, String jpushId) {
		if(new UserDao().updateJpush(phone, jpushId))
			return "true";
		return "false";
	}
	
	public String stopFollow(String info) {
		UserFans userFans = new Gson().fromJson(info, UserFans.class);
		if(new UserDao().stopFollow(userFans))
			return "true";
		return "false";
	}
	
	public String buyNamecard(String id,String num){
		if(new UserDao().buyNamecard(id, num)){
			return "true";
		}
		return "false";
	}
}
