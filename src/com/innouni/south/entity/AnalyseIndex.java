package com.innouni.south.entity;

/**
 * 技术分析指标(包括技术指标和移动平均指标)
 * 
 * @author HuGuojun
 * @date 2013-12-3 下午2:17:41
 * @modify
 * @version 1.0.0
 */
public class AnalyseIndex {

	private String name;
	private String price1;
	private String price2;
	private String suggest1;
	private String suggest2;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice1() {
		return price1;
	}
	public void setPrice1(String price1) {
		this.price1 = price1;
	}
	public String getPrice2() {
		return price2;
	}
	public void setPrice2(String price2) {
		this.price2 = price2;
	}
	public String getSuggest1() {
		return suggest1;
	}
	public void setSuggest1(String suggest1) {
		this.suggest1 = suggest1;
	}
	public String getSuggest2() {
		return suggest2;
	}
	public void setSuggest2(String suggest2) {
		this.suggest2 = suggest2;
	}
}
