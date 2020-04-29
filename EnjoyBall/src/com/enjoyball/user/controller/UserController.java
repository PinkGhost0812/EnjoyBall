package com.enjoyball.user.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enjoyball.entity.User;
import com.enjoyball.user.dao.UserDao;
import com.enjoyball.user.server.UserServer;
import com.jfinal.core.Controller;

public class UserController extends Controller {
	
	public void follow() {
		System.out.println("attention");
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream out = response.getOutputStream();
			String userFans = get("userFans");
			String ans = new UserServer().follow(userFans);
			out.print(ans);
			out.close();
			renderNull();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fans() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream out = response.getOutputStream();
			String id = getPara("id");
			int ans = new UserServer().attention(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void find() {
		System.out.println("find");
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String id = get("id");
			String u = new UserServer().findUser(id);
			out.print(u);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findByPhoneNumber() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String phone = get("phone");
			String u = new UserServer().findByPhoneNumber(phone);
			out.print(u);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login() {
		System.out.println("user login");
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream out = response.getOutputStream();
			String phone = getPara("phone");
			String pwd = getPara("pwd");
			String ans = new UserServer().login(phone, pwd);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findByTeamId() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("teamId");
			String ans = new UserServer().findByTeamId(id);
			out.print(ans);
			out.close();
			renderNull();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findByDTeamId() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new UserServer().findByDTeamId(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void follownum() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new UserServer().followNum(id);
			
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getfollow() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new UserServer().getfollow(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getfans() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String ans = new UserServer().getFans(id);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String info = getPara("info");
			String ans = new UserServer().update(info);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void register() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String info = getPara("info");
			String ans = new UserServer().register(info);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void uploadImg() {
		System.out.println("userUploadImg");
		try {
			HttpServletResponse response = getResponse();
			HttpServletRequest request = getRequest();
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
//			UploadFile uf = getFile();
//			System.out.println(uf.getFileName());
			String id = getPara("id");
			InputStream is = request.getInputStream();
			File file = new File("E://img//u" + id + ".png");
			if( !file.exists()) {
				if(file.createNewFile())
					System.out.println("create");
			}
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[2014];
			int len = 0;
			while((len = is.read(buffer)) != -1){
				fos.write(buffer,0,len);
			}
			User.dao.set("user_headportrait", "img/u"+id+".png").update();
			out.print("true");
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updatePwd() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String phone = getPara("phone");
			String pwd = getPara("pwd");
			String ans = new UserServer().updatePwd(phone,pwd);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findManyUser() {
		PrintWriter out = null;
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			String phone = getPara("phone");
			String ans = new UserServer().findManyUser(phone);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			if(out != null)
				out.close();
			e.printStackTrace();
		}
	}
	
	public void findByUserName() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String name = getPara("name");
			String ans = new UserServer().findByUserName(name);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateJpush() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String phone = getPara("phone");
			String jpushId = getPara("jpushId");
			String ans = new UserServer().updateJpush(phone,jpushId);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopFollow() {
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String info = getPara("userFans");
			String ans = new UserServer().stopFollow(info);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buyNamecard(){
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String id = getPara("id");
			String num = getPara("num");
			String ans = new UserServer().buyNamecard(id, num);
			out.print(ans);
			out.close();
			renderNull();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
