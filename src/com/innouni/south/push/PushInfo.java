package com.innouni.south.push;

/**
 * 推送消息封装类
 * @author HuGuojun
 * @data 2013-10-31
 */
public class PushInfo {
	
	private String content;
	private String expertId;
	private String expertName;
	private String userId;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getExpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	public String getExpertName() {
		return expertName;
	}
	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
