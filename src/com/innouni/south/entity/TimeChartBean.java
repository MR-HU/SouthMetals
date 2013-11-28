package com.innouni.south.entity;

import java.util.ArrayList;

import org.stockchart.points.LinePoint;

/**
 * 时间点
 * 
 * @author HuGuojun
 * @date 2013-11-28 下午2:22:32
 * @modify
 * @version 1.0.0
 */
public class TimeChartBean {
	
	private String date;
	private ArrayList<LinePoint> linePoints = new ArrayList();
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public ArrayList<LinePoint> getLinePoints() {
		return linePoints;
	}
	public void setLinePoints(ArrayList<LinePoint> linePoints) {
		this.linePoints = linePoints;
	}
	
}
