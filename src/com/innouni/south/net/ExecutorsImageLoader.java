package com.innouni.south.net;

import java.io.DataInputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.innouni.south.app.MainApplication;
import com.innouni.south.cache.ImagesCache;
import com.innouni.south.util.FileUtil;
import com.innouni.south.util.Util;

/**
 * ͼƬ���� ���� ���ش洢
 * @author HuGuojun
 * @data 2013-09-05
 */
public class ExecutorsImageLoader {
	
	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	private static MainApplication application;

	private static Context context;
	
	private static ImagesCache imageCache;
	
	private FileUtil fileUtil;
	
	private static ExecutorsImageLoader executorsLoader = new ExecutorsImageLoader();

	private Handler handler = new Handler();
	
	/** �����Ѿ����ʹ���ͼƬ·�� */
	public ArrayList<String> cacheUrl = new ArrayList<String>();
	
	public static ExecutorsImageLoader getInstance(Context mContext){
		context = mContext;
		application = MainApplication.getApplication();
		imageCache = application.getImagesCache();
		return executorsLoader == null ? new ExecutorsImageLoader() : executorsLoader;
	}
	
	/**
	 * �������� Url ����ͼƬ����
	 * @param imageUrl ͼƬ�������ַ
	 * @param callback 
	 * @param downData
	 * @return
	 */
	public Bitmap loadDrawable(final String imageUrl, final ImageCallback callback) {
		final String cacheName = Util.convertStr(imageUrl);
		fileUtil = FileUtil.getInstance(context);
		if(loadCache(cacheName) == null){
			executorService.submit( new Runnable() {
				public void run() {
					final Bitmap map = downImageFromUrl(imageUrl); 
					handler.post(new Runnable() {
						@Override
						public void run() {
							callback.imageLoaded(map);
						}
					});
				}
			});
		}
		return imageCache.isCached(cacheName) ? imageCache.get(cacheName).get() : null;
	}

	/**
	 * ��������ͼƬ 
	 * @param imageUrl ����ͼƬͼƬ��ַ
	 * @param isCacheFile �Ƿ���Ϊ���ػ����ļ�
	 * @return �ɹ���������ͼƬ, ���򷵻�null
	 */
	private Bitmap downImageFromUrl(String imageUrl) {
		if(cacheUrl.contains(imageUrl)){
			return loadCache(Util.convertStr(imageUrl));
		}else{
			Log.e("cache", "imageUrl= " +imageUrl);
			byte[] buffer = null;
			Bitmap  bit= null;
			try {
				URL url = new URL(imageUrl);
				HttpURLConnection connectoin = (HttpURLConnection) url.openConnection();
				DataInputStream dis = new DataInputStream(connectoin.getInputStream());
				if (connectoin.getContentLength() > 0){
					buffer = new byte[connectoin.getContentLength()];
					dis.readFully(buffer);
				}
				dis.close();
				connectoin.disconnect();
				if(null != buffer && buffer.length > 0){
					BitmapFactory.Options options = new BitmapFactory.Options();
			        options.inPreferredConfig = Bitmap.Config.RGB_565;  //�����ڴ��еı��뷽ʽ
			        //inPurgeable��Ϊtrue��ʾʹ��BitmapFactory������Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
					options.inPurgeable = true;
					 // ��inPurgeable һ��ʹ��   
					options.inInputShareable = true;  
					
					bit = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
					imageCache.put(Util.convertStr(imageUrl), new WeakReference<Bitmap>(bit));
					//��������ΪXXX.CACHEIMG���ļ����ڱ������ص�ͼƬ
					File downFile = fileUtil.createNewFile(Util.convertStr(imageUrl));
					ExecutorSaveCacheServer.getInstance().savaCacheFile(buffer, downFile);
				}
				cacheUrl.add(imageUrl);
			} catch (Exception e) {
				Log.i("cache", "downImageFromUrl  error" + e.toString());
			}
			return bit;
		}
	}

	/**
	 * ���ر��ػ�������
	 * @param cacheName ͼƬ�Ļ�������
	 * @return �б��ػ����ļ��򷵻ػ���ͼƬ, ���򷵻�null
	 */
	private Bitmap loadCache(String cacheName){
		// ���������ʹӻ�����ȡ������
		if (imageCache.isCached(cacheName) ) {
			WeakReference<Bitmap> softReference = imageCache.get(cacheName);
			return softReference.get();
		} else {
			boolean isExistsCache = fileUtil.isExistsFile(cacheName);
			Log.e("cache", ">>>>>>>>>>>>>>>>>>>>"+isExistsCache);
			if(isExistsCache){
				String filePath = fileUtil.findFileByName(cacheName).getAbsolutePath();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(filePath, options);
				int scale = Math.min((int) (options.outHeight / (float) 80), (int) (options.outWidth / (float)60));
				Log.v("file", "org---"+scale);
				if(scale > 2){
					scale = 3;
				}
				Log.v("file", "inSampleSize---"+scale);
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				imageCache.put(cacheName, new WeakReference<Bitmap>(BitmapFactory.decodeFile(filePath, options)));
				return BitmapFactory.decodeFile(filePath, options);
			}else{
				return null;
			}
		}
	}
	
	//����翪�ŵĻص��ӿ�
	public interface ImageCallback {
		//�˷�������������Ŀ������ͼ����Դ
		public void imageLoaded(Bitmap imageDrawable);
	}

}