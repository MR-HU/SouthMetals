package com.innouni.south.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.innouni.south.cache.ImagesCache;
import com.innouni.south.entity.UserInfo;
import com.innouni.south.util.ShareUtil;

/**
 * ����Ӧ�ó���
 * @author HuGuojun 
 * @data 2013-11-18
 */
public class MainApplication extends Application {
	
	/** �б��ҳ����ʱÿҳ��ʾ�������Ϣ���� */
	public static final int PAGE_SIZE = 8;
	
	private static MainApplication application;
	
	public static MainApplication getApplication(){
		return application;
	}
	
	private boolean inActivity = false;
	
	public boolean isInActivity(){
		return inActivity;
	}
	
	public void setInActivity(boolean inActivity){
		this.inActivity = inActivity;
	}
	
	private ImagesCache imagesCache;
	
	private List<Activity> allActiivty = new ArrayList<Activity>();
	
	private UserInfo userInfo;
	
	private ShareUtil shareUtil;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		imagesCache = new ImagesCache();
		shareUtil = ShareUtil.getInstance(this);
		initUserInfo();
	}
	
	/**
	 * �˳�APPǰ������
	 */
	public void exitApp(){
		for(Activity activity : allActiivty){
			if(null != activity) 
				activity.finish();
		}
		if(imagesCache != null){
			imagesCache.clear();
		}
		setImagesCache(null);
		setUserInfo(null);
	}
	
	/**
	 * ��������ļ��еĸ����˻���Ϣ
	 */
	public void clearPreference() {
		shareUtil.removeShareValues(ShareUtil.SINA_ACCESS_TOKEN);
		shareUtil.removeShareValues(ShareUtil.SINA_EXPIRES);
		shareUtil.removeShareValues(ShareUtil.TENCENT_ACCESS_TOKEN);
		shareUtil.removeShareValues(ShareUtil.TENCENT_EXPIRES);
		shareUtil.removeShareValues(ShareUtil.QQ_ACCESS_TOKEN);
		shareUtil.removeShareValues(ShareUtil.QQ_EXPIRES);
		shareUtil.removeShareValues(ShareUtil.QQ_OPEN_ID);
		shareUtil.removeShareValues(ShareUtil.TENCENT_OPEN_ID);
		shareUtil.removeShareValues(ShareUtil.TENCENT_OPEN_KEY);
		shareUtil.removeShareValues(ShareUtil.TENCENT_KEY);
		shareUtil.removeShareValues(ShareUtil.TENCENT_URL);
		shareUtil.removeShareValues(ShareUtil.USERID);
		shareUtil.removeShareValues(ShareUtil.USERNAME);
		shareUtil.removeShareValues(ShareUtil.VIP);
		shareUtil.removeShareValues(ShareUtil.EMAIL);
		shareUtil.removeShareValues(ShareUtil.PHONE);
		shareUtil.removeShareValues(ShareUtil.ISBINDQQ);
		shareUtil.removeShareValues(ShareUtil.ISBINDWEIBO);
	}
	
	public void setActivity(Activity activity){
		allActiivty.add(activity);
	}
	
	public ImagesCache getImagesCache() {
		return imagesCache;
	}

	public void setImagesCache(ImagesCache imagesCache) {
		this.imagesCache = imagesCache;
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	private void initUserInfo(){
		UserInfo info = getUserInfo();
		if (null == info) {
			info = new UserInfo();
			info.setUserId(shareUtil.getIntValues(ShareUtil.USERID));
			info.setUserName(shareUtil.getStringValues(ShareUtil.USERNAME));
			info.setVip(shareUtil.getBooleanValues(ShareUtil.VIP));
			info.setEmail(shareUtil.getStringValues(ShareUtil.EMAIL));
			info.setPhone(shareUtil.getStringValues(ShareUtil.PHONE));
			info.setIsBindQQ(shareUtil.getBooleanValues(ShareUtil.ISBINDQQ));
			info.setIsBindWeibo(shareUtil.getBooleanValues(ShareUtil.ISBINDWEIBO));
			//��������ļ���δ�����û�ID,���ȡ��Ĭ��ֵ-1
			if (info.getUserId().toString().equals("-1")) {
				setUserInfo(null);
			} else {
				setUserInfo(info);
			}
		}
	}
}

