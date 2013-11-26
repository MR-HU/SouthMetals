package com.innouni.south.entity;

/**
 * 专家实体类
 * @author HuGuojun
 * @data 2013-09-03
 */
public class ExpertInfo {
	
	private Object id;
	private Object imageUrl;
	private Object name;
	private Object description;
	
	public ExpertInfo(){}
	
	public ExpertInfo(Object id, Object imageUrl, Object name, Object description) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.name = name;
		this.description = description;
	}
	
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public Object getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(Object imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Object getName() {
		return name;
	}
	public void setName(Object name) {
		this.name = name;
	}
	public Object getDescription() {
		return description;
	}
	public void setDescription(Object description) {
		this.description = description;
	}
	
}
