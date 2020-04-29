package com.enjoyball.information.server;

import java.util.List;
import java.util.Map;

import com.enjoyball.entity.Comment;
import com.enjoyball.entity.News;
import com.enjoyball.entity.User;
import com.enjoyball.information.dao.CommentDao;
import com.enjoyball.util.AuthorAndComment;
import com.enjoyball.util.CommentAndNews;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.kit.JsonKit;

public class CommentService {
	
	public String findCommentByNewsId(String id) {
		Integer i = Integer.parseInt(id);
//		System.out.println("service = " + new CommentDao().findCommentByNewsId(i).toString());
//		System.out.println("serviceGson = " + new Gson().toJson(new CommentDao().findCommentByNewsId(i)));
		return JsonKit.toJson(new CommentDao().findCommentByNewsId(i));
	}
	
	public String find(String sql) {
		List<Comment> list = new CommentDao().find(sql);
		return JsonKit.toJson(list);
	}

	public int count(String id) {
		return new CommentDao().count(id);
	}

	public String findByUserId(String id) {
		List<Comment> list = new CommentDao().findByUserId(id);
		if(list.size() == 0)
			return "false";
		return JsonKit.toJson(list);
	}
	
	public String findSaying(String id) {
		List<CommentAndNews> list = new CommentDao().findSaying(id);
		if(list.size() == 0)
			return "false";
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.toJson(list);
	}
	
//	public String getCommentAndAuthor(String id) {
//		List<Map<Comment,User>> list = new CommentDao().getCommentAndAuthor(id);
//		return JsonKit.toJson(list);
//	}
	
	public String getCommentAndAuthor(String id) {
//		List<AuthorAndComment> list = new CommentDao().getCommentAndAuthor(id);
		return new CommentDao().getCommentAndAuthor(id);
	}
	
	public String getCommentAndAuthorByContest(String id) {
		return new CommentDao().getCommentAndAuthorByContest(id);
	}
	
	public String addComment(String info) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Comment comment = gson.fromJson(info, Comment.class);
		return new CommentDao().addComment(comment);
	}
	
	public String findByContestId(String id) {
		List<Comment> list = new CommentDao().findByContestId(id);
		return JsonKit.toJson(list);
	}
	
	public String likeComment(String id) {
		if(new CommentDao().likeComment(id))
			return "true";
		else
			return "false";
	}
	
	public String delete(String id){
		return new CommentDao().delete(id);
	}
	
}
