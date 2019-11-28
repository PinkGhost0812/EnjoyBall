package com.enjoyball.news.controller;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
}
