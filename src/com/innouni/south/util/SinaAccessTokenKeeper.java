package com.innouni.south.util;


import com.weibo.sdk.android.Oauth2AccessToken;

import android.content.Context;

/**
 * 用于保存和删除新浪{@code Access_token }的工具类
 * @author HuGuojun
 * @data 2013-08-15
 */
public class SinaAccessTokenKeeper {
	
	private static ShareUtil shareUtil;
	
	/**
	 * 保存token
	 * @param context
	 * @param token
	 */
	public static void keepAccessToken(Context context, Oauth2AccessToken token) {
		shareUtil = ShareUtil.getInstance(context);
		shareUtil.setStringValues(ShareUtil.SINA_ACCESS_TOKEN, token.getToken());
		shareUtil.setLongValues(ShareUtil.SINA_EXPIRES, token.getExpiresTime());
	}
	
	/**
	 * 删除token
	 * @param context
	 * @param key
	 */
	public static void clear(Context context){
	    shareUtil = ShareUtil.getInstance(context);
	    shareUtil.removeShareValues(ShareUtil.SINA_ACCESS_TOKEN);
	    shareUtil.removeShareValues(ShareUtil.SINA_EXPIRES);
	}

	/**
	 * 从配置文件读取token
	 * @param context
	 * @return
	 */
	public static Oauth2AccessToken readAccessToken(Context context){
		shareUtil = ShareUtil.getInstance(context);
		Oauth2AccessToken token = new Oauth2AccessToken();
		token.setToken(shareUtil.getStringValues(ShareUtil.SINA_ACCESS_TOKEN));
		token.setExpiresTime(shareUtil.getLongValues(ShareUtil.SINA_EXPIRES));
		return token;
	}
}
