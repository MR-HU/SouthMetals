package com.innouni.south.cache;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.graphics.Bitmap;

/**
 * Õº∆¨ª∫¥Ê¿‡
 * @author HuGuojun
 * @data 2013-11-18
 */
public class ImagesCache extends HashMap<String, WeakReference<Bitmap>>{

	private static final long serialVersionUID = 1L;
	
	public boolean isCached(String cacheKey){
		WeakReference<Bitmap> weakReference = get(cacheKey);
		return this.containsKey(cacheKey) &&  weakReference!= null && weakReference.get()!=null;
	}
	
}