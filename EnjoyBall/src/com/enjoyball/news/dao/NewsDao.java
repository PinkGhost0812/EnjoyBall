package com.enjoyball.news.dao;

import java.util.ArrayList;
import java.util.List;

import com.enjoyball.entity.Collection;
import com.enjoyball.entity.Comment;
import com.enjoyball.entity.News;
import com.enjoyball.entity.User;
import com.enjoyball.user.controller.UserController;
import com.enjoyball.user.server.UserServer;
import com.enjoyball.util.CommentAndNews;
import com.enjoyball.util.DbUtil;
import com.enjoyball.util.NewsAndAuthor;
import com.enjoyball.util.NewsAndCommentNum;
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
	
	public List<com.enjoyball.util.NewsAndAuthor> findByUserId(String id){
		System.out.println(id);
		List<com.enjoyball.util.NewsAndAuthor> list = new ArrayList<>();
		List<Collection> collecList = Collection.dao.find
				("select * from user_collection where user_id = ?",id);
		System.out.println(collecList.toString());
		for(int i = 0 ; i < collecList.size() ; i++) {
			
			Collection collection= collecList.get(i);
			List<Object> newsList = DbUtil.findAllWithWhere("select * from news_info where news_id = ?",
					com.enjoyball.util.News.class, new Object[] {collection.get("news_id")});
			System.out.println(newsList.toString());
			if(newsList != null && newsList.size() != 0) {
				System.out.println(newsList.size()+"");
				System.out.println(i);
				com.enjoyball.util.News news = (com.enjoyball.util.News) newsList.get(0);
				//根据newsid查找作者
				String authorId=String.valueOf(news.getNews_author());
				List<Object> author = DbUtil.findAllWithWhere("select * from user_info where user_id = ?",
						com.enjoyball.util.User.class, new Object[] {authorId});
				NewsAndAuthor naa = new NewsAndAuthor();
				naa.setAuthor((com.enjoyball.util.User)author.get(0));
				naa.setNews(news);
				list.add(naa);
			}
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

	public int findCommentNumByNews(String id){

		List<Comment> list = Comment.dao.find("select * from comment_info where comment_belone = ?",id);
		return list.size();
	}
	
	public List<NewsAndCommentNum> findNewsAndCommentNumList(){
		
		List<NewsAndCommentNum> list=new ArrayList<>();
		
		List<Object> newsList = DbUtil.findAllWithWhere("select * from news_info",
				com.enjoyball.util.News.class, new Object[] {});

		for(int i=0;i<newsList.size();i++){
			System.out.println(newsList.get(i));
			NewsAndCommentNum cacn=new NewsAndCommentNum();
			cacn.setNews((com.enjoyball.util.News)newsList.get(i));
			int num=findCommentNumByNews(String.valueOf(cacn.getNews().getNews_id()));
			cacn.setCommentNum(num);
			list.add(cacn);
			System.out.println(list.get(i));
		}
		
		return list;
		
	}
}
