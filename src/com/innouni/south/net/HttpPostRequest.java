package com.innouni.south.net;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.innouni.south.activity.R;

/**
 * HTTP POST请求
 * @author HuGuojun
 * @data 2013-09-03
 */
public class HttpPostRequest {
	
	public static HttpPost getHttpPost(String url){
		HttpPost request = new HttpPost(url);
		return request;
	}
	
	public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException,IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	
	public static String getDataFromWebServer(Context context, String action, List<NameValuePair> params){
		String url = context.getString(R.string.app_url) + "/?c=app&a=" + action;
		HttpPost request = HttpPostRequest.getHttpPost(url);
		String result = null;
		try{
			if (null != params) {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			HttpResponse response = HttpPostRequest.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode() == 200){
				result = EntityUtils.toString(response.getEntity());
			}
		}catch(ClientProtocolException e){
			result = "net_err";
		}catch(IOException e){
			result = "net_err";
		}catch(Exception e){
			result = "net_err";
		}
		return result;
	}
	
	/**
	 * 从南方白银网获取新闻数据
	 * @param params
	 * @return  String
	 * @exception
	 */
	public static String getDataFromWebServer(List<NameValuePair> params){
		String url = "http://www.drssm.com/?api";
		HttpPost request = HttpPostRequest.getHttpPost(url);
		String result = null;
		try{
			if (null != params) {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			HttpResponse response = HttpPostRequest.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode() == 200){
				result = EntityUtils.toString(response.getEntity());
			}
		}catch(ClientProtocolException e){
			result = "net_err";
		}catch(IOException e){
			result = "net_err";
		}catch(Exception e){
			result = "net_err";
		}
		return result;
	}
	
	public static String getDataFromWebServer(Context context, String url){
		HttpPost request = HttpPostRequest.getHttpPost(url);
		String result = null;
		try{
			HttpResponse response = HttpPostRequest.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode() == 200){
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}
		}catch(ClientProtocolException e){
			result = "net_err";
		}catch(IOException e){
			result = "net_err";
		}catch(Exception e){
			result = "net_err";
		}
		return result;
	}
	
	public static String getDataFromWebServerByGet(String url) {
		String result = "net_err";
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			result = "net_err";
		}
		return result;
	}
}
