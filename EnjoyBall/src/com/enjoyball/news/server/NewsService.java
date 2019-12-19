package com.enjoyball.news.server;

import java.util.List;

import com.enjoyball.entity.Collection;
import com.enjoyball.entity.News;
import com.enjoyball.news.dao.NewsDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.kit.JsonKit;

public class NewsService {
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	public String list() {
		return new NewsDao().list();
	}
	
	public String findById(String id) {
		return JsonKit.toJson(new NewsDao().findByID(id));
	}
	
	public String findByContest(String id) {
		List<News> list = new NewsDao().findByContest(id);
		return JsonKit.toJson(list);
	}
	
	public String collect(String info) {
		Collection collection = new Gson().fromJson(info, Collection.class);
		if(new NewsDao().collect(collection))
			return "true";
		else
			return "false";
	}
	
	public String findByCls(String cls) {
		List<News> list = new NewsDao().findByCls(cls);
		return JsonKit.toJson(list);
	}
	
	public String findByUserId(String id) {
		List<com.enjoyball.util.News> list = new NewsDao().findByUserId(id);
		return gson.toJson(list);
	}
	
	public String unCollect(String info) {
		Collection collec = gson.fromJson(info, Collection.class);
		int x = new NewsDao().unCollect(collec);
		if(x != -1)
			return "true";
		else
			return "false";
	}
	
	public String collectType(String info) {
		Collection collection = gson.fromJson(info,Collection.class);
		if(new NewsDao().collectType(collection))
			return "true";
		else
			return "false";
	}
	
	public String threeNews() {
		List<News> list = new NewsDao().threeNews();
		return JsonKit.toJson(list);
	}
	
	public String newsPage(int page) {
		return new NewsDao().newsPage(page);
	}
	
	public String newsPageByClass(int page, int cls) {
		List<News> list = new NewsDao().newsPageByClass(page, cls);
		return JsonKit.toJson(list);
	}
	
	public String search(int page, String content) {
		List<News> list = new NewsDao().search(page,content);
		return JsonKit.toJson(list);
	}
}
