package com.enjoyball.information.contorller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.enjoyball.entity.Comment;
import com.enjoyball.entity.News;
import com.enjoyball.information.server.CommentService;
import com.enjoyball.util.JPushUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfinal.core.Controller;

public class Information extends Controller {
	
	public void newscomment() {
		System.out.println("newsComment");
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			String id = getPara("belone");
			PrintWriter out = response.getWriter();
			String ans = new CommentService().findCommentByNewsId(id);
			System.out.println("ans = " + ans);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			List<Comment> list = gson.fromJson(ans,new TypeToken<List<Comment>>(){}.getType());
			System.out.println(list.toString());
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
			ServletOutputStream out = response.getOutputStream();
			String sql = get("info");
			String ans = new CommentService().find(sql);
			out.print(ans);
			out.close();
			renderNull();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void count() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream out = response.getOutputStream();
			String id = get("id");
			Integer ans = new CommentService().count(id);
			out.print(ans);
			out.close();
			renderNull();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void findByUserId() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new CommentService().findByUserId(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findSaying() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new CommentService().findSaying(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getCommentAndAuthor() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new CommentService().getCommentAndAuthor(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getCommentAndAuthorByContest() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new CommentService().getCommentAndAuthorByContest(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addComment() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String info = getPara("info");
			String ans = new CommentService().addComment(info);
			System.out.println(ans);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findByContestId() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new CommentService().findByContestId(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendJpush() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("key", "value");
		JPushUtils.sendAllNotification("this is a message test",map,"1104a89792c36f7660a");
		renderNull();
	}

	public void likeComment() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new CommentService().likeComment(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delete(){
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("commentId");
			String ans = new CommentService().delete(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
