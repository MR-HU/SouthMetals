package com.innouni.south.entity;

/**
 * �ƾ�ָ��ʵ����
 * @author HuGuojun
 * @data 2013-09-04
 */
public class FinancialData {
	
	/** ָ��ID*/
	private Object id;
	/** ָ������ */
	private Object name;
	/** ���� */
	private Object current;
	/** �� */
	private Object open;
	/** ���� */
	private Object close;
	/** �ǵ� */
	private Object up;
	/** ���� */
	private Object percent;
	
	public Object getName() {
		return name;
	}
	
	public void setName(Object name) {
		this.name = name;
	}
	
	public Object getCurrent() {
		return current;
	}
	
	public void setCurrent(Object current) {
		this.current = current;
	}
	
	public Object getClose() {
		return close;
	}
	public void setClose(Object close) {
		this.close = close;
	}
	
	public Object getUp() {
		return up;
	}
	
	public void setUp(Object up) {
		this.up = up;
	}
	
	public Object getPercent() {
		return percent;
	}
	
	public void setPercent(Object percent) {
		this.percent = percent;
	}
	
	public Object getId() {
		return id;
	}
	
	public void setId(Object id) {
		this.id = id;
	}

	public Object getOpen() {
		return open;
	}

	public void setOpen(Object open) {
		this.open = open;
	}
}
