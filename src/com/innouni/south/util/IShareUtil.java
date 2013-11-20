package com.innouni.south.util;


/**
 * Ϊ{@link ShareUtils}������Ĵ洢�ṩ{@code Key }ֵ����
 * 
 * @author HUGuojun
 * @data 2013-11-18
 */
public interface IShareUtil {

	/** SharedPreferences�洢�ļ������� */
	public static final String SHARENAME = "southShareInfo";
	
	/** QQ���������APP_ID */
	public static final String QQ_APP_ID = "100560437";
	
	/** QQ���������APP_KEY */
	public static final String QQ_APP_KEY = "a75b84d065ebab3fd65d055ff471ed98";
	
	/** QQ��Ȩ������ */
	public static final String QQ_SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,upload_photo,"
        				+ "add_share,add_topic,list_album,upload_pic,add_album,set_user_face,get_vip_info,get_vip_rich_info,get_intimate_friends_weibo,match_nick_tips_weibo";
	
	/** ���˿���ƽ̨�����APP_ID */
	public static final String RENREN_APP_ID = "240149";
	
	/** ���˿���ƽ̨�����API_KEY */
	public static final String RENREN_API_KEY = "792bcf37fd384046a7fb150b1f8bcbaa";
	
	/** ���˿���ƽ̨�����SECRET_KEY */
	public static final String RENREN_SECRET_KEY = "719b099301bc4caab79ed3aa7023a3fb";
	
	/** ��Ѷ΢������ƽ̨�����APP_KEY */
	public static final String TENCENT_APP_KEY = "801402968";
	
	/** ��Ѷ΢������ƽ̨�����APP_SECRET */
	public static final String Tencent_APP_SECRET = "538465818ecd4778cbf4e91f5b0bd189";
	
	/** ��Ѷ΢������ƽ̨�������Ȩ�ص�ҳ */
	public static final String TENCENT_REDIRECT_URL = "http://www.innouni.com";
	
	/** ����΢������ƽ̨�����APP_KEY */
	public static final String SINA_APP_KEY = "6357510";

	/** ����΢������ƽ̨�����APP_SECRET */
	public static final String SINA_APP_SECRET = "43f334c588868f44a8f156458ba0c0be";
	
	/** ����΢������ƽ̨�������Ȩ�ص�ҳ */
	public static final String SINA_REDIRECT_URL = "http://www.innouni.com";

	/** ����΢����Ȩ������ */
	public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
						+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
						+ "follow_app_official_microblog";
	

	/**-------------�����ֶα�ʾ�����ݻ��������ļ��д洢,���û��˺��˳���������˺���صĸ�����Ϣ�ᱻ���-------------*/
	/**-------------�����ֶα�ʾ�����ݻ��������ļ��д洢,���û��˺��˳���������˺���صĸ�����Ϣ�ᱻ���-------------*/
	
	
	/** ����΢����Ȩ���õ����� */
	public static final String SINA_ACCESS_TOKEN = "sinaToken";

	/** �������Ƶ���Ч�� */
	public static final String SINA_EXPIRES = "sinaExpires";
	
	/** ��Ѷ΢����Ȩ���õ����� */
	public static final String TENCENT_ACCESS_TOKEN = "tencentToken";

	/** ��Ѷ���Ƶ���Ч�� */
	public static final String TENCENT_EXPIRES = "tencentExpires";
	
	/** QQ��Ȩ���õ����� */
	public static final String QQ_ACCESS_TOKEN = "qqToken";
	
	/** QQ���Ƶ���Ч�� */
	public static final String QQ_EXPIRES = "qqExpires";
	
	/** QQ����Ψһ��ʶ�û����(ÿһ��openid��QQ�����Ӧ)*/
	public static final String QQ_OPEN_ID = "qqOpenid";

	/** ��Ѷ΢����Ȩ�󱣴��һЩ���� */
	String TENCENT_OPEN_ID = "tencentOpenId";
	
	String TENCENT_OPEN_KEY = "tencentOpenKey";
	
	String TENCENT_KEY = "tencentKey";
	
	String TENCENT_URL = "tencentUrl";
	
	/** �Ƿ��״��������� */
	String FIRSTINSTALL = "firstInstall";
	
	/** �����Ϣ���� */
	String VERSION_TITLE = "versionTitle";
	
	/** �����Ϣ���� */
	String VERSION_CONTENT = "versionContent";
	
	/** �������� */
	String SERVER_LINE = "serverLine";
	
	String USERID = "userID";
	String USERNAME = "userName";
	String VIP = "vip";
	String EMAIL = "email";
	String PHONE = "phone";
	String ISBINDQQ = "isBindQQ";
	String ISBINDWEIBO = "isBindWeibo";
}
