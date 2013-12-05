package com.innouni.south.entity;

/**
 * 新闻资讯(包括实时银讯，每日精评，全球市场，外汇要闻，世界财经，名家专栏)
 * 早中晚评论
 * 即时新闻
 * @author HuGuojun
 * @data 2013-09-04
 */
public class MarketNews {
	
	private Object id;
	private Object title;
	private Object content;
	private Object time;
	private Object description;
	private Object source;
	
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public Object getTitle() {
		return title;
	}
	public void setTitle(Object title) {
		this.title = title;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public Object getTime() {
		return time;
	}
	public void setTime(Object time) {
		this.time = time;
	}
	public Object getDescription() {
		return description;
	}
	public void setDescription(Object description) {
		this.description = description;
	}
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}
	
}
