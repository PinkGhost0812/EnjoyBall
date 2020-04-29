package com.enjoyball.information.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enjoyball.entity.Comment;
import com.enjoyball.entity.News;
import com.enjoyball.util.AuthorAndComment;
import com.enjoyball.util.CommentAndNews;
import com.enjoyball.util.DbUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.kit.JsonKit;

public class CommentDao {
	
	public List<Comment> findCommentByNewsId(int id){
		return Comment.dao.find("select * from comment_info where comment_belone = 1");
	}
	
	public List<Comment> find(String sql){
		List<Comment> list = Comment.dao.find(sql);
		return list;
	}
	
	public int count(String id) {
		List<Comment> list = Comment.dao.find("select * from comment_info where comment_author = ?",id);
		return list.size();
	}

	public List<Comment> findByUserId(String id){
		List<Comment> list = Comment.dao.find("select * from comment_info where comment_author = ?", id);
		return list;
	}
	
	public List<CommentAndNews> findSaying(String id){
		List<CommentAndNews> list = new ArrayList<CommentAndNews>();
		List<Object> commentList = DbUtil.findAllWithWhere("select * from comment_info where comment_author = ?",
				com.enjoyball.util.Comment.class, new Object[] {id});
		for(int i = 0 ; i < commentList.size() ; i++) {
			com.enjoyball.util.Comment comment = (com.enjoyball.util.Comment) commentList.get(i);
			List<Object> newsList = DbUtil.findAllWithWhere("select * from news_info where news_id = ?",
					com.enjoyball.util.News.class, new Object[] {comment.getComment_belone()});
			if(newsList != null && newsList.size() != 0) {
				com.enjoyball.util.News n = (com.enjoyball.util.News) newsList.get(0);
				CommentAndNews can = new CommentAndNews();
				can.setComment(comment);
				can.setNews(n);
				list.add(can);
			}
		}
		return list;
		
//		List<Map<String,News>> list = new ArrayList<Map<String,News>>();
//		List<Comment> commentList = Comment.dao.find("select * from comment_info where comment_author = ?", id);
//		for(int i = 0 ; i < commentList.size() ; i++) {
//			News n = News.dao.findById(commentList.get(i).getInt("comment_belone"));
//			Map<String,News> map = new HashMap<String, News>();
//			map.put(commentList.get(i).getStr("comment_content"), n);
//			list.add(map);
//		}
//		return list;
	}
	
//	public List<Map<Comment,User>> getCommentAndAuthor(String id){
//		List<Map<Comment,User>> list = new ArrayList<Map<Comment,User>>();
//		List<Comment> commentList = Comment.dao.find("select * from comment_info where comment_belone = ?", id);
//		String ans = JsonKit.toJson(commentList);
//		System.out.println(ans);
//		for(int i = 0 ; i < commentList.size() ; i++) {
//			User u = User.dao.findById(commentList.get(i).getInt("comment_author"));
//			Map<Comment,User> map = new HashMap<Comment, User>();
//			map.put(commentList.get(i), u);
//			list.add(map);
//		}
//		System.out.println(JsonKit.toJson(list));
//		return list;
//	}
	
	public String getCommentAndAuthor(String id){
		List<AuthorAndComment> list = new ArrayList<AuthorAndComment>();
		List<Object> commentList = DbUtil.findAllWithWhere("select * from comment_info where comment_belone = ?", 
				com.enjoyball.util.Comment.class, new Object[] {id});
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i = 0 ; i < commentList.size() ; i++) {
			AuthorAndComment aac = new AuthorAndComment();
			com.enjoyball.util.Comment comment = (com.enjoyball.util.Comment) commentList.get(i);
			List<Object> user = DbUtil.findAllWithWhere("select * from user_info where user_id = ?", 
					com.enjoyball.util.User.class, new Object[]{comment.getComment_author()});
			com.enjoyball.util.User u = (com.enjoyball.util.User) user.get(0);
			try {
				Date date = df.parse(comment.getComment_time().toString());
//				System.out.println("date = " + comment.getComment_time().toString());
//				System.out.println("parse = " + date);
				comment.setComment_time(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println(comment.getComment_time().toString());
			aac.setComment(comment);
			aac.setAuthor(u);
			list.add(aac);
		}
		
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		System.out.println("gson = " + gson.toJson(list));
		return gson.toJson(list);
	}
	
	public String getCommentAndAuthorByContest(String id){
		List<AuthorAndComment> list = new ArrayList<AuthorAndComment>();
		List<Object> commentList = DbUtil.findAllWithWhere("select * from comment_info where comment_contest = ?", 
				com.enjoyball.util.Comment.class, new Object[] {id});
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i = 0 ; i < commentList.size() ; i++) {
			AuthorAndComment aac = new AuthorAndComment();
			com.enjoyball.util.Comment comment = (com.enjoyball.util.Comment) commentList.get(i);
			List<Object> user = DbUtil.findAllWithWhere("select * from user_info where user_id = ?", 
					com.enjoyball.util.User.class, new Object[]{comment.getComment_author()});
			com.enjoyball.util.User u = (com.enjoyball.util.User) user.get(0);
			try {
				Date date = df.parse(comment.getComment_time().toString());
//				System.out.println("date = " + comment.getComment_time().toString());
//				System.out.println("parse = " + date);
				comment.setComment_time(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println(comment.getComment_time().toString());
			aac.setComment(comment);
			aac.setAuthor(u);
			list.add(aac);
		}
		
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		System.out.println("gson = " + gson.toJson(list));
		return gson.toJson(list);
	}
	

	public String addComment(Comment comment) {
		int id = DbUtil.executeUpdate("insert into comment_info (comment_author,comment_class,"
				+ "comment_content, comment_likenum, comment_time, comment_belone, comment_contest) "
				+ "values(?,?,?,?,?,?,?)", new Object[] {comment.getComment_author(),comment.getComment_class(),
						comment.getComment_content(),comment.getComment_likenum(), comment.getComment_time(), 
						comment.getComment_belone(), comment.getComment_contest()});
//		boolean flag = comment.set("comment_author", comment.getComment_author()).set("comment_class", comment.getComment_class())
//			.set("comment_content", comment.getComment_content()).set("comment_likenum", comment.getComment_likenum())
//			.set("comment_time", comment.getComment_time()).set("comment_belone", comment.getComment_belone())
//			.set("comment_contest", comment.getComment_contest()).save();
		if(id != -1) {
			Comment com = Comment.dao.findById(id);
			return JsonKit.toJson(com);
		}
		return "false";
	}
	
	public List<Comment> findByContestId(String id) {
		List<Comment> list = Comment.dao.find("select * from comment_info where comment_contest = ?",id);
		return list;
	}
	
	public boolean likeComment(String id) {
		Comment comment = Comment.dao.findById(id);
		int likeNum = comment.get("comment_likenum");
		return comment.set("comment_likenum", ++likeNum).update();
	}
	
	public String delete(String id){
		int tag=DbUtil.executeUpdate
				("delete from comment_info where comment_id = ?", 
						new Object[] {id});
		
		System.out.println(tag);
		
		if(tag==0){
			return "false";
		}else{
			return "true";
		}
		
	}

}
