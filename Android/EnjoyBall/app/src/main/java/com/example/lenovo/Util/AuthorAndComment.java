package com.example.lenovo.Util;


import com.example.lenovo.entity.Comment;
import com.example.lenovo.entity.User;

public class AuthorAndComment{
	private Comment comment;
	private User author;

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
	
}
