package com.enjoyball.appointment.controller;

import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.enjoyball.appointment.server.AppointmentServer;
import com.jfinal.core.Controller;

public class AppointmentController extends Controller {
	public void list() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			int page = getInt("page");
			String ans = new AppointmentServer().list(page);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void find() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String sql = getPara("sql");
			String ans = new AppointmentServer().find(sql);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findByClass() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			int page = getInt("page");
			String cls = getPara("cls");
			String ans = new AppointmentServer().findByClass(page,cls);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void add() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream out = response.getOutputStream();
			String info = getPara("info");
			String ans = new AppointmentServer().add(info);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addAppointmentTeam() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream out = response.getOutputStream();
			String info = getPara("demandInfo");
			String idList = getPara("idList");
			String ans = new AppointmentServer().addFormalAppointment(info,idList);
			if(ans.equals(""))
				out.print("false");
			else
				out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findById() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new AppointmentServer().findById(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void apply() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String userId = getPara("userId");
			String teamId = getPara("teamId");
			String applyId = getPara("applyId");
			System.out.println(applyId);
			String ans = new AppointmentServer().apply(userId,teamId,applyId);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findByUserId() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String userId = getPara("id");
			String ans = new AppointmentServer().findByUserId(userId);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getDemandTeam() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String userId = getPara("id");
			String ans = new AppointmentServer().getDemandTeam(userId);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void applyToLeader() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String info = getPara("info");
			String id = getPara("id");
			String ans = new AppointmentServer().applyToLeader(info,id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void messageList() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new AppointmentServer().messageList(id);
			System.out.println(ans);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void teamA() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String demandId = getPara("demandId");
			String ans = new AppointmentServer().teamA(demandId);
			System.out.println(ans);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void teamB() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String demandId = getPara("demandId");
			String ans = new AppointmentServer().teamB(demandId);
			System.out.println(ans);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addAppointmentWithInvite() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String demandInfo = getPara("demandInfo");
			String idList = getPara("idList");
			String ans = new AppointmentServer().addAppointmentWithInvite(demandInfo,idList);
			System.out.println(ans);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public void acceptInvite() {
//		try {
//			HttpServletResponse response = getResponse();
//			response.setCharacterEncoding("UTF-8");
//			response.setContentType("text/html;charset=UTF-8");
//			PrintWriter out = response.getWriter();
//			String teamId = getPara("teamId");
//			String demandId = getPara("demandId");
//			String ans = new AppointmentServer().acceptInvite(teamId,demandId);
//			System.out.println(ans);
//			out.print(ans);
//			out.close();
//			renderNull();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public void acceptApply() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String teamId = getPara("teamId");
			String demandId = getPara("demandId");
			String applyInfo = getPara("applyId");
			String ans = new AppointmentServer().acceptApply(teamId,demandId,applyInfo);
			System.out.println(ans);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refuse() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new AppointmentServer().refuse(id);
			System.out.println(ans);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void change() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String teama = getPara("teama");
			String teamb = getPara("teamb");
			String userId = getPara("userId");
			String ans = new AppointmentServer().change(teama,teamb,userId);
			System.out.println(ans);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
