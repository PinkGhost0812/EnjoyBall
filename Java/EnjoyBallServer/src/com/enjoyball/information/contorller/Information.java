package com.enjoyball.information.contorller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.enjoyball.information.server.CommentService;
import com.jfinal.core.Controller;

public class Information extends Controller {
	public void comment() {
		System.out.println("comment");
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream out = response.getOutputStream();
			out.print(new CommentService().commentList());
			out.flush();
			out.close();
			renderNull();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
