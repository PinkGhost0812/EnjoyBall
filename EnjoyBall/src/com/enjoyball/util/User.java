package com.enjoyball.util;

import java.io.Serializable;

public class User implements Serializable {
    private Integer user_id;
    private String user_nickname;
    private String user_password;
    private String user_headportrait;
    private String user_sex;
    private String user_phonenumber;
    private String user_address;
    private String user_email;
    private String user_signature;
    private Integer user_score;
    private Integer user_likenum;
    private Integer user_vip;
    private Integer user_age;
    private String user_jpushid;
    private Integer user_namecard;
    
	public User() {
	}
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_nickname=" + user_nickname + ", user_password=" + user_password
				+ ", user_headportrait=" + user_headportrait + ", user_sex=" + user_sex + ", user_phonenumber="
				+ user_phonenumber + ", user_address=" + user_address + ", user_email=" + user_email
				+ ", user_signature=" + user_signature + ", user_score=" + user_score + ", user_likenum=" + user_likenum
				+ ", user_vip=" + user_vip + ", user_age=" + user_age + ", user_jpushid=" + user_jpushid
				+ ", user_namecard=" + user_namecard + "]";
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUser_nickname() {
		return user_nickname;
	}
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_headportrait() {
		return user_headportrait;
	}
	public void setUser_headportrait(String user_headportrait) {
		this.user_headportrait = user_headportrait;
	}
	public String getUser_sex() {
		return user_sex;
	}
	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}
	public String getUser_phonenumber() {
		return user_phonenumber;
	}
	public void setUser_phonenumber(String user_phonenumber) {
		this.user_phonenumber = user_phonenumber;
	}
	public String getUser_address() {
		return user_address;
	}
	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_signature() {
		return user_signature;
	}
	public void setUser_signature(String user_signature) {
		this.user_signature = user_signature;
	}
	public Integer getUser_score() {
		return user_score;
	}
	public void setUser_score(Integer user_score) {
		this.user_score = user_score;
	}
	public Integer getUser_likenum() {
		return user_likenum;
	}
	public void setUser_likenum(Integer user_likenum) {
		this.user_likenum = user_likenum;
	}
	public Integer getUser_vip() {
		return user_vip;
	}
	public void setUser_vip(Integer user_vip) {
		this.user_vip = user_vip;
	}
	public Integer getUser_age() {
		return user_age;
	}
	public void setUser_age(Integer user_age) {
		this.user_age = user_age;
	}
	public String getUser_jpushid() {
		return user_jpushid;
	}
	public void setUser_jpushid(String user_jpushid) {
		this.user_jpushid = user_jpushid;
	}
	public Integer getUser_namecard() {
		return user_namecard;
	}
	public void setUser_namecard(Integer user_namecard) {
		this.user_namecard = user_namecard;
	}
	public User(Integer user_id, String user_nickname, String user_password, String user_headportrait, String user_sex,
			String user_phonenumber, String user_address, String user_email, String user_signature, Integer user_score,
			Integer user_likenum, Integer user_vip, Integer user_age, String user_jpushid, Integer user_namecard) {
		super();
		this.user_id = user_id;
		this.user_nickname = user_nickname;
		this.user_password = user_password;
		this.user_headportrait = user_headportrait;
		this.user_sex = user_sex;
		this.user_phonenumber = user_phonenumber;
		this.user_address = user_address;
		this.user_email = user_email;
		this.user_signature = user_signature;
		this.user_score = user_score;
		this.user_likenum = user_likenum;
		this.user_vip = user_vip;
		this.user_age = user_age;
		this.user_jpushid = user_jpushid;
		this.user_namecard = user_namecard;
	}

   
}
