package com.innouni.south.util;

import android.content.Context;

import com.tencent.weibo.oauthv2.OAuthV2;

/**
 * 用于保存和删除腾讯微博{@code Access_token }的工具类
 * @author HuGuojun
 * @data 2013-08-19
 */
public class TencentAccessTokenKeeper {
	
	private static ShareUtil shareUtil;
	
	/**
	 * 保存token
	 * @param context
	 * @param token
	 */
	public static void keepAccessToken(Context context, OAuthV2 token) {
		shareUtil = ShareUtil.getInstance(context);
		shareUtil.setStringValues(ShareUtil.TENCENT_ACCESS_TOKEN, token.getAccessToken());
		shareUtil.setStringValues(ShareUtil.TENCENT_EXPIRES, token.getExpiresIn());
		shareUtil.setStringValues(ShareUtil.TENCENT_OPEN_ID, token.getOpenid());
		shareUtil.setStringValues(ShareUtil.TENCENT_OPEN_KEY, token.getOpenkey());
		shareUtil.setStringValues(ShareUtil.TENCENT_KEY, token.getClientId());
		shareUtil.setStringValues(ShareUtil.TENCENT_URL, token.getClientSecret());
	}
	
	/**
	 * 删除token
	 * @param context
	 * @param key
	 */
	public static void clear(Context context){
	    shareUtil = ShareUtil.getInstance(context);
	    shareUtil.removeShareValues(ShareUtil.TENCENT_ACCESS_TOKEN);
	    shareUtil.removeShareValues(ShareUtil.TENCENT_EXPIRES);
	    shareUtil.removeShareValues(ShareUtil.TENCENT_OPEN_ID);
	    shareUtil.removeShareValues(ShareUtil.TENCENT_OPEN_KEY);
	    shareUtil.removeShareValues(ShareUtil.TENCENT_KEY);
	    shareUtil.removeShareValues(ShareUtil.TENCENT_URL);
	}

	/**
	 * 从配置文件读取token
	 * @param context
	 * @return
	 */
	public static OAuthV2 readAccessToken(Context context){
		shareUtil = ShareUtil.getInstance(context);
		OAuthV2 oAuthV2 = new OAuthV2();
		oAuthV2.setAccessToken(shareUtil.getStringValues(ShareUtil.TENCENT_ACCESS_TOKEN));
		oAuthV2.setExpiresIn(shareUtil.getStringValues(ShareUtil.TENCENT_EXPIRES));
		oAuthV2.setOpenid(shareUtil.getStringValues(ShareUtil.TENCENT_OPEN_ID));
		oAuthV2.setOpenkey(shareUtil.getStringValues(ShareUtil.TENCENT_OPEN_KEY));
		oAuthV2.setClientId(shareUtil.getStringValues(ShareUtil.TENCENT_KEY));
		oAuthV2.setClientSecret(shareUtil.getStringValues(ShareUtil.TENCENT_URL));
		return oAuthV2;
	}
}
