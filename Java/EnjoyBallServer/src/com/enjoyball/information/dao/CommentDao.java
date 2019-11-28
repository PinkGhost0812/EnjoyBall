package com.enjoyball.information.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class CommentDao {
	public List<Record> commentList(){
		List<Record> list = Db.find("select * from comment_info");
		
		return list;
	}
}
