package com.innouni.south.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.innouni.south.activity.R;
import com.innouni.south.entity.VersionInfo;
import com.innouni.south.widget.MsgBox;

/**
 * �汾���µĹ�����
 * @author HuGuojun
 * @data 2013-08-23
 */
public class UpdateVersionUtil {
	
	private static Context context;
	private Resources resources;
	
	private ProgressDialog progress;
	private ProgressDialog progressBar;
	
	private boolean showLoading = false;
	
	static UpdateVersionUtil updateUtil = new UpdateVersionUtil();
	
	public static UpdateVersionUtil getInstance(Context mContext) {
		context = mContext;
		return updateUtil == null ? new UpdateVersionUtil() : updateUtil;
	}
	
	public void setShowLoading(boolean showLoading){
		this.showLoading = showLoading;
	}
	
	public void startCheckVersion() {
		resources = context.getResources();
		progress = new ProgressDialog(context);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setMessage(resources.getString(R.string.ver_checking));
		
		progressBar = new ProgressDialog(context);
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setTitle(R.string.app_name);
		progressBar.setMessage(resources.getString(R.string.ver_apk_downloading));
		progressBar.setMax(100);
		
		new UpdateTask().execute();
	}
	
	private class UpdateTask extends AsyncTask<String, Integer, InputStream> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(showLoading){
				progress.show();
			}
		}
		
		@Override
		protected InputStream doInBackground(String... params) {
			try {
				String url = resources.getString(R.string.app_url) + "/upload/app/update.xml";
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setConnectTimeout(5000);
				conn.setDoOutput(true); 
		        conn.setDoInput(true); 
		        conn.setRequestMethod("GET"); 
		        conn.setUseCaches(false); 
		        conn.setInstanceFollowRedirects(true);
		        conn.setRequestProperty("Content-Type", "text/html;UTF-8"); 
		        conn.setRequestProperty("Connection", "Keep-Alive"); 
		        if(conn.getResponseCode() == 200){
					return conn.getInputStream();
				}else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(InputStream result) {
			super.onPostExecute(result);
			if(progress.isShowing()) progress.hide();
			if(null != result){
				VersionInfo info = parseVersionXml(result);
				if(null == info || "".equals(info.getVersionNum())){
					if(showLoading){
						//��ʾ�������쳣,���Ժ�����
						dialogAlter(R.string.ver_update_exception);
					}
				}else {
					float updateVer = Float.valueOf(info.getVersionNum());
					if(updateVer <= getVersion()){
						if(showLoading){
							//��ʾ��ǰ�Ѿ������°汾���������
							dialogAlter(R.string.no_new_version);
						}
					}else {
						//���°汾,ѯ���Ƿ���²���
						updateVer(info);
					}
				}
			}else {
				//��ʾ�������쳣,���Ժ�����
				if(showLoading){
					//��ʾ�������쳣,���Ժ�����
					dialogAlter(R.string.ver_update_exception);
				}
			}
		}
	}
	
	/**
	 * ���°汾
	 * @param info
	 */
	private void updateVer(final VersionInfo info) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.app_update_dialog_layout, null);
		TextView contentView = (TextView) view.findViewById(R.id.text_ver_content);
		contentView.setText(info.getContent());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view).setTitle(R.string.ver_update);
		builder.setPositiveButton(R.string.ver_update_now, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//��������APK����
				new DownApkTask(info).execute();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.ver_update_next, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	private class DownApkTask extends AsyncTask<String, Integer, String>{
		
		private int allLength = 0;
		private File file = null;
		private VersionInfo verInfo;
		
		public DownApkTask(VersionInfo info){
			this.verInfo = info;
			resources = context.getResources();
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String savePath = Environment.getExternalStorageDirectory().getPath()+"/Android/data/";
				File tmpFile = new File(savePath);
				if (!tmpFile.exists()) {
					tmpFile.mkdir();
				}
				file = new File(tmpFile, resources.getString(R.string.app_name) + verInfo.getVersionNum()+".apk");
				if (file.exists()){
					file.delete();
				}
				file.createNewFile();
				if(verInfo.getApkUrl().endsWith("apk")){
					URL url = new URL(verInfo.getApkUrl());
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					allLength = conn.getContentLength();
					InputStream inputStream = conn.getInputStream();
					conn.connect();
					if(null != inputStream){
						if (conn.getResponseCode() >= 400) {
							Toast.makeText(context, R.string.net_time_out, Toast.LENGTH_LONG).show();
						} else {
							int currentLen  = 0;
							byte[] buf = new byte[inputStream.available()];
							FileOutputStream fos = new FileOutputStream(file);
							DataOutputStream dataOutputStream = new DataOutputStream(fos);
							int len = 0;
							while ((len = inputStream.read(buf)) != -1) {
								if (len <= 0) {
									break;
								} else {
									dataOutputStream.write(buf, 0, len);
									dataOutputStream.flush();
									currentLen += len;
									publishProgress((int) ((currentLen  * 100)/((float) allLength)));
								}
							}
							fos.close();
							inputStream.close();
						}
					}
					conn.disconnect();
				}
			} catch (Exception e) {
				if(file.exists()){
					file.delete();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressBar.setProgress(0);
			progressBar.hide();
			if(null != file && file.exists()){
				openAPKFile(file, Float.valueOf(verInfo.getVersionNum()));
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progressBar.setProgress(values[0]);
		}
	}

	/**
	 * �Ի�����ʾ
	 * @param resId
	 */
	private void dialogAlter(int resId){
		MsgBox.alert(resources.getString(resId), context);
	}
	
	/**
	 * ��ȡ�汾��
	 * @return ��ǰӦ�õİ汾��
	 */
	private float getVersion(){
		try {
	         PackageManager manager = context.getPackageManager();
	         PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
	         return Float.valueOf(info.versionName);
	     } catch (Exception e) {
	         e.printStackTrace();
	         return 0;
	     }
	 }
	
	/**
	 * Pull��ʽ �����°汾XML�ļ�
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	private VersionInfo parseVersionXml(InputStream xml) {
		try{
			VersionInfo info = new VersionInfo();
			XmlPullParser pullParser = Xml.newPullParser();
			pullParser.setInput(xml, "UTF-8");
			int event = pullParser.getEventType();
			while(event != XmlPullParser.END_DOCUMENT){
				switch (event) {
				case XmlPullParser.START_TAG:
					if("versionNum".equals(pullParser.getName())){
						info.setVersionNum(pullParser.nextText());
					}else if ("apkUrl".equals(pullParser.getName())) {
						info.setApkUrl(pullParser.nextText());
					}else if ("content".equals(pullParser.getName())) {
						info.setContent(pullParser.nextText());
					}
					break;
				}
				event = pullParser.next();
			}
			return info;
		}catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��APK���а�װ
	 * @param file
	 * @param version
	 */
	private void openAPKFile(File file,float version) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
