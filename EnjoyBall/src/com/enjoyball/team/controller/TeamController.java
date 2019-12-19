package com.enjoyball.team.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enjoyball.entity.Team;
import com.enjoyball.team.server.TeamService;
import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;

public class TeamController extends Controller {
	
	public void find() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			HttpServletRequest request = getRequest();
			request.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String teamName = get("teamName");
			String ans = new TeamService().find(teamName);
			out.print(ans);
			out.close();
			renderNull();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void regist() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream out = response.getOutputStream();
			String teamInfo = get("info");
			Team team = new Gson().fromJson(teamInfo, Team.class);
			String ans = new TeamService().register(team);
			out.print(ans);
			out.close();
			renderNull();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void findByIdCls() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = get("id");
			String cls = getPara("cls");
			String ans = new TeamService().findByIdCls(id,cls);
			out.print(ans);
			out.close();
			renderNull();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void findById() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new TeamService().findById(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findByPerson() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new TeamService().findByPerson(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void uploadImg() {
		try {
			HttpServletResponse response = getResponse();
			HttpServletRequest request = getRequest();
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			InputStream is = request.getInputStream();
			File file = new File("E://img//t" + id + ".png");
			if( !file.exists()) {
				if(file.createNewFile())
					System.out.println("create");
			}
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[2014];
			int len = 0;
			while((len = is.read(buffer)) != -1){
				fos.write(buffer,0,len);
			}
			Team.dao.findById(id).set("team_logo", "img/t"+id+".png").update();
			out.print("true");
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findMember() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			if(id == null)
				return;
			String ans = new TeamService().findMember(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void dissolve() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			if(id == null)
				return;
			String ans = new TeamService().dissolve(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String info = getPara("info");
			if(info == null)
				return;
			String ans = new TeamService().update(info);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void invite() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
//			PrintWriter out = response.getWriter();
			String teamId = getPara("teamId");
			String userId = getPara("userId");
			if(teamId == null || userId == null)
				return;
			new TeamService().invite(teamId,userId);
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void inviteOk() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String teamId = getPara("teamId");
			String userId = getPara("userId");
			if(teamId == null || userId == null)
				return;
			String ans = new TeamService().inviteOk(teamId,userId);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gun() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String teamId = getPara("teamId");
			String userId = getPara("userId");
			if(teamId == null || userId == null)
				return;
			String ans = new TeamService().gun(teamId,userId);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void out() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String teamId = getPara("teamId");
			String userId = getPara("userId");
			if(teamId == null || userId == null)
				return;
			String ans = new TeamService().out(teamId,userId);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
