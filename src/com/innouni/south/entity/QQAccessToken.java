package com.innouni.south.entity;


/**
 * 自定义类封装QQ的AccessToken
 * @author HuGuojun
 * @data 2013-08-21
 */
public class QQAccessToken {

	private String openId;
	private String accessToken;
	private long expiresIn;
	
	public QQAccessToken() {}

	public QQAccessToken(String openId, String accessToken, long expiresIn) {
		this.openId = openId;
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	
}
