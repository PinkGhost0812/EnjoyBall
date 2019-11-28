package com.enjoyball.user.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.enjoyball.entity.UserFans;
import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class UserController extends Controller {
	
	public void attention() {
		System.out.println("attention");
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream out = response.getOutputStream();
			String userFans = get("userFans");
			UserFans ufs = new Gson().fromJson(userFans, UserFans.class);
			List<Record> list = Db.find("select * from user_fans where user_id = ? and fans_id = ?",ufs.getUser_id(),ufs.getFans_id());
			if(list.size() == 0) {
				Record uf = new Record().set("user_id", ufs.getUser_id()).set("fans_id", ufs.getFans_id());
				Db.save("user_fans", uf);
				out.print("true");
			}else {
				out.print("flase");
			}
			renderNull();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
