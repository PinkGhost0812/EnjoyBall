package com.enjoyball.news.dao;

import java.util.ArrayList;
import java.util.List;

import com.enjoyball.entity.Collection;
import com.enjoyball.entity.News;
import com.enjoyball.util.DbUtil;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

public class NewsDao {
	public String list() {
		List<News> newsList = News.dao.find("select * from news_info");
		return JsonKit.toJson(newsList);
	}
	
	public News findByID(String id) {
		News news = News.dao.findById(id);
		return news;
	}
	
	public List<News> findByContest(String id){
		List<News> list = News.dao.find("select * from news_info where news_game = ?", id);
		return list;
	}
	
	public boolean collect(Collection collection) {
		List<Object> list = DbUtil.findAllWithWhere("select * from user_collection where news_id = ? and user_id = ?", 
				com.enjoyball.util.Collection.class,new Object[] {collection.getNew_id(),collection.getUser_id()});
		if(list.size() == 0)
			return collection.set("news_id", collection.getNew_id()).set("user_id", collection.getUser_id()).save();
		return false;
	}
	
	public List<News> findByCls(String cls) {
		List<News> list = News.dao.find("select * from news_info where news_class = ?",cls);
		return list;
	}
	
	public List<com.enjoyball.util.News> findByUserId(String id){
		List<com.enjoyball.util.News> list = new ArrayList<>();
		List<Collection> collecList = Collection.dao.find("select * from user_collection where user_id = ?",id);
		for(int i = 0 ; i < collecList.size() ; i++) {
			List<Object> news = DbUtil.findAllWithWhere("select * from news_info where news_id = ?", com.enjoyball.util.News.class, 
					new Object[] {collecList.get(i).get("news_id")});
			if(news.size() != 0)
				list.add((com.enjoyball.util.News)news.get(0));
		}
		return list;
	}
	
	public int unCollect(Collection collection) {
		return DbUtil.executeUpdate("delete from user_collection where news_id = ? and user_id = ?", 
				new Object[] {collection.getNew_id(),collection.getUser_id()});
	}
	
	public boolean collectType(Collection collection) {
		List<Object> list = DbUtil.findAllWithWhere("select * from user_collection where news_id = ? and user_id = ?", 
				com.enjoyball.util.Collection.class,new Object[] {collection.getNew_id(),collection.getUser_id()});
		if(list.size() == 0)
			return false;
		return true;
	}
	
	public List<News> threeNews(){
		List<News> list = News.dao.find("select * from news_info order by news_heat desc limit 0, 3");
		return list;
	}
	
	public String newsPage(int page){
		Page<News> list = News.dao.paginate(page, 8, "select *","from news_info");
		List<News> newsList = list.getList();
		return JsonKit.toJson(newsList);
	}
	
	public List<News> newsPageByClass(int page, int cls){
		Page<News> list = News.dao.paginate(page, 8, "select *", "from news_info where news_class = ?",cls);
		List<News> newsList = list.getList();
		return newsList;
	}
	
	public List<News> search(int page, String content){
		System.out.println(content);
		Page<News> list = News.dao.paginate(page, 8, "select *", "from news_info where news_title like '%" + content + "%'");
		List<News> newsList = list.getList();
		return newsList;
	}
}
