package com.innouni.south.entity;


/**
 * 用户信息实体类
 * @author HuGuojun
 * @data 2013-11-18
 */
public class UserInfo {
	
	private Object userId;
	private Object userName;
	private Object vip;
	private Object email;
	private Object phone;
	private Object isBindQQ;
	private Object isBindWeibo;

	public Object getUserId() {
		return userId;
	}

	public Object getVip() {
		return vip;
	}

	public void setVip(Object vip) {
		this.vip = vip;
	}

	public Object getIsBindQQ() {
		return isBindQQ;
	}

	public void setIsBindQQ(Object isBindQQ) {
		this.isBindQQ = isBindQQ;
	}

	public Object getIsBindWeibo() {
		return isBindWeibo;
	}

	public void setIsBindWeibo(Object isBindWeibo) {
		this.isBindWeibo = isBindWeibo;
	}

	public void setUserId(Object userId) {
		this.userId = userId;
	}

	public Object getUserName() {
		return userName;
	}

	public void setUserName(Object userName) {
		this.userName = userName;
	}

	public Object getEmail() {
		return email;
	}

	public void setEmail(Object email) {
		this.email = email;
	}

	public Object getPhone() {
		return phone;
	}

	public void setPhone(Object phone) {
		this.phone = phone;
	}
	
}
