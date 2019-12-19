package com.enjoyball.news.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.enjoyball.news.server.NewsService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class NewsController extends Controller{
	public void like() {
		System.out.println("newsLike");
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream out = response.getOutputStream();
			Integer id = getParaToInt("id");
			
			Record news = Db.findById("news_info","news_id", id);
			Integer likeNum = news.getInt("news_likenum");
			news.set("news_likenum", likeNum + 1);
			Db.update("news_info","news_id", news);
//			List<Record> news = Db.find("select * from news_info where news_id = ?",new Object[] {id});
//			int likeNum = news.get(0).getInt("news_likenum");
//			System.out.println(likeNum);
//			news.get(0).set("news_id", id).set("news_likenum", likeNum + 1);
//			Db.update("news_info",news.get(0));
//			Db.update("update news_info set news_likenum = ? where news_id = ?",likeNum+1,id);
			out.print("true");
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void list() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String ans = new NewsService().list();
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
			String ans = new NewsService().findById(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findByContest() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new NewsService().findByContest(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void collect() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String info = getPara("info");
			String ans = new NewsService().collect(info);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findByCls() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String cls = getPara("cls");
			String ans = new NewsService().findByCls(cls);
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
			String id = getPara("id");
			String ans = new NewsService().findByUserId(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void unCollect() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String info = getPara("info");
			String ans = new NewsService().unCollect(info);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void collectType() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String info = getPara("info");
			String ans = new NewsService().collectType(info);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getThree() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String ans = new NewsService().threeNews();
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void newsPage() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			int page = getInt("page");
			String ans = new NewsService().newsPage(page);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void newsPageByClass() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			int page = getInt("page");
			int cls = getInt("cls");
			String ans = new NewsService().newsPageByClass(page,cls);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void search() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String content = getPara("content");
			int page = getInt("page");
			String ans = new NewsService().search(page,content);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
