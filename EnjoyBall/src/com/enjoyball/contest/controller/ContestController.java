package com.enjoyball.contest.controller;

import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.enjoyball.contest.server.ContestServer;
import com.jfinal.core.Controller;

public class ContestController extends Controller {
	public void list() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			int page = getInt("page");
			String ans = new ContestServer().list(page);
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
			String cls = getPara("cls");
			int page = getInt("page");
			String ans = new ContestServer().find(cls,page);
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
			String ans = new ContestServer().add(info);
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
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new ContestServer().findById(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
