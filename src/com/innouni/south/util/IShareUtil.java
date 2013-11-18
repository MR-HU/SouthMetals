package com.innouni.south.util;


/**
 * 为{@link ShareUtils}工具类的存储提供{@code Key }值常量
 * 
 * @author HUGuojun
 * @data 2013-11-18
 */
public interface IShareUtil {

	/** SharedPreferences存储文件的名称 */
	public static final String SHARENAME = "southShareInfo";
	
	/** QQ互联申请的APP_ID */
	public static final String QQ_APP_ID = "100505815";
	
	/** QQ互联申请的APP_KEY */
	public static final String QQ_APP_KEY = "bef8b1fe71e83b89dea8592437088af4";
	
	/** QQ授权的内容 */
	public static final String QQ_SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,upload_photo,"
        				+ "add_share,add_topic,list_album,upload_pic,add_album,set_user_face,get_vip_info,get_vip_rich_info,get_intimate_friends_weibo,match_nick_tips_weibo";
	
	/** 人人开放平台申请的APP_ID */
	public static final String RENREN_APP_ID = "240149";
	
	/** 人人开放平台申请的API_KEY */
	public static final String RENREN_API_KEY = "792bcf37fd384046a7fb150b1f8bcbaa";
	
	/** 人人开放平台申请的SECRET_KEY */
	public static final String RENREN_SECRET_KEY = "719b099301bc4caab79ed3aa7023a3fb";
	
	/** 腾讯微博开放平台申请的APP_KEY */
	public static final String TENCENT_APP_KEY = "801402968";
	
	/** 腾讯微博开放平台申请的APP_SECRET */
	public static final String Tencent_APP_SECRET = "538465818ecd4778cbf4e91f5b0bd189";
	
	/** 腾讯微博开放平台申请的授权回调页 */
	public static final String TENCENT_REDIRECT_URL = "http://www.innouni.com";
	
	/** 新浪微博开放平台申请的APP_KEY */
	public static final String SINA_APP_KEY = "2611018552";

	/** 新浪微博开放平台申请的APP_SECRET */
	public static final String SINA_APP_SECRET = "65de0d2bf0536fc480aea848aa5c5d17";
	
	/** 新浪微博开放平台申请的授权回调页 */
	public static final String SINA_REDIRECT_URL = "http://www.innouni.com";

	/** 新浪微博授权的内容 */
	public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
						+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
						+ "follow_app_official_microblog";
	

	/**-------------以下字段表示的内容会在配置文件中存储,当用户账号退出后与这个账号相关的个人信息会被清除-------------*/
	/**-------------以下字段表示的内容会在配置文件中存储,当用户账号退出后与这个账号相关的个人信息会被清除-------------*/
	
	
	/** 新浪微博授权后获得的令牌 */
	public static final String SINA_ACCESS_TOKEN = "sinaToken";

	/** 新浪令牌的有效期 */
	public static final String SINA_EXPIRES = "sinaExpires";
	
	/** 腾讯微博授权后获得的令牌 */
	public static final String TENCENT_ACCESS_TOKEN = "tencentToken";

	/** 腾讯令牌的有效期 */
	public static final String TENCENT_EXPIRES = "tencentExpires";
	
	/** QQ授权后获得的令牌 */
	public static final String QQ_ACCESS_TOKEN = "qqToken";
	
	/** QQ令牌的有效期 */
	public static final String QQ_EXPIRES = "qqExpires";
	
	/** QQ用于唯一标识用户身份(每一个openid与QQ号码对应)*/
	public static final String QQ_OPEN_ID = "qqOpenid";

	/** 腾讯微博授权后保存的一些东西 */
	String TENCENT_OPEN_ID = "tencentOpenId";
	
	String TENCENT_OPEN_KEY = "tencentOpenKey";
	
	String TENCENT_KEY = "tencentKey";
	
	String TENCENT_URL = "tencentUrl";
	
	/** 是否首次启动程序 */
	String FIRSTINSTALL = "firstInstall";
	
	/** 软件信息标题 */
	String VERSION_TITLE = "versionTitle";
	
	/** 软件信息内容 */
	String VERSION_CONTENT = "versionContent";
	
	/** 服务热线 */
	String SERVER_LINE = "serverLine";
	
	String USERID = "userID";
	String USERNAME = "userName";
	String VIP = "vip";
	String EMAIL = "email";
	String PHONE = "phone";
	String ISBINDQQ = "isBindQQ";
	String ISBINDWEIBO = "isBindWeibo";
}
