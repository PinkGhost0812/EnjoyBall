package com.enjoyball.information.server;

import java.util.ArrayList;
import java.util.List;

import com.enjoyball.entity.Comment;
import com.enjoyball.information.dao.CommentDao;
import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Record;

public class CommentService {
	public String commentList() {
		List<Record> list = new CommentDao().commentList();
		List<Comment> commentList = new ArrayList<>();
		for(int i = 0 ; i < list.size() ; i++) {
			Comment comment = new Comment(
					list.get(i).getInt("comment_id"),
					list.get(i).getInt("comment_author"),
					list.get(i).getInt("comment_class"),
					list.get(i).getStr("comment_content"),
					list.get(i).getInt("comment_likenum"),
					list.get(i).getDate("comment_time"),
					list.get(i).getInt("comment_belone")
				);
			commentList.add(comment);
		}
		return new Gson().toJson(commentList);
	}
}
