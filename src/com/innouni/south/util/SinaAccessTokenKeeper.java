package com.innouni.south.util;


import com.weibo.sdk.android.Oauth2AccessToken;

import android.content.Context;

/**
 * ���ڱ����ɾ������{@code Access_token }�Ĺ�����
 * @author HuGuojun
 * @data 2013-08-15
 */
public class SinaAccessTokenKeeper {
	
	private static ShareUtil shareUtil;
	
	/**
	 * ����token
	 * @param context
	 * @param token
	 */
	public static void keepAccessToken(Context context, Oauth2AccessToken token) {
		shareUtil = ShareUtil.getInstance(context);
		shareUtil.setStringValues(ShareUtil.SINA_ACCESS_TOKEN, token.getToken());
		shareUtil.setLongValues(ShareUtil.SINA_EXPIRES, token.getExpiresTime());
	}
	
	/**
	 * ɾ��token
	 * @param context
	 * @param key
	 */
	public static void clear(Context context){
	    shareUtil = ShareUtil.getInstance(context);
	    shareUtil.removeShareValues(ShareUtil.SINA_ACCESS_TOKEN);
	    shareUtil.removeShareValues(ShareUtil.SINA_EXPIRES);
	}

	/**
	 * �������ļ���ȡtoken
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
