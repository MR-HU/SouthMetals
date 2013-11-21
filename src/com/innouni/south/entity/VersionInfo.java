package com.innouni.south.entity;

/**
 * 封装版本信息类
 * @author HuGuojun
 * @data 2013-08-23
 */
public class VersionInfo {
	
	//版本号
	private String versionNum;
	//APK下载地址
	private String apkUrl;
	//新版更新内容介绍
	private String content;
	
	public String getVersionNum() {
		return versionNum;
	}
	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
