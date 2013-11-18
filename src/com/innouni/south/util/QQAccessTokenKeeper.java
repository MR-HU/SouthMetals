package com.innouni.south.util;

import com.innouni.south.entity.QQAccessToken;

import android.content.Context;

/**
 * ���ڱ����ɾ��QQ{@code Access_token }�Ĺ�����
 * @author HuGuojun
 * @data 2013-08-21
 */
public class QQAccessTokenKeeper {
	
	private static ShareUtil shareUtil;
 
	/**
	 * ����token
	 * @param context
	 * @param {@link QQAccessToken}
	 */
	public static void keepAccessToken(Context context, QQAccessToken token){
		shareUtil = ShareUtil.getInstance(context);
		shareUtil.setStringValues(ShareUtil.QQ_ACCESS_TOKEN, token.getAccessToken());
		shareUtil.setStringValues(ShareUtil.QQ_OPEN_ID, token.getOpenId());
		shareUtil.setLongValues(ShareUtil.QQ_EXPIRES, token.getExpiresIn());
	}
	
	/**
	 * ɾ��token
	 * @param context
	 * @param key
	 */
	public static void clear(Context context){
	    shareUtil = ShareUtil.getInstance(context);
	    shareUtil.removeShareValues(ShareUtil.QQ_ACCESS_TOKEN);
	    shareUtil.removeShareValues(ShareUtil.QQ_OPEN_ID);
	    shareUtil.removeShareValues(ShareUtil.QQ_EXPIRES);
	}
	
	/**
	 * �������ļ���ȡtoken
	 * @param context
	 * @return
	 */
	public static QQAccessToken readAccessToken(Context context){
		shareUtil = ShareUtil.getInstance(context);
		QQAccessToken token = new QQAccessToken();
		token.setAccessToken(shareUtil.getStringValues(ShareUtil.QQ_ACCESS_TOKEN));
		token.setOpenId(shareUtil.getStringValues(ShareUtil.QQ_OPEN_ID));
		token.setExpiresIn(shareUtil.getLongValues(ShareUtil.QQ_EXPIRES));
		return token;
	}
}
